import numpy as np

x = np.array([1, 2, 3, 4, 5, 6])
print(x)
print(x < 4)
print(np.less(x, 4))
print(np.count_nonzero(x < 4))
print(np.sum(x < 4))
print(np.any(x < 4))
print(np.all(x < 4))
print((x > 1) & (x < 4))
print((x < 2) | (x > 4))
print(x[(x == 2) | (x >= 4)])
