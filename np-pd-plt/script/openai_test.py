from openai import OpenAI

client = OpenAI(api_key = "-", base_url = "https://api.deepseek.com")

response = client.chat.completions.create(
    model    = "deepseek-chat",
    stream   = False,
    messages = [
        {"role": "system", "content": "你是一个牛人"},
        {"role": "user",   "content": "牛的"       },
    ]
)

print(response.choices[0].message.content)
