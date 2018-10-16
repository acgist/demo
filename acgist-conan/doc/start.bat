@echo off
for /r ./ %%i in (
	acgist-conan*.jar 
) do (
	if exist %%i ( java -jar --add-modules jdk.incubator.httpclient %%i )
)