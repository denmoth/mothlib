#!/usr/bin/env sh
# Downloads Temurin 17 into .jdks/jdk-17 (curl + tar only). Run from repo root once.
set -eu
ROOT=$(CDPATH= cd -- "$(dirname "$0")/.." && pwd)
JDK_DIR="$ROOT/.jdks/jdk-17"
if [ -x "$JDK_DIR/bin/java" ]; then
  echo "Already present: $JDK_DIR"
  exit 0
fi
mkdir -p "$ROOT/.jdks"
TMP=$(mktemp)
trap 'rm -f "$TMP"' EXIT
curl -fsSL "https://api.adoptium.net/v3/binary/latest/17/ga/linux/x64/jdk/hotspot/normal/eclipse?project=jdk" -o "$TMP"
tar -xzf "$TMP" -C "$ROOT/.jdks"
EXTRACTED=$(find "$ROOT/.jdks" -maxdepth 1 -type d -name 'jdk-17*' | head -1)
[ -n "$EXTRACTED" ] || { echo "Unpack failed"; exit 1; }
rm -rf "$JDK_DIR"
mv "$EXTRACTED" "$JDK_DIR"
echo "OK: $JDK_DIR"
"$JDK_DIR/bin/java" -version
