本项目为 Java Decompiler 反编译引擎的 eclipse 插件。 

Java Decompiler 项目官网地址：[http://java-decompiler.github.io](http://java-decompiler.github.io)，其中，JD-GUI 是 Java Decompiler 的图形界面接口。JD-Eclipse 是 Java Decompiler 在 Eclipse 上的插件，其核心为 JD-Core。

本项目不是原创性项目，而是 JD-Eclipse  项目的临时延续性维护，原项目因未知原因，更新迟滞。

JD-Eclipse 的项目地址：https://github.com/java-decompiler/jd-eclipse



本项目地址：

- https://github.com/viogel4/Better-Decompiler
- https://gitee.com/viogel4/better-decompile



## How to install Better Decompiler?
1. Build or download & unzip "better-decompiler-2-x.y.z.zip".
2. Launch _Eclipse.
3. Click on "Help > Install New Software...".
4. Darg the zip file to the dialog, the previous step just opend.
6. Check  "Better Decompiler Eclipse Plug-in".
7. Next, next, next... and restart.

## How to check the file associations ?
Click on  "Window > Preferences > General > Editors > File Associations", select the "*.class without source"   and set "Better Decompiler Class File Viewer" to default.
## How to configure Better Decompiler ?
Click on "Window > Preferences > Java > Better Decompiler"

## How to uninstall Better Decompiler ?
1. Click on "Help > About Eclipse > Installation Details",
2. Select "Better Decompiler Plug-in",
3. Click on "Uninstall...".

## License
Released under the [GNU GPL v3](LICENSE).





2.0.0：

1. 使用 jd-core-1.1.3版本。
2. 测试使用环境 eclipse 2020-3。
3. 使用 jdk8 编译，暂不使用更高版本。
4. 项目中原有的一些bug修复。
5. 删除原项目中的一些冗余文件。
6. gradle 脚本更新为新版本用法，剔除过时用法，方便过渡到更新版本。
7. 内部版本号暂未更新。