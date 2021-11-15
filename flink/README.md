# Flink

做一个简单的实时故障统计

清洗、分流、保存数据
按照时段统计故障地图和曲线图

单机部署

## 内存

jobmanager.memory.process.size
taskmanager.memory.process.size

## 版本

flink-1.14.0

## 单机部署

./start-cluster.sh

> http://localhost:8081/

## 提交任务

flink run -c com.acgist.Main acgist.jar

## 查询任务

flink list

## 停止任务

flink cancel ${id}

## 关闭

stop-cluster.sh

## MVN创建项目

mvn archetype:generate "-DgroupId=com.acgist" "-DarchetypeGroupId=org.apache.flink" "-DarchetypeVersion=1.14.0" "-DarchetypeArtifactId=flink-quickstart-java"

## 处理过程

Source Operator -> Transformation Operator -> Sink Operator

## 时间

Event Time -> Ingestion Time -> Process Time