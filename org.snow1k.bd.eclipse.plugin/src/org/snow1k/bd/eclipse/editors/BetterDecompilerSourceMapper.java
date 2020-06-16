package org.snow1k.bd.eclipse.editors;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.internal.core.SourceMapper;
import org.eclipse.jface.preference.IPreferenceStore;
import org.jd.core.v1.ClassFileToJavaSourceDecompiler;
import org.jd.core.v1.api.loader.Loader;
import org.snow1k.bd.eclipse.BetterDecompilerPlugin;
import org.snow1k.bd.eclipse.util.loader.DirectoryLoader;
import org.snow1k.bd.eclipse.util.loader.ZipLoader;
import org.snow1k.bd.eclipse.util.printer.LineNumberStringBuilderPrinter;

/**
 * SourceMapper
 * 
 * @author 千堆雪
 * @version 1.0.0
 */
public class BetterDecompilerSourceMapper extends SourceMapper {
    // 字节码文件名后缀
    private final static String JAVA_CLASS_SUFFIX = ".class";
    // 源码文件名后缀
    private final static String JAVA_SOURCE_SUFFIX = ".java";
    private final static int JAVA_SOURCE_SUFFIX_LENGTH = 5;

    // jd-core反编译器，来源于jd-core反编译引擎
    private final static ClassFileToJavaSourceDecompiler DECOMPILER = new ClassFileToJavaSourceDecompiler();
    private LineNumberStringBuilderPrinter printer = new LineNumberStringBuilderPrinter();

    private File basePath;

    public BetterDecompilerSourceMapper(File basePath, IPath sourcePath, String sourceRootPath,
        Map<String, String> options) {
        super(sourcePath, sourceRootPath, options);
        this.basePath = basePath;
    }

    /**
     * 获取源码，覆盖父类方法
     */
    @Override
    public char[] findSource(String javaSourcePath) {
        char[] source = null;

        // Search source file
        if (this.rootPaths == null) {
            source = super.findSource(javaSourcePath);
        } else {
            Iterator iterator = this.rootPaths.iterator();

            while (iterator.hasNext() && (source == null)) {
                String sourcesRootPath = (String)iterator.next();
                source = super.findSource(sourcesRootPath + IPath.SEPARATOR + javaSourcePath);
            }
        }

        if ((source == null) && javaSourcePath.toLowerCase().endsWith(JAVA_SOURCE_SUFFIX)) {
            String internalTypeName = javaSourcePath.substring(0, javaSourcePath.length() - JAVA_SOURCE_SUFFIX_LENGTH);

            try {
                // 对字节码文件进行反编译，此处也可更改为使用其它反编译引擎
                source = decompile(this.basePath.getAbsolutePath(), internalTypeName);
            } catch (Exception e) {
                BetterDecompilerPlugin.getDefault().getLog()
                    .log(new Status(Status.ERROR, BetterDecompilerPlugin.PLUGIN_ID, 0, e.getMessage(), e));
            }
        }

        return source;
    }

    /**
     * @param basePath
     *            Path to the root of the classpath, either a path to a directory or a path to a jar file.
     * @param internalClassName
     *            internal name of the class，类名称
     * @return Decompiled class text，返回反编译后的源码内容
     */
    protected char[] decompile(String basePath, String internalTypeName) throws Exception {
        // 加载插件配置参数
        IPreferenceStore store = BetterDecompilerPlugin.getDefault().getPreferenceStore();

        // 行号重排
        boolean realignmentLineNumber = store.getBoolean(BetterDecompilerPlugin.PREF_REALIGN_LINE_NUMBERS);
        // unicode编码
        boolean unicodeEscape = store.getBoolean(BetterDecompilerPlugin.PREF_ESCAPE_UNICODE_CHARACTERS);
        // 显示行号
        boolean showLineNumbers = store.getBoolean(BetterDecompilerPlugin.PREF_SHOW_LINE_NUMBERS);
        // 显示元数据
        boolean showMetaData = store.getBoolean(BetterDecompilerPlugin.PREF_SHOW_METADATA);

        Map<String, Object> configuration = new HashMap<>();
        configuration.put("realignLineNumbers", realignmentLineNumber);

        // Initialize loader
        Loader loader;
        File base = new File(basePath);

        if (base.isFile()) {
            if (basePath.toLowerCase().endsWith(".jar") || basePath.toLowerCase().endsWith(".zip")) {
                loader = new ZipLoader(base);
            } else {
                BetterDecompilerPlugin.getDefault().getLog().log(new Status(Status.ERROR,
                    BetterDecompilerPlugin.PLUGIN_ID, "Unexpected container type file: " + basePath));
                return null;
            }
        } else {
            loader = new DirectoryLoader(base);
        }

        // 对printer进行设置
        printer.setRealignmentLineNumber(realignmentLineNumber);
        printer.setUnicodeEscape(unicodeEscape);
        printer.setShowLineNumbers(showLineNumbers);

        // Decompile class file
        DECOMPILER.decompile(loader, printer, internalTypeName, configuration);

        StringBuilder stringBuffer = printer.getStringBuffer();

        if (showMetaData) {// 如果显示元数据
            // Add location
            stringBuffer.append("\n\n/* Location:              ");
            String classPath = internalTypeName + JAVA_CLASS_SUFFIX;
            String location = base.isFile() ? base.getPath() + "!/" + classPath : new File(base, classPath).getPath();
            // Escape "\ u" sequence to prevent "Invalid unicode" errors
            stringBuffer.append(location.replaceAll("(^|[^\\\\])\\\\u", "\\\\\\\\u"));

            // Add Java compiler version，添加java编译器版本
            int majorVersion = printer.getMajorVersion();
            if (majorVersion >= 45) {
                stringBuffer.append("\n * Java compiler version: ");

                if (majorVersion >= 49) {
                    stringBuffer.append(majorVersion - (49 - 5));
                } else {
                    stringBuffer.append(majorVersion - (45 - 1));
                }

                stringBuffer.append(" (");
                stringBuffer.append(majorVersion);
                stringBuffer.append('.');
                stringBuffer.append(printer.getMinorVersion());
                stringBuffer.append(')');
            }

            // Add JD-Core version，添加jd-core版本
            stringBuffer.append("\n * JD-Core Version:       ");
            stringBuffer.append(BetterDecompilerPlugin.VERSION_JD_CORE);
            stringBuffer.append("\n */");
        }

        return stringBuffer.toString().toCharArray();
    }
}
