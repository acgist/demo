import gradio as gr

def echo(audio):
    # 直接返回音频，前端会自动显示波形
    return audio

# inputs 和 outputs 使用 gr.Audio() 即可
demo = gr.Interface(
    fn=echo,
    inputs=gr.Audio(label="上传或录音", type="numpy"),
    outputs=None
    # 或者简写为 outputs="audio"
)
demo.launch()