/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy. This project is distributed under the GPLv3 license. This is a Copyleft
 * license that gives the user the right to use, copy and modify the code freely for non-commercial purposes.
 */

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
 * @version 0.1.4
 * @see org.eclipse.jdt.internal.core.SourceMapper
 */
public class JDSourceMapper extends SourceMapper {
    private final static String JAVA_CLASS_SUFFIX = ".class";
    private final static String JAVA_SOURCE_SUFFIX = ".java";
    private final static int JAVA_SOURCE_SUFFIX_LENGTH = 5;

    private final static ClassFileToJavaSourceDecompiler DECOMPILER = new ClassFileToJavaSourceDecompiler();

    private File basePath;

    private LineNumberStringBuilderPrinter printer = new LineNumberStringBuilderPrinter();

    public JDSourceMapper(File basePath, IPath sourcePath, String sourceRootPath, Map options) {
        super(sourcePath, sourceRootPath, options);
        this.basePath = basePath;
    }

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

            // Decompile class file
            try {
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
     *            internal name of the class.
     * @return Decompiled class text.
     */
    protected char[] decompile(String basePath, String internalTypeName) throws Exception {
        // 加载插件配置参数
        IPreferenceStore store = BetterDecompilerPlugin.getDefault().getPreferenceStore();

        // 行号重振
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

        // Initialize printer
        printer.setRealignmentLineNumber(realignmentLineNumber);
        printer.setUnicodeEscape(unicodeEscape);
        printer.setShowLineNumbers(showLineNumbers);

        // Decompile class file
        DECOMPILER.decompile(loader, printer, internalTypeName, configuration);

        StringBuilder stringBuffer = printer.getStringBuffer();

        // Metadata
        if (showMetaData) {
            // Add location
            stringBuffer.append("\n\n/* Location:              ");
            String classPath = internalTypeName + JAVA_CLASS_SUFFIX;
            String location = base.isFile() ? base.getPath() + "!/" + classPath : new File(base, classPath).getPath();
            // Escape "\ u" sequence to prevent "Invalid unicode" errors
            stringBuffer.append(location.replaceAll("(^|[^\\\\])\\\\u", "\\\\\\\\u"));
            // Add Java compiler version
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
            // Add JD-Core version
            stringBuffer.append("\n * JD-Core Version:       ");
            stringBuffer.append(BetterDecompilerPlugin.VERSION_JD_CORE);
            stringBuffer.append("\n */");
        }

        return stringBuffer.toString().toCharArray();
    }
}
