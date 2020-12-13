# jmtrace



## 简介

jmtrace是一款通过程序插桩方式hack Java程序从而允许用户打印对共享内存访问信息的工具



## 使用示例

在为`jmtrace-jar-with-dependencies.jar`所在目录下运行如下命令：

```bash
java -javaagent:./jmtrace-jar-with-dependencies.jar  -jar <the-jar-file-to-hack>
```



## 编译依赖

本程序采用java语言编写，采用maven进行项目管理，依赖于asm 9..0进行字节码的改写。

- java 1.8
- maven 3.6.3
- asm 9.0

由于采用maven进行管理，因此直接下载后即可使用maven打包



## 打包说明

使用maven进行打包，在`pom.xml`所在文件夹下运行如下命令即可打包

```bash
mvn package
```

生成的jar包位于`./target/`目录下，名为`jmtrace-jar-with-dependencies.jar`。

**注意**不是`jmtrace.jar`！！！要带有`with-dependencies`字段的才有将asm打包进去。



