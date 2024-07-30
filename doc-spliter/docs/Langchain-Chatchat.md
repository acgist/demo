# Langchain-Chatchat

## 环境

```
sudo apt install build-essential
sudo apt install python3 python3-pip
sudo apt install nvidia-cuda-toolkit

vim ~/.pip/pip.conf

---
[global]
index-url=https://pypi.tuna.tsinghua.edu.cn/simple
---
```

## conda

https://mirrors.tuna.tsinghua.edu.cn/help/anaconda/

```
wget https://mirrors.tuna.tsinghua.edu.cn/anaconda/archive/Anaconda3-2024.06-1-Linux-x86_64.sh

vim ~/.condarc
---
channels:
  - defaults
show_channel_urls: true
default_channels:
  - https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/r
  - https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/main
  - https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/msys2
custom_channels:
  msys2: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud
  menpo: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud
  pytorch: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud
  bioconda: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud
  simpleitk: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud
  conda-forge: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud
  pytorch-lts: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud
  deepmodeling: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud
---

conda clean -i

# 更新conda
conda update conda
conda update anaconda
conda update --all

# 安装更新
conda update  xxxx
conda install xxxx

## 创建虚拟环境
conda create --name 环境名称 python=3.11
conda create --name 环境名称 python=3.11
## 激活某个环境
conda activate 环境名称
## 退出当前环境
conda deactivate
## 删除某个环境
conda remove --name 环境名称 --all
## 复制某个环境
conda create --name 环境名称 --clone 环境名称
## 列出所有的环境
conda env list
```

## Xinference

https://github.com/abetlen/llama-cpp-python
https://inference.readthedocs.io/zh-cn/latest/index.html
https://inference.readthedocs.io/zh-cn/latest/getting_started/using_xinference.html

```
mkdir -p /data/xinference
conda create --name Xinference python=3.11
conda activate Xinference
pip install "xinference[vllm]"
# pip install llama-cpp-python
CMAKE_ARGS="-DLLAVA_BUILD=OFF" pip install llama-cpp-python --verbose
XINFERENCE_HOME=/data/xinference xinference-local --host 0.0.0.0 --port 9997
conda deactivate
```

## ollama

https://ollama.com/download/linux

```
mkdir -p /data/ollama
conda create --name ollama python=3.11
conda activate ollama
curl -fsSL https://ollama.com/install.sh | sh
# ollama rm glm4
# ollama pull glm4
ollama pull quentinz/bge-large-zh-v1.5
ollama run glm4
ollama list
ollama show glm4
conda deactivate
```

## langchain-chatchat

https://github.com/chatchat-space/Langchain-Chatchat

```
mkdir -p /data/lccc
conda create --name lccc python=3.11
conda activate lccc
pip install "langchain-chatchat[all]" -U
vim ~/.bashrc
---
export CHATCHAT_ROOT=/data/lccc
---
. ~/.bashrc
chatchat init
chatchat kb -r
chatchat start -a
conda deactivate
```
