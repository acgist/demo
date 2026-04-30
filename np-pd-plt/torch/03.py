import torch

x = torch.arange(0, 12)
print(x)
for index in range(len(x)):
    if index >= 12 - 5:
        break
    print(index)
    print(x[index + 5:index + 5 + 1])
    print(x[index    :index + 5 + 0])