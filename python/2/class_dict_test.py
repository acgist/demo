
from dataclasses import asdict, dataclass

@dataclass
class person_dict:
    name: str
    age : int

class person:
    def __init__(self, name, age):
        self.name = name
        self.age  = age

p = person("张三", 18)
print(p)
print(vars(p))
print(p.__dict__)

pd = person_dict("张三", 18)
print(pd)
print(vars(pd))
print(pd.__dict__)
print(asdict(pd))