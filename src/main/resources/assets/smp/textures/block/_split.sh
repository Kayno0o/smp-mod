#!/bin/bash

INPUT="$1"
SIZE=16

if [ ! -f "$INPUT" ]; then
  echo "❌ Error: File '$INPUT' not found."
  exit 1
fi

base="${INPUT%.*}"
ext="${INPUT##*.}"

width=$(identify -format "%w" "$INPUT")
height=$(identify -format "%h" "$INPUT")

rows=$((height / SIZE))
cols=$((width / SIZE))
index=0

for ((y=0; y<rows; y++)); do
  for ((x=0; x<cols; x++)); do
    px=$((x * SIZE))
    py=$((y * SIZE))
    magick "$INPUT" -crop "${SIZE}x${SIZE}+${px}+${py}" +repage "${base}_${index}.${ext}"
    ((index++))
  done
done
