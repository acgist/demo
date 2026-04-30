import torch
import torchaudio
import matplotlib.pyplot as plt

def frequency_waveform(file, target_freqs = [300, 1200, 2400]):
    pcm, sr = torchaudio.load(file)
    pcm     = pcm[:, 48000:48000 * 2]
    if pcm.dim() > 1:
        pcm = pcm.mean(dim = 0)
    n_fft      = 400
    hop_length = 80
    window = torch.hann_window(n_fft)
    spec   = torch.stft(
        pcm,
        n_fft          = n_fft,
        hop_length     = hop_length,
        window         = window,
        return_complex = True
    )
    freqs       = torch.fft.fftfreq(n_fft, 1 / sr)[:n_fft // 2]
    time_frames = torch.arange(0, spec.shape[1]) * hop_length / sr
    plt.figure(figsize = (15, 10))
    # 原始波
    plt.subplot(len(target_freqs) + 2, 1, 1)
    plt.plot(pcm.numpy())
    plt.title("Original Waveform")
    # 频谱图
    plt.subplot(len(target_freqs) + 2, 1, 2)
    plt.imshow(
        20 * torch.log10(torch.abs(spec) + 1e-6), 
        aspect = "auto",
        origin = "lower",
        extent = [0, time_frames[-1], 0, freqs[-1]]
    )
    plt.colorbar(label = "dB")
    plt.ylabel("Frequency (Hz)")
    plt.title("Spectrogram")
    # 各频率的时域波形
    # print(freqs)
    for i, freq in enumerate(target_freqs):
        idx = torch.argmin(torch.abs(freqs - freq))
        x = spec * 0
        # x[10:40, :] += spec[10:40, :]
        x = x + spec[idx:idx + 1]
        component = torch.istft(
            x,
            n_fft      = n_fft,
            hop_length = hop_length,
            window     = window,
            length     = pcm.shape[0]
        )
        # torchaudio.save(f"D:/tmp/wav/{freq}.wav", component.unsqueeze(0), sr, channels_first = True)
        plt.subplot(len(target_freqs) + 2, 1, 3 + i)
        # plt.subplot(len(target_freqs) + 2, 1, 3)
        plt.plot(component.numpy().real)
        plt.title(f"{freq}Hz Component Waveform")
    plt.tight_layout()
    plt.show()

if __name__ == "__main__":
    # list(range(0, 48000, 100))
    frequency_waveform("D:/tmp/dzht.wav")
