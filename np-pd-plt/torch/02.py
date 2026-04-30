import torch

a = torch.rand([2, 2, 3])
b = torch.rand([2, 3])
print(a)
print(b)
print(a + b)
a = torch.rand([10, 5, 3, 256, 128]);
b = torch.rand([10, 1, 3, 256, 128]);
print(a + b)