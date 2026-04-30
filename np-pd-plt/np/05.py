import numpy as np

a = np.array([1, 2, 3])
b = np.array([1, 2, 4])
c = np.array([1, 2, 5, 6])
print(a + b)
# print(a + c)
d = np.ones((3, 3))
print(a[:, np.newaxis] + d)
print(a[:, np.newaxis] + b)