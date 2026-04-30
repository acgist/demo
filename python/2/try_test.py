try:
    1 / 0
except ZeroDivisionError:
    print("除数不能为0")
else:
    print("else")
finally:
    print("finally")
print("end")