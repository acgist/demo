import numpy as np
import matplotlib.pyplot as plt

# np.random.seed(42)
x      = np.random.randn(100)
bins   = np.linspace(-5, 5, 20)
counts = np.zeros_like(bins)
print(x)
print(bins)
print(counts)
i = np.searchsorted(bins, x)
print(i)
np.add.at(counts, i, 1)
print(counts)
plt.step(bins, counts)
plt.show()
plt.cla()
plt.hist(x, bins)
plt.show()