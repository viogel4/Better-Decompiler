此项目为 Java Decompiler 反编译引擎的 eclipse 插件。 Java Decompiler 项目官网地址：[http://java-decompiler.github.io](http://java-decompiler.github.io)

其中，JD-GUI 是 Java Decompiler 的图形界面接口。JD-Eclipse 是 Java Decompiler 在 Eclipse 上的插件，其核心为 JD-Core。

本项目不是原创性项目，而是中断项目 JD-Eclipse  的临时延续性维护，原项目因未知原因，更新迟滞，但尚不能确认已中断（中断可能性不大）。

JD-Eclipse 的项目地址：https://github.com/java-decompiler/jd-eclipse



编译并打包：

```shell
gradlew build
```



# JD-Eclipse

JD-Eclipse, a Java decompiler plug-in for the Eclipse platform.

- Java Decompiler projects home page: [http://java-decompiler.github.io](http://java-decompiler.github.io)
- Java Decompiler Wikipedia page: [http://en.wikipedia.org/wiki/Java_Decompiler](http://en.wikipedia.org/wiki/Java_Decompiler)
- JD-Eclipse source code: [https://github.com/java-decompiler/jd-eclipse](https://github.com/java-decompiler/jd-eclipse)

## Description
JD-Eclipse is a plug-in for the Eclipse platform. It allows you to 
display all the Java sources during your debugging process, even if 
you do not have them all.

## How to build JD-Eclipse ?
### With Gradle:
```
> ./gradlew installSiteDist
```
generate _"build/install/jd-eclipse-site"_
### With Eclipse:
- Download dependencies
```
> ./gradlew downloadDependencies
```
- Launch _Eclipse_,
- Import the 3 _"Existing Projects into Workspace"_ by selecting the parent project folder,
- Export _"Deployable features"_,
- Copy _"site.xml"_ to the destination directory.

## How to install JD-Eclipse ?
1. Build or download & unzip _"jd-eclipse-site-x.y.z.zip"_,
2. Launch _Eclipse_,
3. Click on _"Help > Install New Software..."_,
4. Click on button _"Add..."_ to add an new repository,
5. Enter _"JD-Eclipse Update Site"_ and select the local site directory,
6. Check _"Java Decompiler Eclipse Plug-in"_,
7. Next, next, next... and restart.

## How to check the file associations ?
Click on _"Window > Preferences > General > Editors > File Associations"_
- _"*.class"_ : _Eclipse_ _"Class File Viewer"_ is selected by default.
- _"*.class without source"_ : _"JD Class File Viewer"_ is selected by default.

## How to configure JD-Eclipse ?
Click on _"Window > Preferences > Java > Decompiler"_

## How to uninstall JD-Eclipse ?
1. Click on _"Help > About Eclipse > Installation Details"_,
2. Select _"JD-Eclipse Plug-in"_,
3. Click on _"Uninstall..."_.

## License
Released under the [GNU GPL v3](LICENSE).

## Donations
Did JD-GUI help you to solve a critical situation? Do you use JD-Eclipse daily? What about making a donation?



2.0.1：

1. 使用 jd-core-1.1.3版本。
2. 测试使用环境 eclipse 2020-3。
3. 使用 jdk8 编译，暂不使用更高版本。
4. 项目中原有的一些bug修复。
5. 删除原项目中的一些冗余文件。
6. gradle脚本更新为新版本用法，剔除过时用法，方便过渡到更新版本。
7. 内部版本号暂未更新。