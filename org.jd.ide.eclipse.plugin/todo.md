Bundle-ClassPath: /lib/jd-core-1.1.3.jar 后面要不要加一个点呢？



//自定义源码目录，但对应的输出目录默认为bin/main，如何更改
sourceSets {
	main {
		java {
			srcDir 'src'
		}
	}
}


//设置编译后输出的默认位置
eclipse {
    classpath{
        defaultOutputDir = file('bin')
    }
}


上面的配置会导致.classpath中的目标目录变更为bin/main，如何解决？
