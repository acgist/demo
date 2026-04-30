# print("hello world")
# message = "hello world"
# print(message)
class Person:
    def __init__(self, name, age):
        self.name = name
        self.age = age
        print("person is created")
    def __del__(self):
        print("person is deleted")
p = Person("张三", 18)
print("1")
p1 = p
p = None
# del p
print("2")
del p1
print("3")

array = [1, 2, 3, 4, 5]
print(array)
# del array[2]
del array[1:2]
# array.pop(0)
# array.remove(3)
print(array)

str = "1234"
print(str)
str = str[:1] + str[2:]
print(str)
