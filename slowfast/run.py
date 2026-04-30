import torch

from utils import loadVideo

features_slow, features_fast = loadVideo("/data/aliang/slowfast/smoke1.avi")

# JIT
model = torch.jit.load("model.pt")
model.cuda()
model.eval()

# 原始模型
# model = torch.load("model.pth", weights_only = False)
# model.cuda()
# model.eval()

slow = torch.unsqueeze(torch.cat(features_slow), 0).to("cuda")
fast = torch.unsqueeze(torch.cat(features_fast), 0).to("cuda")

ret = model(slow, fast)

print(ret)
print(torch.max(ret, 1))
print(torch.softmax(ret, 1))
