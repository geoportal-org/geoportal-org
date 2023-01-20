# Hooks

## Conventions

The commit message must match to the pattern:

`<JIRA ticket number><space><description>`
e.g.

`GPP-45 Update readme`

## Git hooks

- `commit-msg` - this hook validates the git message before committing to see if it matches to the naming convention. To install this hook copy this file to local .git/hooks/ directory
```shell
cp git-hooks/commit-msg .git/hooks/
```

## Gitlab hooks

- `pre-receive` - this hook validates the git message before pushing to gitlab to see if it matches to the naming convention. To install this hook copy this file to gitlab server to the custom_hooks directory
```
/data/gitlab/repositories/geoss/yellow-pages.git/custom_hooks
```
