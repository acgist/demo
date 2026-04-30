import numpy as np
import matplotlib.pyplot as plt

mean = [0, 0]
conv = [[1, 2], [2, 5]]
rand = np.random.RandomState(42)
x = rand.multivariate_normal(mean, conv, 100)
print(x.shape)

plt.scatter(x[:, 0], x[:, 1])

i = np.random.choice(x.shape[0], 20, replace = False)
s = x[i]
plt.scatter(s[:, 0], s[:, 1], facecolor = 'none', edgecolors = 'b', s = 200)

plt.show()