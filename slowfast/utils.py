import cv2
import torch
import numpy as np

# 间隔帧数
frame_skip_slow = 13 # 23 # 13
frame_skip_fast =  7 # 17 #  7
# 采样帧数
frame_size_slow = 4
frame_size_fast = frame_size_slow * 2
# 采样通道
frame_slow_channel = 3 * frame_size_slow
frame_fast_channel = 3 * frame_size_fast

def loadVideo(file, flip = False):
    index = 0
    features_slow = []
    features_fast = []
    capture = cv2.VideoCapture(file)
    while True:
        # 跳过首帧
        index += 1
        ret, frame = capture.read()
        if not ret:
            break
        if index % frame_skip_slow != 0 and index % frame_skip_fast != 0:
            continue
        # 处理图片
        h = frame.shape[0]
        w = frame.shape[1]
        m = max(h, w)
        mat = np.zeros((m, m, 3), dtype = np.uint8)
        mat[0:h, 0:w] = frame
        mat = cv2.resize(mat, (256, 256))
        if flip:
            mat = cv2.flip(mat, 1)
        # cv2.imwrite("mat.jpg",   mat)
        # cv2.imwrite("frame.jpg", frame)
        rgb = cv2.cvtColor(mat, cv2.COLOR_BGR2RGB)
        # 处理数据
        feature = torch.tensor(rgb).float() / 255.0 * 2.0 - 1.0
        feature = feature.permute(2, 0, 1)
        if index % frame_skip_slow == 0 and len(features_slow) < frame_size_slow:
            features_slow.append(feature)
        if index % frame_skip_fast == 0 and len(features_fast) < frame_size_fast:
            features_fast.append(feature)
        if len(features_slow) >= frame_size_slow and len(features_fast) >= frame_size_fast:
            break
    return (features_slow, features_fast)
