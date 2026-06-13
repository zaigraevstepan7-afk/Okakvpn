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

---

# Раунд 2 — дебло­ат, приватность, DNS

Артефакт: **`PingVPN-1.1.17-noads-clean.apk`** (на базе noads-stable + правки ниже).

## 3. Убран PairIP (license check)
`com.pairip.application.Application` — это просто наследник настоящего
`com.pingsecure.client.app.Application`, добавляющий `LicenseClient.checkLicense()`
в `attachBaseContext`. В манифесте возвращён настоящий Application, удалён
`LicenseActivity`:
```diff
- android:name="com.pairip.application.Application"
+ android:name="com.pingsecure.client.app.Application"
- <activity android:name="com.pairip.licensecheck.LicenseActivity"/>
```
Проверка лицензии Google Play больше не выполняется.

## 4. Дебло­ат: удалены мёртвые рекламные нативные либы
Реклама отключена → их `.so` никогда не грузятся. Удалены из `lib/arm64-v8a/`:
`libapplovin-native-crash-reporter`, `libtapjoy`, `libpglarmor`,
`libtobEmbedPagEncrypt`, `libtt_ugen_layout`, `libnms`, `libapminsighta`,
`libapminsightb` (последние два — APM-телеметрия ByteDance).
Осталось 8 essential-либ: `libapp`, `libflutter`, `libdartjni`, `libgojni`
(Xray), `libtun2socks`, `libbuffer`, `libdatastore_shared_counter`,
`libfile_lock`.

## 5. Приватность: вырезана авто-инициализация рекламы/телеметрии
Удалены `ContentProvider`'ы, поднимавшие SDK на старте процесса (пинги в сеть
ещё до любого показа): MyTarget, AppLovin, Vungle, Facebook Audience, IronSource
(×2), Mintegral, Bigo, Maticoo, Yandex DebugPanel, а также AppMetrica
`PreloadInfoContentProvider`. Остались только легитимные провайдеры:
`androidx.startup.InitializationProvider`, `FirebaseInitProvider`,
`PicassoProvider`, `ShareFileProvider`.

Добавлены manifest-флаги отключения телеметрии Firebase/GA (фреймворк хранит
`true/false` как boolean, SDK читают через `getBoolean`):
```xml
firebase_analytics_collection_enabled = false
firebase_crashlytics_collection_enabled = false
firebase_performance_collection_enabled = false
google_analytics_adid_collection_enabled = false
google_analytics_default_allow_ad_personalization_signals = false
firebase_messaging_auto_init_enabled = false
```

## 6. Надёжные DNS
В `Xray.buildVpnInterface`, после цикла применения DNS из конфига, в TUN
добавляются резолверы-fallback **1.1.1.1 (Cloudflare)** и **8.8.8.8 (Google)** —
меньше залипаний резолва и DNS-leak. Конфигурационные DNS остаются приоритетными.

## Проверки round 2
- `apksigner verify` → v2+v3 OK
- Application = `com.pingsecure.client.app.Application` (PairIP убран)
- ad/telemetry auto-init провайдеров: **0**; легитимных: 4
- essential `.so` целы (`libgojni` извлекается в полные 33 323 880 B)
- Firebase kill-switch флаги присутствуют

---

# Раунд 3 — ребрендинг (Root VPN) + новый дизайн иконки/splash

Артефакт: **`RootVPN-1.1.17-noads-clean.apk`**.

## 7. Переименование
`android:label` "Ping VPN" → **"Root VPN"** (launcher-имя). Имена внутри UI —
в компилированном Flutter (`libapp.so`), не меняются.

## 8. Новая иконка + splash (векторно, без растровых ассетов)
- `res/drawable/ic_launcher_background.xml` — диагональный градиент индиго→синий→циан
  (`#4338CA → #2563EB → #06B6D4`).
- `res/drawable/ic_root_foreground.xml` — белый щит с замочной скважиной
  (security/root-мотив), `fillType="evenOdd"` для сквозного выреза.
- `res/mipmap-anydpi-v26/ic_launcher.xml` — adaptive-icon + `monochrome`
  (themed icons на Android 13+).
- `res/drawable/launch_background.xml` — splash в том же градиенте с центрированным лого.
- Легаси-растровые `mipmap-*/ic_launcher.webp` (Android ≤ 7) оставлены как есть —
  на API 26+ (≈все актуальные устройства) показывается новая векторная иконка.

## 9. Telegram-попап — почему НЕ убран
Это **серверное in-app сообщение**, а не реклама SDK. API приложения
(`libapp.so`): `/api/v1/app/message-shown`, `/api/v1/app/message-action-taken`,
конфиг с `/api/v1/system/get-current-config` и `/api/v1/init/initialize`.
Текст промо в бинаре/ассетах отсутствует — приходит с бэкенда
(`ping-vpn.vercel.app`), **с того же хоста, что и список серверов**
(`/api/v1/vpn/vpn-configurations`). Триггер показа — в компилированном Dart.
Поэтому убрать попап репакингом нельзя, не сломав загрузку серверов и без
исходников Flutter. Надёжно гасится только на стороне устройства
(Private DNS / AdGuard, блок хоста `ping-vpn.vercel.app`) — но это заблокирует и серверы.
