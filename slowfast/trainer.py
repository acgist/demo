import os
import torch
import torch.nn as nn

from tqdm import tqdm
from utils import loadVideo, frame_size_slow, frame_size_fast, frame_slow_channel, frame_fast_channel
from slowfast import SlowFastNet

print("""

众鸟高飞尽，孤云独去闲。
相看两不厌，只有敬亭山。

""")

num_epochs  = 128 # 训练总的轮次
batch_size  =  50 # 训练批次数量
train_ratio = .90 # 训练集总占比

label_mapping = {
    "smoke"  : 0,
    "nosmoke": 1,
}

label_size = len(label_mapping)

class VideoFrameDataset(torch.utils.data.Dataset):
    def __init__(self, features, labels):
        self.features = features
        self.labels   = labels

    def __len__(self):
        return len(self.features)

    def __getitem__(self, index):
        feature = self.features[index]
        label   = self.labels  [index]
        return feature, label

def loadVideoFrameDataset(path):
    files = []
    for type_name in os.listdir(path):
        if label_mapping.get(type_name) == None:
            continue
        type_file = os.path.join(path, type_name)
        if os.path.isfile(type_file):
            continue
        for video_name in os.listdir(type_file):
            video_file = os.path.join(path, type_name, video_name)
            if not os.path.isfile(video_file):
                continue
            files.append((video_file, video_name, type_name))
    features = []
    labels   = []
    process = tqdm(files)
    for video_file, video_name, type_name in process:
        process.set_postfix(file = "{}".format(video_name[-16:]))
        for flip in range(2): # 原图 水平翻转
            features_slow, features_fast = loadVideo(video_file, flip == 1)
            if len(features_slow) != frame_size_slow or len(features_fast) != frame_size_fast:
                continue
            feature_slow = torch.cat(features_slow)
            feature_fast = torch.cat(features_fast)
            label        = torch.zeros(len(label_mapping)).float()
            label[label_mapping[type_name]] = 1.0
            features.append((feature_slow, feature_fast))
            labels  .append(label)
    return VideoFrameDataset(features, labels)

# dataset = loadVideoFrameDataset("/data/aliang/slowfast/video_dev")
dataset = loadVideoFrameDataset("/data/aliang/slowfast/video_prd")

train_size = int(train_ratio * len(dataset))
val_size   = len(dataset) - train_size

train_dataset, val_dataset = torch.utils.data.random_split(dataset, [ train_size, val_size ])

train_loader = torch.utils.data.DataLoader(train_dataset, batch_size = batch_size, shuffle = True, num_workers = 0)
val_loader   = torch.utils.data.DataLoader(val_dataset,   batch_size = batch_size, shuffle = True, num_workers = 0)

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

model = SlowFastNet(label_size, frame_slow_channel, frame_fast_channel)
model.to(device)

criterion = nn.CrossEntropyLoss()
optimizer = torch.optim.AdamW(model.parameters(), lr = 0.001) # eps = 1e-8, weight_decay = 1e-2
scheduler = torch.optim.lr_scheduler.StepLR(optimizer, step_size = 10, gamma = 0.9)

for epoch in range(num_epochs):
    loss_sum   = 0.0
    loss_count = 0
    accu_sum   = 0
    accu_count = 0
    # 训练集
    model.train()
    process = tqdm(train_loader, total = len(train_loader), desc = f"Epoch [{epoch + 1} / { num_epochs }]")
    for features, labels in process:
        slow   = features[0].to(device)
        fast   = features[1].to(device)
        labels = labels.to(device)
        optimizer.zero_grad()
        pred = model(slow, fast)
        loss = criterion(pred, labels)
        loss.backward()
        optimizer.step()
        with torch.no_grad():
            loss_sum   += loss.item()
            loss_count += 1
            pred_idx = torch.argmax(pred,   1)
            true_idx = torch.argmax(labels, 1)
            accu_sum   += (pred_idx == true_idx).sum().item()
            accu_count += labels.size(0)
            process.set_postfix(loss = "{:.2f}".format(loss_sum / loss_count), accu = "{:.2f}%".format(100 * accu_sum / accu_count))
    scheduler.step()
    # 验证集
    model.eval()
    if epoch % 20 == 0:
        torch.save(model, "checkpoint/model_cpk_{}.pth".format(epoch))
    with torch.no_grad():
        accu_sum   = 0
        accu_count = 0
        # 混淆矩阵
        confusion_matrix = torch.zeros(label_size, label_size)
        for features, labels in val_loader:
            slow   = features[0].to(device)
            fast   = features[1].to(device)
            labels = labels.to(device)
            pred = model(slow, fast)
            pred_idx = torch.argmax(pred,   1)
            true_idx = torch.argmax(labels, 1)
            accu_sum   += (pred_idx == true_idx).sum().item()
            accu_count += labels.size(0)
            for i, j in zip(pred_idx, true_idx):
                confusion_matrix[i, j] += 1
        print("Accuracy: {} / {} = {:.2f}%".format(accu_sum, accu_count, 100 * accu_sum / accu_count))
        print(confusion_matrix)

model = model.cpu()
torch.save(model, "model.pth")
model = torch.jit.trace(model, (torch.rand(1, frame_slow_channel, 256, 256), torch.rand(1, frame_fast_channel, 256, 256)))
model.save("model.pt")

print("""

贵逼人来不自由，龙骧凤翥势难收。
满堂花醉三千客，一剑霜寒十四州。
鼓角揭天嘉气冷，风涛动地海山秋。
东南永作金天柱，谁羡当时万户侯。

""")
