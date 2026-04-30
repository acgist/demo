def test(a, b):
    print("1=", a, b)

def test(a, b, c):
    print("2=", a, b, c)

# 不支持重载

# test(1, 2)
test(1, 2, 3)

# def test(a, b, c = "", d):
#     print("3=", a, b, c, d)

def args_dynamic(*args):
    print(args)
args_dynamic(1, 2, 3)
arr = [1, 2, 3]
args_dynamic(*arr)

def map_dynamic(**kwargs):
    print(kwargs)
map_dynamic(a = 1, b = 2, c = 3)
obj = {"a": 1, "b": 2, "c": 3}
map_dynamic(**obj)

# import time

# time.sleep(1000000)
