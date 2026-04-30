obj = {"name": "张三", "age": 18}
print(obj["name"])
obj["name"] = "李四"
obj["nike"] = "李四"
print(obj["name"])
print(obj["nike"])
print(obj)
for key, value in enumerate(obj):
    print(key, value)
for key, value in obj.items():
    print(key, value)
del obj["nike"]
print(obj)
# print(obj["xx"])
print(obj.get("xx"))
obj.update(x = 2)
obj.update({"y": 3})
print(obj)