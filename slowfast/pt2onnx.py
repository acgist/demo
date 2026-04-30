import onnx
import torch

from utils import frame_slow_channel, frame_fast_channel

model = torch.load("model.pth", weights_only = False)
model.cpu()
model.eval()

input_names  = ["slow", "fast"]
output_names = ["out"]

slow = torch.rand(1, frame_slow_channel, 256, 256)
fast = torch.rand(1, frame_fast_channel, 256, 256)

# https://docs.pytorch.org/docs/stable/onnx_torchscript.html#torch.onnx.export
torch.onnx.export(
    model,
    (slow, fast),
    "model.onnx",
    input_names  = input_names,
    output_names = output_names,
    verbose = "True"
)
