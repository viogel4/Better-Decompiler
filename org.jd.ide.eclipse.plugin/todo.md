Bundle-ClassPath: /lib/jd-core-1.1.3.jar ����Ҫ��Ҫ��һ�����أ�



//�Զ���Դ��Ŀ¼������Ӧ�����Ŀ¼Ĭ��Ϊbin/main����θ���
sourceSets {
	main {
		java {
			srcDir 'src'
		}
	}
}


//���ñ���������Ĭ��λ��
eclipse {
    classpath{
        defaultOutputDir = file('bin')
    }
}


��������ûᵼ��.classpath�е�Ŀ��Ŀ¼���Ϊbin/main����ν����
