# jmtrace



## 简介

jmtrace是一款通过程序插桩方式hack Java程序从而允许用户打印对共享内存访问信息的工具

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


## 运行说明

在为`jmtrace-jar-with-dependencies.jar`所在目录下运行如下命令：

```bash
java -javaagent:./jmtrace-jar-with-dependencies.jar  -jar <the-jar-file-to-hack>
```

## 使用示例

在主目录下提供了`HelloWorld.jar`以供测试。
### 1. 编译打包
首先，在当前目录运行
```bash
mvn package
```
即可对工具进行打包，当看到`BUILD SUCCESS`时，表示打包成功。
```bash
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  2.408 s
[INFO] Finished at: 2020-12-13T21:12:13+08:00
[INFO] ------------------------------------------------------------------------
```
此时在`./target/`目录下将生成两个jar包，分别是`jmtrace.jar`和`jmtrace-jar-with-dependencies.jar`。
其中`jmtrace-jar-with-dependencies.jar`才是真正能运行的jar包。
### 2. 运行测试
对于`jmtrace-jar-with-dependencies.jar`的使用方法是：
```bash
java -javaagent:./jmtrace-jar-with-dependencies.jar  -jar <the-jar-file-to-hack>
```
本地提供了用于测试的`HelloWorld.jar`包，以及用于测试的脚本`test.sh`。

运行测试脚本即可得到形如样例输出的结果。
若提示权限不够，可以直接运行命令
```bash
java -javaagent:./target/jmtrace-jar-with-dependencies.jar  -jar ./HelloWorld.jar
```
或者改变用户权限
```bash
chmod +x ./test.sh
```
随后运行脚本即可
### 3. 样例输出
```bash
$ ./test.sh
Hello World!
arr[0] is 10
arr[1] is 9
arr[2] is 8
arr[3] is 7
arr[4] is 6
arr[5] is 5
arr[6] is 4
arr[7] is 3
arr[8] is 2
arr[9] is 1
R 9 0000000062487aef njuics.test.Ohter.strFiled
W 9 0000000062487aef njuics.test.Ohter.strFiled
R 9 0000000062487aef njuics.test.Ohter.boolFiled
W 9 0000000062487aef njuics.test.Ohter.boolFiled
R 9 0000000062487aef njuics.test.Ohter.strFiled
W 9 000000007b1fba42 njuics.test.Ohter.strFiled
R 9 000000007b1fba42 njuics.test.Ohter.boolFiled
W 9 000000007b1fba42 njuics.test.Ohter.boolFiled
arr[0] is 10
arr[1] is 9
arr[2] is 8
arr[3] is 7
arr[4] is 6
arr[5] is 5
arr[6] is 4
arr[7] is 3
arr[8] is 2
arr[9] is 1
R 9 000000005e6f2689 njuics.test.HelloWorld.staticFiled
Before, hw.staticFiled is 0
R 9 00000000075d6dc5 njuics.test.HelloWorld.strFiled
Before, hw.strFiled is local
R 9 00000000075d6dc5 njuics.test.HelloWorld.strFiled
R 9 00000000075d6dc5 njuics.test.HelloWorld.intFiled
Before, hw.intFiled is 0
R 9 000000003f175950 njuics.test.HelloWorld.arr
R 9 0000000039dc295a I[0]
arr[0] is 10
R 9 000000003f175950 njuics.test.HelloWorld.arr
R 9 000000001e200b86 I[0]
R 9 0000000039dc295a I[0]
R 9 000000003f175950 njuics.test.HelloWorld.arr
R 9 0000000039dc295b I[1]
arr[1] is 9
R 9 000000003f175950 njuics.test.HelloWorld.arr
R 9 000000001e200b87 I[1]
R 9 0000000039dc295b I[1]
R 9 000000003f175950 njuics.test.HelloWorld.arr
R 9 0000000039dc295c I[2]
arr[2] is 8
R 9 000000003f175950 njuics.test.HelloWorld.arr
R 9 000000001e200b88 I[2]
R 9 0000000039dc295c I[2]
R 9 000000003f175950 njuics.test.HelloWorld.arr
R 9 0000000039dc295d I[3]
arr[3] is 7
R 9 000000003f175950 njuics.test.HelloWorld.arr
R 9 000000001e200b89 I[3]
R 9 0000000039dc295d I[3]
R 9 000000003f175950 njuics.test.HelloWorld.arr
R 9 0000000039dc295e I[4]
arr[4] is 6
R 9 000000003f175950 njuics.test.HelloWorld.arr
R 9 000000001e200b8a I[4]
R 9 0000000039dc295e I[4]
R 9 000000003f175950 njuics.test.HelloWorld.arr
R 9 0000000039dc295f I[5]
arr[5] is 5
R 9 000000003f175950 njuics.test.HelloWorld.arr
R 9 000000001e200b8b I[5]
R 9 0000000039dc295f I[5]
R 9 000000003f175950 njuics.test.HelloWorld.arr
R 9 0000000039dc2960 I[6]
arr[6] is 4
R 9 000000003f175950 njuics.test.HelloWorld.arr
R 9 000000001e200b8c I[6]
R 9 0000000039dc2960 I[6]
R 9 000000003f175950 njuics.test.HelloWorld.arr
R 9 0000000039dc2961 I[7]
arr[7] is 3
R 9 000000003f175950 njuics.test.HelloWorld.arr
R 9 000000001e200b8d I[7]
R 9 0000000039dc2961 I[7]
R 9 000000003f175950 njuics.test.HelloWorld.arr
R 9 0000000039dc2962 I[8]
arr[8] is 2
R 9 000000003f175950 njuics.test.HelloWorld.arr
R 9 000000001e200b8e I[8]
R 9 0000000039dc2962 I[8]
R 9 000000003f175950 njuics.test.HelloWorld.arr
R 9 0000000039dc2963 I[9]
arr[9] is 1
R 9 000000003f175950 njuics.test.HelloWorld.arr
R 9 000000001e200b8f I[9]
R 9 0000000039dc2963 I[9]
W 9 00000000727f7ec2 njuics.test.HelloWorld.staticFiled
W 9 00000000075d6dc5 njuics.test.HelloWorld.strFiled
W 9 00000000075d6dc5 njuics.test.HelloWorld.intFiled
R 9 0000000077a77404 njuics.test.HelloWorld.staticFiled
After, hw.staticFiled is 1
R 9 00000000075d6dc5 njuics.test.HelloWorld.strFiled
After, hw.strFiled is dynamic
R 9 00000000075d6dc5 njuics.test.HelloWorld.intFiled
After, hw.intFiled is 1
R 9 000000003f175950 njuics.test.HelloWorld.arr
R 9 0000000039dc295a I[0]
arr[0] is 0
R 9 000000003f175950 njuics.test.HelloWorld.arr
R 9 0000000039dc295b I[1]
arr[1] is 1
R 9 000000003f175950 njuics.test.HelloWorld.arr
R 9 0000000039dc295c I[2]
arr[2] is 2
R 9 000000003f175950 njuics.test.HelloWorld.arr
R 9 0000000039dc295d I[3]
arr[3] is 3
R 9 000000003f175950 njuics.test.HelloWorld.arr
R 9 0000000039dc295e I[4]
arr[4] is 4
R 9 000000003f175950 njuics.test.HelloWorld.arr
R 9 0000000039dc295f I[5]
arr[5] is 5
R 9 000000003f175950 njuics.test.HelloWorld.arr
R 9 0000000039dc2960 I[6]
arr[6] is 6
R 9 000000003f175950 njuics.test.HelloWorld.arr
R 9 0000000039dc2961 I[7]
arr[7] is 7
R 9 000000003f175950 njuics.test.HelloWorld.arr
R 9 0000000039dc2962 I[8]
arr[8] is 8
R 9 000000003f175950 njuics.test.HelloWorld.arr
R 9 0000000039dc2963 I[9]
arr[9] is 9

```