class Person:
    # 基础构造与生命周期
    def __new__(cls, name, age):
        print("1. 创建实例")
        return super().__new__(cls)
    def __init__(self, name, age):
        print("2. 初始化实例")
        self.name = name
        self.age  = age
    def __del__(self):
        print("3. 销毁实例")
    # 字符串表示
    def __str__(self):
        return f"str = {self.name} {self.age}"
    def __repr__(self):
        return f"Person name = {self.name} age = {self.age}"
    # 模拟容器行为
    def __len__(self):
        return len(self.__dict__)
    def __iter__(self):
        return iter(self.__dict__)
    def __getitem__(self, key):
        return self.__dict__[key]
    def __setitem__(self, key, value):
        self.__dict__[key] = value
    def __delitem__(self, key):
        del self.__dict__[key]
    def __contains__(self, key):
        return key in self.__dict__
    # 属性访问控制
    def __getattr__(self, name):
        return self.__dict__[name]
    def __setattr__(self, name, value):
        self.__dict__[name] = value
    def __delattr__(self, name):
        del self.__dict__[name]
    # 让对象像函数一样调用
    def __call__(self, *args, **kwargs):
        print("4. 调用实例")
    # 比较运算符
    def __eq__(self, other):
        return self.name == other.name and self.age == other.age
    def __ne__(self, other):
        return not self.__eq__(other)
    def __lt__(self, other):
        return self.age < other.age
    def __gt__(self, other):
        return self.age > other.age
    def __ge__(self, other):
        return self.age >= other.age
    def __le__(self, other):
        return self.age <= other.age
    # 上下文管理器（With语句）
    def __enter__(self):
        print("5. 进入上下文")
        return self
    def __exit__(self, exc_type, exc_val, exc_tb):
        print("6. 退出上下文")
    
p = Person("张三", 18)
print(p)
print(repr(p))
print(len(p))
for value in p:
    print("iter = " + value)
print(p.name)
p["name"] = "李四"
print(p["name"])
print("name" in p)
del p["name"]
print("name" in p)
p()
with p:
    pass
# p.address = "中国"
# for key, value in p.__dict__.items():
#     print(key, value)
