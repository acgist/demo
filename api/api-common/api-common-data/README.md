# 数据库模块
maven添加：
```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```
添加扫描：
```
@EntityScan("com.api.data.**.entity")
@ComponentScan({"com.api.data"})
@EnableJpaRepositories(basePackages = "com.api.data.**.repository", repositoryBaseClass = BaseExtendRepositoryImpl.class)
@EnableTransactionManagement
```
