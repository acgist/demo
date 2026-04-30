import os
import hashlib

files = os.listdir("D:/download")
hash = []
for file in files:
    if os.path.isfile(os.path.join("D:/download", file)):
        with open(os.path.join("D:/download", file), 'rb') as stream:
            text = stream.read()
            sha1 = hashlib.sha1(text).hexdigest()
            if sha1 in hash:
                print(f"文件已经存在{file}")
            else:
                hash.append(sha1)