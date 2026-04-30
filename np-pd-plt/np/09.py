import numpy as np

x = np.zeros(10)
print(x)
i = [0, 0, 1, 3, 4]
x[i] += 1
print(x)
np.add.at(x, i, 1)
print(x)
