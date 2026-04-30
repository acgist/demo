import array
import numpy as np

L = list(range(10))
print(array.array('i', L))
print(array.array('d', L))
print(np.array(L))
print(np.array([range(i, i + 4) for i in (2, 4, 6)]))