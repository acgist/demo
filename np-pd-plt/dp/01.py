import numpy  as np
import pandas as pd

data = pd.Series([0.25, 0.5, 0.75, 1.0])
print(data)
print(type(data))
print(type(data.index))
print(type(data.values))
map = pd.Series({
    'q': 1,
    "b": 2
})
print(map)
x = np.random.random((2, 3))
print(x)
print(pd.DataFrame(x, columns = ["a", "b", "v"], index = ["x", "y"]))
x = pd.Series({ "x1": 1, "x2": 2, 'x3': 3 })
y = pd.Series({ "x1": 1, "x2": 2 })
xy = pd.DataFrame({'x': x, "y": y})
print(xy)
print(pd.DataFrame([{'x': i, 'y': i * 2} for i in range(4)]))
print(xy.T)