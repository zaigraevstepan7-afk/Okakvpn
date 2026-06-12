# Ping VPN 1.1.17 — декомпиляция и пересборка

Разбор оригинального бандла (`Ping VPN_1.1.17.apks`, split-формат Android App
Bundle), высококачественная декомпиляция и сборка одного устанавливаемого
universal APK.

## 1. Входные данные

Загружен split-ZIP архив в двух частях:

| Файл | Размер |
|------|--------|
| `Ping_VPN_1.1.17.z01` | 26 214 400 B (часть 1) |
| `Ping_VPN_1.1.17.zip` | 22 708 587 B (финальная часть) |

Объединение split-архива (`zip -s 0 … --out combined.zip`) дало
`Ping VPN_1.1.17.apks` — это **набор split-APK из App Bundle**:

| Split | Размер | Содержимое |
|-------|--------|-----------|
| `base.apk` | 59.5 MB | dex, ресурсы, манифест, ассеты Flutter |
| `split_config.arm64_v8a.apk` | 20.7 MB | нативные библиотеки `lib/arm64-v8a/*.so` |
| `split_config.ru.apk` | 45 KB | русская локализация |
| `split_config.xxxhdpi.apk` | 188 KB | ресурсы плотности xxxhdpi |

## 2. Профиль приложения

| Параметр | Значение |
|----------|----------|
| package | `com.pingsecure.client.app` |
| versionName / versionCode | `1.1.17` / `58` |
| minSdk / targetSdk | `24` / `36` |
| Launcher | `com.pingsecure.client.app.MainActivity` |
| ABI | `arm64-v8a` |
| UI-фреймворк | **Flutter** (`libflutter.so`, `libapp.so`, `libdartjni.so`) |
| VPN-ядро | **Xray-core** (Go, `libgojni.so` ≈ 33 MB) + `libtun2socks.so` |
| Защита | **Google Play PairIP** — license check (`com.pairip.licensecheck`) |

### Архитектура

- **Flutter UI ↔ нативный слой** связаны через `MethodChannel` /
  `EventChannel`. Хендлеры в `com.pingsecure.client.app.handlers`:
  `MethodHandler`, `AdsMethodHandler`, `MobileInfoMethodHandler`,
  `ReviewHandler`, `UserIdMethodHandler`, `PlatformSettingsHandler`,
  `VPNStateStreamHandler`, `StatsStreamHandler`, `AndroidLogsHandler`.
- **VPN** реализован в `com.pingsecure.client.app.bg`:
  `VPNService` (Android `VpnService`) поднимает TUN-интерфейс, `xray.Xray`
  запускает Xray-core, `Tun2Socks` заворачивает TUN-трафик в локальный
  SOCKS, поднятый Xray. Конфиг туннеля строится билдером
  `xray.VpnConfig` (по умолчанию адрес `10.0.42.2/30`, MTU `1500`,
  маршруты/DNS/SOCKS-порт настраиваются).
- **Монетизация (реклама):** Yandex Ads (`ads.YandexAdsLoader`,
  `io.appmetrica`), AppLovin / IronSource mediation, Google AdMob
  (`com.google.android.gms.ads`), Pangle / ByteDance
  (`com.bytedance.sdk.openadsdk`), Facebook Audience Network
  (`assets/audience_network.dex`).
- **Сервисы Google:** Firebase (analytics, sessions, messaging),
  Play Install Referrer, Play Review API, WorkManager, Room, DataStore.

### О защите PairIP

Манифест объявляет `android:name="com.pairip.application.Application"` —
это PairIP-обёртка, выполняющая **проверку лицензии Google Play** через
`LicenseContentProvider` / `LicenseClient`. Это **«лёгкий» вариант** PairIP:
`libpairipcore.so` и зашифрованный VM-байткод в ассетах **отсутствуют** —
dex-классы не зашифрованы, поэтому декомпиляция полная и читаемая.

> ⚠️ **Рантайм-оговорка.** License check сверяет подпись/лицензию с Google
> Play. При установке пересобранного APK с **другим** ключом и в обход Play
> проверка лицензии не пройдёт. В этой версии она, как правило, не блокирует
> запуск жёстко (показывает диалог/повтор), но поведение зависит от
> устройства. Чтобы гарантированно отключить проверку, нужно убрать
> PairIP-обёртку из манифеста и `LicenseContentProvider` — намеренно **не
> делалось**, чтобы сохранить APK максимально близким к оригиналу.

## 3. Инструменты

| Инструмент | Версия | Назначение |
|-----------|--------|-----------|
| apktool | 2.11.1 | декод smali + ресурсов, пересборка |
| jadx | 1.5.1 | dex → читаемый Java (анализ) |
| Android build-tools | r34 | `aapt2`, `zipalign`, `apksigner` |
| JDK / keytool | 21 | подпись, генерация ключа |

## 4. Декомпиляция (высокое качество)

- **apktool** — полный декод `base.apk`: 9 dex → smali
  (`smali`, `smali_classes2..9`) + `assets/audience_network.dex` →
  `smali_assets`; ресурсы и `AndroidManifest.xml` декодированы в текст.
- **jadx** — `--deobf --show-bad-code`, dex → **26 466 Java-файлов**
  (Kotlin-код восстановлен в читаемый Java). Ядро приложения
  (`com.pingsecure.client.app.*`) декомпилируется чисто.

Артефакты:
- `decompiled-core/pingsecure-java/` — ключевые исходники приложения (Java).
- `decompiled-core/AndroidManifest.decoded.xml` — читаемый манифест.
- `PingVPN-1.1.17-decompiled-java.zip` — **полная** Java-декомпиляция (jadx).

## 5. Сборка universal APK

Базовый split не самодостаточен (нет `lib/`, локали и dpi — в отдельных
сплитах). Шаги пересборки в один устанавливаемый APK:

1. Скопированы все 16 `lib/arm64-v8a/*.so` из `split_config.arm64_v8a.apk`
   в декодированный base.
2. Из манифеста удалены требования сплитов:
   `android:requiredSplitTypes="base__abi,base__density"` и meta-data
   `com.android.vending.splits[.required]`.
3. `apktool b` → unsigned universal APK.
4. `zipalign -p 4` → выравнивание.
5. `apksigner sign` схемами **v2 + v3** (для minSdk 24 v1 не требуется).
6. `apksigner verify` — подпись валидна.

Проверка результата (`aapt2 dump badging`): пакет, версия, launcher и
`native-code: 'arm64-v8a'` на месте, требование сплитов отсутствует.

### Готовые артефакты

| Файл | Размер | Назначение |
|------|--------|-----------|
| `PingVPN-1.1.17-universal-signed.apk` | ≈ 92 MB | **устанавливаемый** universal APK (base + нативные либы) |
| `PingVPN-1.1.17-base-signed.apk` | ≈ 72 MB | пересобранный base-split (round-trip декомпиляции) |
| `PingVPN-1.1.17-decompiled-java.zip` | ≈ 80 MB | полная Java-декомпиляция |

SHA-256:
```
d7af3dd274310a843ea06b5f49e723310dfb4e2e52cdc6221b70ca781433a5e3  PingVPN-1.1.17-universal-signed.apk
676dfcb56a2099325766bcefb7a8cf04a8ad2d173e509353c042f1737cf5d726  PingVPN-1.1.17-base-signed.apk
b42fa912a4ee30d86ad05998dd30dfbec86f2a0cd76d426364ce99896b45873b  PingVPN-1.1.17-decompiled-java.zip
```

Подпись (self-signed; замените на свой ключ для продакшена):
```
CN=Ping VPN, OU=Dev, O=Ping  •  SHA-256 cert: 041cb42d…71b4ae
```

## 6. Воспроизведение

```bash
./scripts/rebuild.sh "/path/to/Ping VPN_1.1.17.apks"
# -> work/final/PingVPN-1.1.17-universal-signed.apk
```

## 7. Установка

```bash
adb install -r PingVPN-1.1.17-universal-signed.apk
```

Оригинал подписан другим (Play) ключом, поэтому **обновление поверх
магазинной версии не пройдёт** — сначала удалите установленную. Локали `ru`
и плотность `xxxhdpi` в universal не вмерджены (у них отдельные
`resources.arsc`); приложение использует дефолтные ресурсы — на работу VPN
не влияет.

---
*Примечание: материал подготовлен для анализа/обучения. Уважайте лицензию и
права правообладателя приложения.*
