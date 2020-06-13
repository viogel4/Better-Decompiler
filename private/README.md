清理项目构建过程中生产的文件：

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



创建eclipse项目

```
gradlew eclipse
```

在build.gradle文件中添加 `apply plugin: 'eclipse'`，否则上述指令无效。

```
apply plugin: 'eclipse'

sourceSets {
    main {
        java {
            srcDir 'src'
            outputDir = file('bin')
        }
    }
}

eclipse.classpath.defaultOutputDir = file('bin')
```

注意：

- 目前eclipse对gradle插件支持不够好， 有时上述代码设置可能不会生效。解决方案：项目右键，Build Path -> Configure Build Path。取消勾选“Allow output folders for source folders”，点击 Apply and Close 进行保存。这样，eclipse编译的输出目录才会是指定的bin目录，否则可能会是bin/main目录，且无法修改。

  ![image-20200614011640095](C:\Users\千堆雪\AppData\Roaming\Typora\typora-user-images\image-20200614011640095.png)

- 在转化为eclipse项目之后，不能再使用项目右键 ->Gradle -> Refresh Gradle Project，否则eclipse项目又会重新转化为 Gradle 项目，.classpath文件又会恢复成原来的格式。

- eclispe插件开发时，如果需要启动或调试，则必须使用eclipse项目的格式。gradle项目格式，只是可以用于构建和打包，没办法使用eclipse自带的插件调试功能。







