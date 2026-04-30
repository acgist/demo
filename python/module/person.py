class Person:
    def __init__(self, name: str, age: int):
        self.name = name
        self.age = age
    def say(self):
        print(f"我是{self.name}，我今年{self.age}岁")

def create_person(name: str, age: int) -> Person:
    return Person(name, age)
