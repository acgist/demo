# 计划

*√=完成、○-进行中、#-未开始、?-待定、--忽略*

## 主要功能

|任务|是否实现|当前状态|详细功能|
|:--|:--:|:--:|:--|
|自然语言对话|○|○|自然语言对话|
|诗词内容生成|○|○|图片转为诗词、标签转为诗词|
|诗词风格迁移|-|-|-|
|图片内容生成|○|○|诗词转为图片、标签转为图片|
|图片风格迁移|○|○|图片转为图片|
|音频内容生成|?|?|?|
|音频风格迁移|?|?|?|
|视频内容生成|?|?|?|
|视频风格迁移|?|?|?|

## 详细功能

|任务|当前状态|
|:--|:--:|
|图片标记|○|
|诗词标记|○|
|文档标记|○|
|自然语言对话|○|
|图片转为诗词|○|
|标签转为诗词|○|
|诗词转为图片|○|
|标签转为图片|○|
|图片转为图片|○|

#### 图片标记

为每张图片打上文本标记，提供给模型微调。

#### 诗词标记

为每首诗词打上文本标记，提供给模型微调。诗词标记主要使用分词。

#### 文档标记

解析常见文档，根据语义分段。

#### 自然语言对话

智能对话

#### 图片转为诗词

图片提取标签然后转为诗词

#### 标签转为诗词

#### 诗词转为图片

诗词提取标签然后转为图片

#### 标签转为图片

#### 图片转为图片

#### TODO

* 本地文档
* 搜索引擎
* 更好的分段效果
* GITHUB代码搜索

## 代码规范

* 宏定义开头必须使用`LFR_`
* 尽量使用智能指针，避免使用传统指针。
* 类和结构体直接使用命名空间`lifuren`
* 头文件宏定义`LFR_HEADER_module_path_filename_HPP`
* 全局方法必须使用命名空间`lifuren::module | lifuren::filename`
