# Модификации Ping VPN 1.1.17 — ad-free + стабильность

Артефакт: **`PingVPN-1.1.17-noads-stable.apk`** (universal, подписан v2+v3).
Все правки внесены на уровне smali/манифеста (Dart-логика в `libapp.so` и
Go-ядро в `libgojni.so` скомпилированы и не патчатся — поэтому работаем на
границе native ↔ Flutter и в Android-glue VPN-сервиса).

## 1. Вырезана вся реклама

### 1.1 Нейтрализован MethodChannel `ads_manager`
`com/pingsecure/client/app/handlers/AdsMethodHandler` — единственный мост,
через который Flutter-сторона просит загрузить/показать рекламу
(методы `prepareAds`, `showOnConnect`, `showOnDisconnect`, `setUserConsent`).
Остальные сети (AppLovin/IronSource/AdMob/Pangle/FB) подключены как
**медиация под Yandex Ads** — без вызова Yandex-загрузчика они мертвы.

`onMethodCall` (исходно ~1800 строк smali) заменён на no-op, который сразу
отвечает «рекламы нет», чтобы UI-флоу (подключение/отключение) продолжался
мгновенно:

```smali
.method public final onMethodCall(Lio/flutter/plugin/common/MethodCall;Lio/flutter/plugin/common/MethodChannel$Result;)V
    .locals 1
    sget-object v0, Ljava/lang/Boolean;->FALSE:Ljava/lang/Boolean;
    invoke-interface {p2, v0}, Lio/flutter/plugin/common/MethodChannel$Result;->success(Ljava/lang/Object;)V
    return-void
.end method
```

Побочный эффект: вызов `MobileAds.initialize` (Yandex), который раньше шёл
**внутри** `prepareAds`, больше не выполняется — SDK не инициализируется.

### 1.2 Удалена авто-инициализация рекламных SDK (AndroidManifest.xml)
Убраны два `ContentProvider`, которые поднимали ad-SDK на старте процесса:

```diff
- <provider android:authorities="…MobileAdsInitializeProvider" android:name="com.yandex.mobile.ads.core.initializer.MobileAdsInitializeProvider"/>
- <provider android:authorities="…mobileadsinitprovider"    android:name="com.google.android.gms.ads.MobileAdsInitProvider"/>
```

Итог: ни Yandex, ни Google Ads не инициализируются вообще → нет фоновых
рекламных запросов/телеметрии. (AdMob и так был на **тестовом** App ID
`ca-app-pub-3940256099942544~…`.)

## 2. Стабильнее подключение

> Глубокая логика реконнекта Xray-core — в Go (`libgojni.so`), не патчится.
> Доступные и безопасные рычаги в Android-слое:

### 2.1 Зажат MTU TUN-интерфейса ≤ 1400
`com/pingsecure/client/app/bg/xray/Xray` — перед `VpnService.Builder.setMtu()`.
Большой MTU (1500) на мобильных/PPPoE-сетях ловит фрагментацию и
PMTU-blackhole (зависания/обрывы). Клампим к 1400, меньшие значения из
конфига сохраняются:

```smali
    move-result p1
    const/16 v0, 0x578      # 1400
    if-le p1, v0, :pv_mtu_ok
    const/16 p1, 0x578
    :pv_mtu_ok
    invoke-virtual {p2, p1}, Landroid/net/VpnService$Builder;->setMtu(I)…
```
(В establish уже были `setBlocking(true)` и `setUnderlyingNetworks(null)`.)

### 2.2 `START_NOT_STICKY` → `START_STICKY`
`com/pingsecure/client/app/bg/XrayService.onStartCommand$app_release` возвращал
`2` (START_NOT_STICKY) — после убийства системой VPN-сервис не поднимался.
Заменено на `1` (START_STICKY): ОС перезапускает foreground VPN-сервис, если
он был убит под нагрузкой/в памяти → соединение живёт дольше.

```diff
-    const/4 v0, 0x2   # START_NOT_STICKY
+    const/4 v0, 0x1   # START_STICKY
```

## Сборка

```bash
java -jar tools/apktool.jar b -f work/decompiled/base -o modded-unsigned.apk
tools/build-tools/zipalign -p -f 4 modded-unsigned.apk aligned.apk
tools/build-tools/apksigner sign --ks tools/release.keystore … --out PingVPN-1.1.17-noads-stable.apk aligned.apk
```

Проверки: `apksigner verify` → v2+v3 OK; `aapt2 dump badging` → пакет, launcher
и `native-code: arm64-v8a` на месте; ad-init провайдеров в манифесте: 0.

## Оговорки
- Подпись — новый self-signed ключ ⇒ ставить «с нуля» (`adb install`),
  поверх магазинной версии не обновится.
- **PairIP license check** не трогался (по умолчанию не блокирует sideload
  жёстко). Если будет мешать — отдельным шагом убирается обёртка
  `com.pairip.application.Application` и `LicenseContentProvider`.
- MTU-кламп и START_STICKY — консервативные улучшения стабильности на стороне
  Android; качество самих VPN-серверов/сети ими не меняется.
