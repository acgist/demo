# Flink

��һ���򵥵�ʵʱ����ͳ��

��ϴ����������������
����ʱ��ͳ�ƹ��ϵ�ͼ������ͼ

��������

## �ڴ�

jobmanager.memory.process.size
taskmanager.memory.process.size

## �汾

flink-1.14.0

## ��������

./start-cluster.sh

> http://localhost:8081/

## �ύ����

flink run -c com.acgist.Main acgist.jar

## ��ѯ����

flink list

## ֹͣ����

flink cancel ${id}

## �ر�

stop-cluster.sh

## MVN������Ŀ

mvn archetype:generate "-DgroupId=com.acgist" "-DartifactId=demo" "-DarchetypeArtifactId=maven-archetype-quickstart"
