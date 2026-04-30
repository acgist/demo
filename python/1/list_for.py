names  = ['张三', '李四', '王五', '赵六']
for name in names:
    print(name)

for index, name in enumerate(names):
    print(index, name)

# names = [1, 2, 0, 9, 3, 4, 5]
# print(max(names))
# print(min(names))
# print(sum(names))

x = [value + str(1) for value in names]
print(x)

for value in x:
    print("1=" + value)

for value in x[1:]:
    print("2=" + value)

for value in x[-1:]:
    print("3=" + value)

o = x
print(x)
print(o)
del o[0]
print(x)
print(o)
o = x[:]
del o[0]
print(x)
print(o)