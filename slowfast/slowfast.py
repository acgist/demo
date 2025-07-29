import torch
import torch.nn as nn
import torch.nn.functional as F

class SlowLayer(nn.Module):
    def __init__(self, in_dim, out, pool_size):
        super(SlowLayer, self).__init__()
        # pool
        self.pool = nn.MaxPool2d(pool_size)
        # norm
        self.slow_norm = nn.BatchNorm2d(in_dim)
        self.fast_norm = nn.BatchNorm2d(in_dim + 12)
        # _1
        self.slow_left_conv_1_1  = nn.Conv2d(in_dim * 2 + 12, out, 3, padding = 1)
        self.slow_right_conv_1_1 = nn.Conv2d(in_dim * 2 + 12, out, 3, padding = 1)
        self.slow_right_conv_1_2 = nn.Conv2d(out, out, 3, padding = 1)
        self.slow_right_conv_1_3 = nn.Conv2d(out, out, 3, padding = 1)
        # _2
        self.slow_right_conv_2_1 = nn.Conv2d(out, out, 3, padding = 1)
        self.slow_right_conv_2_2 = nn.Conv2d(out, out, 3, padding = 1)
        self.slow_right_conv_2_3 = nn.Conv2d(out, out, 3, padding = 1)
        # _3
        self.slow_right_conv_3_1 = nn.Conv2d(out, out, 3, padding = 1)
        self.slow_right_conv_3_2 = nn.Conv2d(out, out, 3, padding = 1)
        self.slow_right_conv_3_3 = nn.Conv2d(out, out, 3, padding = 1)
    def forward(self, slow, fast):
        # fast
        fast = self.pool(fast)
        fast = F.silu(self.fast_norm(fast))
        # slow
        slow = self.pool(slow)
        slow = F.silu(self.slow_norm(slow))
        # join
        slow = torch.cat([ slow, fast ], dim = 1)
        # _1
        left  = self.slow_left_conv_1_1(slow)
        right = F.silu(self.slow_right_conv_1_1(slow))
        right = F.silu(self.slow_right_conv_1_2(right))
        right = F.silu(self.slow_right_conv_1_3(right))
        slow  = F.silu(right + left)
        # _2
        right = F.silu(self.slow_right_conv_2_1(slow))
        right = F.silu(self.slow_right_conv_2_2(right))
        right = F.silu(self.slow_right_conv_2_3(right))
        slow  = F.silu(right + slow)
        # _3
        right = F.silu(self.slow_right_conv_3_1(slow))
        right = F.silu(self.slow_right_conv_3_2(right))
        right = F.silu(self.slow_right_conv_3_3(right))
        return right + slow

class FastLayer(nn.Module):
    def __init__(self, in_dim, out, pool_size):
        super(FastLayer, self).__init__()
        # pool
        self.pool = nn.MaxPool2d(pool_size)
        # norm
        self.norm = nn.BatchNorm2d(in_dim)
        # _1
        self.fast_left_conv_1_1  = nn.Conv2d(in_dim, out, 3, padding = 1)
        self.fast_right_conv_1_1 = nn.Conv2d(in_dim, out, 3, padding = 1)
        self.fast_right_conv_1_2 = nn.Conv2d(out, out, 3, padding = 1)
        self.fast_right_conv_1_3 = nn.Conv2d(out, out, 3, padding = 1)
        # _2
        self.fast_right_conv_2_1 = nn.Conv2d(out, out, 3, padding = 1)
        self.fast_right_conv_2_2 = nn.Conv2d(out, out, 3, padding = 1)
        self.fast_right_conv_2_3 = nn.Conv2d(out, out, 3, padding = 1)
        # _3
        self.fast_right_conv_3_1 = nn.Conv2d(out, out, 3, padding = 1)
        self.fast_right_conv_3_2 = nn.Conv2d(out, out, 3, padding = 1)
        self.fast_right_conv_3_3 = nn.Conv2d(out, out, 3, padding = 1)
    def forward(self, fast):
        # fast
        fast = self.pool(fast)
        fast = F.silu(self.norm(fast))
        # _1
        left  = self.fast_left_conv_1_1(fast)
        right = F.silu(self.fast_right_conv_1_1(fast))
        right = F.silu(self.fast_right_conv_1_2(right))
        right = F.silu(self.fast_right_conv_1_3(right))
        fast  = F.silu(right + left)
        # _2
        right = F.silu(self.fast_right_conv_2_1(fast))
        right = F.silu(self.fast_right_conv_2_2(right))
        right = F.silu(self.fast_right_conv_2_3(right))
        fast  = F.silu(right + fast)
        # _3
        right = F.silu(self.fast_right_conv_3_1(fast))
        right = F.silu(self.fast_right_conv_3_2(right))
        right = F.silu(self.fast_right_conv_3_3(right))
        return right + fast

class SlowFastNet(nn.Module):
    def __init__(self, out, slow_dim, fast_dim):
        super(SlowFastNet, self).__init__()
        self.slow_head_conv = nn.Conv2d(slow_dim, 20, 3, padding = 1)
        self.fast_head_conv = nn.Conv2d(fast_dim, 32, 3, padding = 1)
        # 256 / 2 / 2 / 2 / 2 / 2 = 8
        self.slow_1 = SlowLayer(20, 28, 2)
        self.slow_2 = SlowLayer(28, 36, 2)
        self.slow_3 = SlowLayer(36, 44, 2)
        self.slow_4 = SlowLayer(44, 52, 2)
        self.slow_5 = SlowLayer(52, 60, 2)
        self.fast_1 = FastLayer(32, 40, 2)
        self.fast_2 = FastLayer(40, 48, 2)
        self.fast_3 = FastLayer(48, 56, 2)
        self.fast_4 = FastLayer(56, 64, 2)
        self.fast_5 = FastLayer(64, 72, 2)
        self.pool = nn.AvgPool2d(2)
        self.fc_1 = nn.Linear(60 * 4 * 4 + 72 * 4 * 4, 256)
        self.fc_2 = nn.Linear(256, out)
    def forward(self, slow, fast):
        # head
        slow = self.slow_head_conv(slow)
        fast = self.fast_head_conv(fast)
        # slow_fast
        slow = F.silu(self.slow_1(slow, fast))
        fast = F.silu(self.fast_1(fast))
        slow = F.silu(self.slow_2(slow, fast))
        fast = F.silu(self.fast_2(fast))
        slow = F.silu(self.slow_3(slow, fast))
        fast = F.silu(self.fast_3(fast))
        slow = F.silu(self.slow_4(slow, fast))
        fast = F.silu(self.fast_4(fast))
        slow = self.slow_5(slow, fast)
        fast = self.fast_5(fast)
        # out
        slow = F.silu(self.pool(slow))
        fast = F.silu(self.pool(fast))
        out  = torch.cat([ slow, fast ], dim = 1)
        out  = torch.flatten(out, start_dim = 1)
        out  = F.silu(self.fc_1(out))
        out  = self.fc_2(out)
        return out
