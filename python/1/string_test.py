message = '1234""1234'\
"123'123'123"\
"!"

print(message)

message = ('1234""1234'
"123'123'123"
"!")

print(message)

message = "helloX world a b c d e h"

print(message.title())
print(message.lower())
print(message.upper())

print("----")
print(message)
print(message.removeprefix("h"))
print(message.removeprefix("hellox"))
print(message.removeprefix("helloX"))
print(message.removeprefix("helloX1"))

print("""
1111\n2
""")

print(r"""
1111\n2
""")
