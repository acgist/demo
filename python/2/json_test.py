import json

from dataclasses import asdict, dataclass

@dataclass
class Person:
    name: str
    age : int

    @property
    def namefull(self):
        return self.name + ' is ' + str(self.age) + ' years old'
    
p = Person('张三', 18)
print(p.namefull)
print(json.dumps(asdict(p), ensure_ascii = False))

def serialize_with_properties(object):
    data = asdict(object)
    for attr_name in dir(type(object)):
        attr = getattr(type(object), attr_name)
        print(attr)
        if isinstance(attr, property):
            data[attr_name] = getattr(object, attr_name)
    return data

print(json.dumps(p, default=serialize_with_properties, ensure_ascii = False))
