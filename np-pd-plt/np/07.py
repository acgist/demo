import numpy as np

rand = np.random.RandomState(42)
x = rand.randint(100, size = 10)
print(x)
print(x[[2, 6, 4]])
index = np.array([[1, 4], [5, 2]])
print(x[index])