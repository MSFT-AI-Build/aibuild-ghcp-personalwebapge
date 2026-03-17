---
name: fast-deploy
description: 'Use when preparing a dependency-light static presentation for GitHub Pages deployment, checking relative paths, and guiding a fast CLI-based publish flow.'
---

# Fast Deploy

## When to Use

- 발표자료를 GitHub Pages로 빠르게 배포할 때
- 배포 전 상대 경로를 점검할 때
- 별도 프레임워크 없이 정적 사이트를 운영할 때

## Procedure

1. `output/`의 상대 경로 구조를 점검한다.
2. GitHub Pages 기준 진입 파일과 정적 자산 경로를 확인한다.
3. 필요한 경우 배포 전 체크 스크립트를 실행한다.
4. `gh` 또는 GitHub 설정 기준 배포 절차를 안내한다.

## Deployment Focus

- `index.html` 위치
- 상대 경로 유지
- 누락 자산 방지
- GitHub Pages 호환성