import numpy as np

x = np.array([0, 2, 1, 10, 3, 4, 6, 2, 1])
print(np.sort(x))
print(x)
print(x.argsort())
print(x[x.argsort()])
x.sort()
print(x)

v = np.random.randint(0, 10, (4, 6))
print(v)
print(np.sort(v, axis = 0))
print(np.sort(v, axis = 1))
