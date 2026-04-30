from ollama import Client

client = Client(
    host = 'http://192.168.8.228:11434'
)

response = client.chat(
    model    = 'deepseek-r1',
    messages = [{
        'role'   : 'user',
        'content': '哥们你是谁',
    }]
)

print(response)
