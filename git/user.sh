#!/bin/sh

git filter-branch --env-filter '

OLD_EMAIL="acgist@ACGIST"
CORRECT_NAME="acgist"
CORRECT_EMAIL="289547414@qq.com"

if [ "$GIT_COMMITTER_EMAIL" = "$OLD_EMAIL" ]
then
    export GIT_COMMITTER_NAME="$CORRECT_NAME"
    export GIT_COMMITTER_EMAIL="$CORRECT_EMAIL"
fi

if [ "$GIT_AUTHOR_EMAIL" = "$OLD_EMAIL" ]
then
    export GIT_AUTHOR_NAME="$CORRECT_NAME"
    export GIT_AUTHOR_EMAIL="$CORRECT_EMAIL"
fi

' -f --tag-name-filter cat -- --branches --tags

# 多次执行删除目录：.git/refs/original
# 如果上面目录可以不用删除末尾添加参数：-f
# 强制推送远程仓库：git push origin --force --all
# 恢复：git filter-branch -f --index-filter 'git rm --cached --ignore-unmatch Rakefile' HEAD