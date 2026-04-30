#coding=utf-8
'''
文件操作示例代码
'''

import os
from _codecs import encode, decode
# from base64 import encode


cfiles = os.listdir("c:/")
# for cfile in cfiles:
#     print cfile + "=" + str(os.path.isfile(cfile))
result = os.popen("dir").read()
# print result.decode("GBK").encode("utf-8")
print encode(decode(result, "GBK"), "utf-8")