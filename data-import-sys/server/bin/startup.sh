#!/bin/bash

#参考 http://docs.oracle.com/javase/8/docs/technotes/tools/unix/java.html

# 在非交互模式下开启alias: shopt -s  expand_aliases
# 如果环境中默认java版本低于8 可以通过此命令选择java8执行: alias java="java8地址"


#DEBUG
	#java -server -jar -Dspring.profiles.active=qa *.jar -Xms256M -XmX512M -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8989

#qa
	java -server -jar -Dspring.profiles.active=qa  *.jar -Xms256M -XmX512M  -XX:+UseG1GC -XX:HeapDumpPath=/data/logs/hprof

#phantom
	#java -server -jar -Dspring.profiles.active=phantom *.jar -Xms256M -XmX512M  -XX:+UseG1GC -XX:HeapDumpPath=/data/logs/shenkansen/hprof

#production
	#java -server -jar -Dspring.profiles.active=production *.jar -Xms256M -XmX512M -XX:+UseG1GC -XX:HeapDumpPath=/data/logs/shenkansen/hprof
