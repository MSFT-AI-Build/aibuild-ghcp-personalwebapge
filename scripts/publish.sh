#!/bin/sh
set -eu

# GitHub Pages 배포 전 확인 흐름을 안내한다.

echo "1. Run ./scripts/check.sh"
echo "2. Ensure output/index.html exists"
echo "3. Verify relative asset paths inside output/"
echo "4. Push the repository to GitHub"
echo "5. Configure GitHub Pages for the chosen source"
echo "6. If using gh, inspect repo settings and issues with gh commands"