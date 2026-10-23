@echo off
rem Builds all three variants and assembles the multi-version fat jar.
rem JDK 21 builds the legacy variants (Loom 1.10), JDK 25 builds the root
rem project (Loom 1.18). Override the install paths via NHF_JDK21 / NHF_JDK25.
setlocal
set "ROOT=%~dp0"
if not defined NHF_JDK21 set "NHF_JDK21=C:\Program Files\Java\jdk-21"
if not defined NHF_JDK25 set "NHF_JDK25=%USERPROFILE%\.jdks\jdk-25"

echo === legacy121 variant (JDK 21) ===
pushd "%ROOT%variants\legacy121"
set "JAVA_HOME=%NHF_JDK21%"
call gradlew.bat build || goto :fail
popd

echo === legacy125 variant (JDK 21) ===
pushd "%ROOT%variants\legacy125"
set "JAVA_HOME=%NHF_JDK21%"
call gradlew.bat build || goto :fail
popd

echo === root fat jar (JDK 25) ===
pushd "%ROOT%"
set "JAVA_HOME=%NHF_JDK25%"
call gradlew.bat build || goto :fail
popd

echo Done. Fat jar: build\libs\no-hide-flags-*.jar
exit /b 0

:fail
echo BUILD FAILED
exit /b 1
