import torch
import matplotlib.pyplot as plt

T = 1000
tau = 4
time = torch.arange(1, T + 1, dtype=torch.float32)
x = torch.sin(0.01 * time) + torch.normal(0, 0.2, (T,))
features = torch.zeros((T - tau, tau))
# for t in range(T - tau):
#     features[t] = x[t:t + tau]
for i in range(tau):
    features[:, i] = x[i:T - tau + i]
labels = x[tau:].reshape((-1, 1))

print(x.shape)
print(features.shape)
print(labels.shape)

plt.plot(x)
plt.show()
plt.show()
for i in range(4):
    plt.plot(features[:, i], label=f'Feature {i}')
plt.legend()
plt.show()
plt.plot(labels[:, 0])
plt.show()
