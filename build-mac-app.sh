#!/bin/bash

set -e

APP_NAME="Astroplanner"
DESKTOP_MODULE="astroplanner-desktop"
VERSION="0.0.1-SNAPSHOT"
MAIN_JAR="astroplanner-desktop-${VERSION}.jar"
MAIN_CLASS="io.github.biopuri.astroplanner.desktop.AstroplannerDesktopApplication"

export JAVA_HOME=$(/usr/libexec/java_home -v 21)
export PATH="$JAVA_HOME/bin:$PATH"

echo "Using Java:"
java -version

echo "Building Maven project..."
mvn clean package

cd "$DESKTOP_MODULE"

echo "Preparing jpackage input..."
rm -rf target/jpackage-input target/dist
mkdir -p target/jpackage-input target/dist

cp "target/${MAIN_JAR}" target/jpackage-input/

mvn dependency:copy-dependencies \
  -DincludeScope=runtime \
  -DoutputDirectory=target/jpackage-input

echo "Building macOS app..."
jpackage \
  --type app-image \
  --name "$APP_NAME" \
  --input target/jpackage-input \
  --main-jar "$MAIN_JAR" \
  --main-class "$MAIN_CLASS" \
  --java-options "--module-path \$APPDIR --add-modules javafx.controls,javafx.fxml" \
  --dest target/dist

echo "Done:"
echo "$DESKTOP_MODULE/target/dist/${APP_NAME}.app"