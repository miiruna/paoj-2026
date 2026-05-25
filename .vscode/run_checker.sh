#!/bin/bash
FILE="$1"
PKG=$(echo "$FILE" | grep -o 'laboratory[0-9]*/exercise[0-9]*')

if [ -z "$PKG" ]; then
    echo "Deschide un fisier din laboratorul dorit si incearca din nou."
    exit 1
fi

LAB=$(echo "$PKG" | cut -d/ -f1)
EX=$(echo "$PKG" | cut -d/ -f2)
OUT="/tmp/${LAB}_${EX}_out"

mkdir -p "$OUT"
echo ">>> Compilez $LAB/$EX..."

# Colectăm toate sursele din laborator + test (pentru dependențe cross-exercise)
SOURCES=$(find "src/com/pao/$LAB" "src/com/pao/test" -name "*.java")

if echo "$SOURCES" | xargs javac -cp lib/java-diff-utils-4.15.jar -d "$OUT" 2>&1; then
    echo ">>> Rulez Checker..."
    if java -cp "$OUT:lib/java-diff-utils-4.15.jar" "com.pao.${LAB}.${EX}.Checker" 2>/dev/null; then
        :
    else
        echo ">>> (Nu exista Checker — rulez Main...)"
        java -cp "$OUT:lib/java-diff-utils-4.15.jar" "com.pao.${LAB}.${EX}.Main"
    fi
else
    echo "Eroare la compilare."
    exit 1
fi
