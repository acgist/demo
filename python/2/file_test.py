import json

from pathlib import Path

path = Path("2/x.txt")

if path.exists():
    print(path.read_text(encoding="utf-8").splitlines())
else:
    path.write_text("hello world")

print(json.dumps("1234"))