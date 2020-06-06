此项目为 jd 反编译引擎在 eclipse 上的插件。

此项目不是原创性项目，而是一个中断项目的延续性维护，原项目已无限期中断。



清理项目：

```shell
gradlew clean
```

编译并打包：

```shell
gradlew build
```

构建时，输入异常堆栈：

```shell
gradlew build --stacktrace
```





2.0.1：

1. 使用 jd-core-1.1.3版本。
2. 测试使用环境 eclipse 2020-3。
3. 使用 jdk8 编译，暂不使用更高版本。
4. 项目中原有的一些bug修复。
5. 删除原项目中的一些冗余文件。
6. gradle脚本更新为新版本用法，剔除过时用法，方便过渡到更新版本。