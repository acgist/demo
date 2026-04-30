import numpy as np

x = np.array([7, 2, 3, 1, 6, 5, 4])
# x = np.random.randint(0, 100, (10))
print(x)
print(np.partition(x, 3))