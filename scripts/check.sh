#!/bin/sh
set -eu

# 워크샵 결과물의 최소 구조를 점검한다.

echo "[check] required directories"
test -d output
test -d output/assets
test -d output/assets/css
test -d output/assets/js
test -d output/assets/img

echo "[check] recommended files"
if [ -f output/index.html ]; then
  echo "- output/index.html found"
else
  echo "- output/index.html not found yet"
fi

if command -v gh >/dev/null 2>&1; then
  echo "- gh available"
else
  echo "- gh not available"
fi

echo "Check completed"