import numpy as np
import time

np.random.seed(0)
def compute_reciprocals(values):
    output = np.empty(len(values))
    for i in range(len(values)):
        output[i] = 1.0 / values[i]
    return output
values = np.random.randint(1, 100, size=1000000)
a = time.time()
# java 8ms
# c++ O3 2ms
# c++ O0 10ms
# compute_reciprocals(values)
values = 1.0 / values
z = time.time()
print(z - a)