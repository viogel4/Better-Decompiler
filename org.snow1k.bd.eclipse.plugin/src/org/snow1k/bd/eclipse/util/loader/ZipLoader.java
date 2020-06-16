package org.snow1k.bd.eclipse.util.loader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.jd.core.v1.api.loader.Loader;
import org.jd.core.v1.api.loader.LoaderException;

/**
 * 压缩包格式加载器
 * 
 * @author 千堆雪
 * @date 2020/06/16
 */
public class ZipLoader implements Loader {
    protected HashMap<String, byte[]> map = new HashMap<>();

    public ZipLoader(File zip) throws LoaderException {
        byte[] buffer = new byte[1024 * 2];

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zip))) {
            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {
                if (!ze.isDirectory()) {
                    // 没有关闭流？？？
                    ByteArrayOutputStream out = new ByteArrayOutputStream();

                    int read = zis.read(buffer);
                    while (read > 0) {
                        out.write(buffer, 0, read);
                        read = zis.read(buffer);
                    }

                    map.put(ze.getName(), out.toByteArray());
                }

                ze = zis.getNextEntry();
            }

            zis.closeEntry();
        } catch (IOException e) {
            throw new LoaderException(e);
        }
    }

    /**
     * 通过class文件获取字节码内容
     */
    @Override
    public byte[] load(String internalName) throws LoaderException {
        return map.get(internalName + ".class");
    }

    /**
     * 判断是否可加载指定名称的class文件
     */
    @Override
    public boolean canLoad(String internalName) {
        return map.containsKey(internalName + ".class");
    }
}
