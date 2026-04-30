import signal

def graceful_exit(signum, frame):
    print("捕获到Ctrl+C！")

signal.signal(signal.SIGINT, graceful_exit)

while True:
    message = input("请输入：\n")
    print(message)
