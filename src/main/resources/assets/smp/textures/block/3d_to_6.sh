#!/bin/bash

INPUT="$1"
SIZE=16

if [ ! -f "$INPUT" ]; then
  echo "❌ Error: File '$INPUT' not found."
  exit 1
fi

declare -A coords=(
  [top]="1 0"
  [bottom]="1 2"
  [west]="0 1"
  [north]="1 1"
  [east]="2 1"
  [south]="3 1"
)

for face in "${!coords[@]}"; do
  read cx cy <<<"${coords[$face]}"
  x=$((cx * SIZE))
  y=$((cy * SIZE))
  base=$(basename "$INPUT" .png)
  magick "$INPUT" -crop "${SIZE}x${SIZE}+${x}+${y}" +repage "${base}_${face}.png"
done
