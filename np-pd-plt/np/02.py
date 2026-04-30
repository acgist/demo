import numpy as np

x = np.array([1, 2, 3])
print(x)
# print(x.reshape((1, 2)))
print(x.reshape((1, 3)))
print(x[np.newaxis, np.newaxis, :, np.newaxis, np.newaxis])

print('\n')

a1 = np.array([1, 2, 3])
a2 = np.array([1, 2, 3]) * 2
a3 = np.array([1, 2, 3]) * 3

print(np.concat([a1, a2, a3]))
print(np.concat([a1[np.newaxis, :], a2[np.newaxis, :], a3[np.newaxis, :]], axis=0))
# print(np.stack([a1, a2, a3]))
# print(np.hstack([a1, a2, a3]))
# print(np.vstack([a1, a2, a3]))

print(np.split(a1, 1))
print(np.split(a1, [1]))
print(np.split(a1, [1, 2]))