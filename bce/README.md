# BCE

> 注意：当前默认使用异或算法加密，如果需要更高安全级别可以使用`AES`/`RSA`或者其他算法。

## 加密文件

```
java -Dencrypt=true -Dsrc=bce-1.0.0.jar -Ddst=dst-1.0.0.jar -jar bce-1.0.0.jar
```

## 启动项目

```
# 设置：JAVA_HOME
cd ${project}/decrypt
make

# 启动
export LD_LIBRARY_PATH=${project}/decrypt
java -agentlib:linux -Ddecrypt=true -jar dst-1.0.0.jar
```

## SpringBoot

```
public static void main(String[] args) {
    if(!Encrypt.encryptMain(args)) {
        return;
    }
    final SpringApplication application = new SpringApplicationBuilder(Application.class).build();
    if(Encrypt.decrypt) {
        Encrypt.customResourceLoader(application);
    }
    application.run(args);
}
```