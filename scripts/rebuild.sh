#!/usr/bin/env bash
#
# rebuild.sh — reproducible decompile + rebuild pipeline for Ping VPN 1.1.17
#
# Takes the original split-bundle archive (.apks / .xapk produced from an
# Android App Bundle) and produces a single, installable, signed universal APK.
#
# Tooling (downloaded into ./tools the first time):
#   - apktool 2.11.1            (smali/resource decode + rebuild)
#   - jadx 1.5.1               (dex -> readable Java, for analysis only)
#   - Android build-tools r34  (aapt2, zipalign, apksigner)
#   - JDK 21, keytool
#
# Usage:
#   ./scripts/rebuild.sh /path/to/Ping\ VPN_1.1.17.apks
#
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
TOOLS="$ROOT/tools"
WORK="$ROOT/work"
APKS="${1:-$WORK/extracted/apks/Ping VPN_1.1.17.apks}"

APKTOOL="$TOOLS/apktool.jar"
JADX="$TOOLS/jadx/bin/jadx"
BT="$TOOLS/build-tools"
KS="$TOOLS/release.keystore"
KS_PASS="pingvpn123"
KS_ALIAS="pingvpn"

mkdir -p "$WORK" "$WORK/extracted/apks" "$WORK/decompiled" "$WORK/rebuilt" "$WORK/final"

# --- 0. keystore (self-signed; replace for production) -----------------------
if [[ ! -f "$KS" ]]; then
  keytool -genkeypair -v -keystore "$KS" -alias "$KS_ALIAS" \
    -keyalg RSA -keysize 2048 -validity 10000 \
    -storepass "$KS_PASS" -keypass "$KS_PASS" \
    -dname "CN=Ping VPN, OU=Dev, O=Ping, L=City, S=State, C=RU"
fi

# --- 1. explode the App Bundle split set -------------------------------------
#   .apks is a zip of: base.apk + split_config.<abi>.apk + split_config.<dpi>.apk
#                      + split_config.<lang>.apk
cd "$WORK/extracted"
unzip -o "$APKS" -d apks >/dev/null

# --- 2. decode base.apk (smali + resources, needed to rebuild) ----------------
java -jar "$APKTOOL" d -f -o "$WORK/decompiled/base" "apks/base.apk"

# --- 3. (analysis) decompile dex -> Java with jadx ----------------------------
"$JADX" --output-dir "$WORK/decompiled/base-java" --threads-count 4 \
        --deobf --show-bad-code "apks/base.apk" || true

# --- 4. merge the abi split's native libs into base ---------------------------
#   The base split carries NO lib/ — the .so live in split_config.arm64_v8a.apk.
#   Merging them makes the rebuilt APK self-contained.
mkdir -p "$WORK/decompiled/base/lib"
( cd /tmp && rm -rf libx && mkdir libx && cd libx \
  && unzip -oq "$WORK/extracted/apks/split_config.arm64_v8a.apk" 'lib/*' \
  && cp -r lib/* "$WORK/decompiled/base/lib/" )

# --- 5. strip split requirements from the manifest ----------------------------
#   A universal APK must not demand the config splits that no longer exist.
python3 - "$WORK/decompiled/base/AndroidManifest.xml" <<'PY'
import re, sys
p = sys.argv[1]; x = open(p, encoding='utf-8').read()
x = x.replace(' android:requiredSplitTypes="base__abi,base__density"', '')
x = re.sub(r'<meta-data android:name="com.android.vending.splits.required"[^>]*/>', '', x)
x = re.sub(r'<meta-data android:name="com.android.vending.splits"[^>]*/>', '', x)
open(p, 'w', encoding='utf-8').write(x)
PY

# --- 6. rebuild -> universal unsigned APK -------------------------------------
java -jar "$APKTOOL" b -f "$WORK/decompiled/base" \
     -o "$WORK/rebuilt/universal-unsigned.apk"

# --- 7. zipalign + sign (v2 + v3; v1 not needed for minSdk 24) -----------------
"$BT/zipalign" -p -f 4 "$WORK/rebuilt/universal-unsigned.apk" \
               "$WORK/rebuilt/universal-aligned.apk"
"$BT/apksigner" sign \
  --ks "$KS" --ks-pass "pass:$KS_PASS" --key-pass "pass:$KS_PASS" \
  --v1-signing-enabled true --v2-signing-enabled true --v3-signing-enabled true \
  --out "$WORK/final/PingVPN-1.1.17-universal-signed.apk" \
  "$WORK/rebuilt/universal-aligned.apk"
"$BT/apksigner" verify --verbose "$WORK/final/PingVPN-1.1.17-universal-signed.apk"

echo "OK -> $WORK/final/PingVPN-1.1.17-universal-signed.apk"
