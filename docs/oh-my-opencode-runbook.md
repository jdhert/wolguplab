# Oh My OpenCode Runbook

이 문서는 현재 프로젝트에서 oh-my-opencode Team Mode를 실행하는 순서를 기록한다.

## 현재 Worktree
```text
D:/wolguplab              [master]
D:/wolguplab-003-backend  [feat/003-backend-api]
D:/wolguplab-004-infra    [feat/004-docker-compose]
```

## 설정 파일
- `.opencode/oh-my-openagent.jsonc`
- `.omo/teams/wolguplab-mvp/config.json`
- `docs/agent-workflow.md`
- `docs/agent-board.md`

## 실행 전 확인
```powershell
cd d:\wolguplab
git worktree list
opencode --version
oh-my-opencode --version
```

## OpenCode 실행
```powershell
cd d:\wolguplab
opencode
```

OpenCode를 실행한 뒤 아래 요청으로 시작한다.

```text
Use oh-my-opencode Team Mode with the wolguplab-mvp team.

Goal:
1. backend-003 should turn docs/plans/003-backend-simulation-api.md into Ready only.
2. infra-004 should inspect docs/plans/004-docker-compose.md and list blockers only.
3. qa-main should review both outputs without editing files.

Rules:
- Follow docs/agent-workflow.md and docs/agent-board.md.
- Use each member's worktreePath.
- Do not modify files outside each member's Write Scope.
- Do not mark any Plan Done.
- Backend implementation remains blocked until JDK 17 is available.
```

## 실행 후 확인
각 worktree에서 변경 내용을 확인한다.

```powershell
cd d:\wolguplab-003-backend
git status --short --branch

cd d:\wolguplab-004-infra
git status --short --branch

cd d:\wolguplab
git status --short --branch
```

## 통합 기준
- Plan 상태와 `docs/agent-board.md` 상태가 일치해야 한다.
- 각 작업은 Write Scope를 벗어나면 안 된다.
- QA Reviewer가 확인하기 전에는 main에 병합하지 않는다.
- Integrator가 최종적으로 검증 결과와 구현 요약을 Plan에 반영한다.
