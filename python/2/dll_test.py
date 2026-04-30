"""
g++ -shared -o dll.dll dll.cpp
g++ -shared -o -fPIC dll.so dll.cpp

pybind11
"""

import ctypes
import platform

# 自动加载对应库
if platform.system() == "Windows":
    lib = ctypes.CDLL("./dll.dll")
else:
    lib = ctypes.CDLL("./dll.so")

lib.add.restype = ctypes.c_int
res = lib.add(10, 20)
print("10 + 20 = ", res)

lib.hello.restype = ctypes.c_char_p
message = lib.hello().decode()
print(message)