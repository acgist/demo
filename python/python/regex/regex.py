#coding=utf-8
'''
正则表达式学习代码
'''

import re

regex = re.compile("\d")
# matchs = regex.match("123")
matchs = regex.search("abc123") # 注意search和match的区别
print "字符串查找"
if matchs != None:
    print matchs.group()
#     print matchs.groups() # 查询组需要使用()
else:
    print "没有匹配到数据"
matchs = regex.findall("abc123");
print "迭代器提取"
matchs = regex.finditer("abc123");
for item in matchs:
    print item.group()
matchs = regex.findall("abc123");
print "字符串提取"
for item in matchs:
    print item;
print "字符串替换"
print regex.sub("a", "d123d")
print "字符串分割"
print regex.split("a1b2c");
# print "abc";

class textUtils:
    'HTML工具'
    content = "";
    def __init__(self, content):
        self.content = content
    def replaceBlack(self):
        return self.content.replace(" ", "-")