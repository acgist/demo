array = [1, 2, 3]
print(array)
print(array[0])
print(array[-1])
print(array[-2])
print(','.join(map(lambda x: str(x), array)))
print(','.join(["1", "2", "3"]))

delete_array = ["1", "2", "3", "1"]
print(delete_array)
# delete_array.remove("1")
# print(delete_array)
while "1" in delete_array:
    delete_array.remove("1")
print(delete_array)
