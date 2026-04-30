import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

data = pd.read_csv("data.csv")
print(data.shape)
heights = np.array(data["height(cm)"])
print(heights)
print(heights.max())
print(heights.min())
print(heights.mean())
plt.hist(heights)
plt.ylabel("number")
plt.xlabel("height(cm)")
plt.show()
