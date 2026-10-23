#!/usr/bin/env bash
# Builds all three variants and assembles the multi-version fat jar.
# JDK 21 builds the legacy variants (Loom 1.10), JDK 25 builds the root
# project (Loom 1.18). Set NHF_JDK21 / NHF_JDK25, or rely on the
# JAVA_HOME_21_X64 / JAVA_HOME_25_X64 vars actions/setup-java exports.
set -euo pipefail
ROOT="$(cd "$(dirname "$0")" && pwd)"

JDK21="${NHF_JDK21:-${JAVA_HOME_21_X64:-}}"
JDK25="${NHF_JDK25:-${JAVA_HOME_25_X64:-}}"
if [ -z "$JDK21" ]; then echo "error: NHF_JDK21 (JDK 21 home) is required" >&2; exit 1; fi
if [ -z "$JDK25" ]; then echo "error: NHF_JDK25 (JDK 25 home) is required" >&2; exit 1; fi

echo "=== legacy121 variant (JDK 21) ==="
(cd "$ROOT/variants/legacy121" && JAVA_HOME="$JDK21" ./gradlew build)

echo "=== legacy125 variant (JDK 21) ==="
(cd "$ROOT/variants/legacy125" && JAVA_HOME="$JDK21" ./gradlew build)

echo "=== root fat jar (JDK 25) ==="
(cd "$ROOT" && JAVA_HOME="$JDK25" ./gradlew build)

echo "Done. Fat jar: build/libs/no-hide-flags-*.jar"
