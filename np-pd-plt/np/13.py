import numpy as np

x = np.dtype({'names': ('name', 'age', 'weight'), "formats": ((np.str_, 10), int, np.float32)})
print(x)
x = np.dtype([('name', 'S10'), ('age', 'i4'), ('weight', 'f8')])
print(x)