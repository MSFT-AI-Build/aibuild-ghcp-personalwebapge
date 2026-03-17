#!/bin/sh
set -eu

# output/ 폴더를 정적 서버로 미리보기한다.
PORT="${1:-8000}"

echo "Serving output/ at http://localhost:${PORT}"
python3 -m http.server "${PORT}" --directory output