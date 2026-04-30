import numpy as np

x = np.random.randint(0, 10, (2, 3, 4))

print(x.ndim)
print(x.shape)
print(x.size)
print(x.dtype)
print(x.itemsize)
print(x.nbytes)

print(x)
print(x[0][0][0])
print(x[0, 0, 0])
print(x[0][0][-1])
print(x[0, 0, -1])

print(x[0, 0][0::2])
print(x[0, 0][::-1])
print(x[0, 0][::-2])
print(x[0, 0][2::-2])

x = np.random.randint(0, 10, (3, 4))

print(x)
print(x[:2, ::2])
print(x[::-1, ::-1])
print(x.T)

print('\n')

print(x)
x[0] = x[0] * 2
print(x)

print(x[0])
print(x[:, 0])
print(x[0, :])
