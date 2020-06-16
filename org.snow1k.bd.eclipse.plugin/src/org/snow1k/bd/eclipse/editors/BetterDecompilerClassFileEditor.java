package org.snow1k.bd.eclipse.editors;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.core.BufferManager;
import org.eclipse.jdt.internal.core.PackageFragmentRoot;
import org.eclipse.jdt.internal.ui.javaeditor.ClassFileEditor;
import org.eclipse.jdt.internal.ui.javaeditor.IClassFileEditorInput;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.snow1k.bd.eclipse.BetterDecompilerPlugin;

/**
 * 继承自字符码编辑器。
 * 
 * @author 千堆雪
 * @version 1.0.0
 * @see org.eclipse.jdt.internal.ui.javaeditor.ClassFileEditor
 */
public class BetterDecompilerClassFileEditor extends ClassFileEditor implements IPropertyChangeListener {

    public BetterDecompilerClassFileEditor() {
        // 注册事件监听
        BetterDecompilerPlugin.getDefault().getPreferenceStore().addPropertyChangeListener(this);
    }

    @Override
    protected void doSetInput(IEditorInput input) throws CoreException {
        if (input instanceof IFileEditorInput) {
            IFile file = ((IFileEditorInput)input).getFile();

            if (file instanceof IClassFile) {
                IClassFile classFile = (IClassFile)file;
                cleanupBuffer(classFile);
                setupSourceMapper(classFile);
            }
        } else if (input instanceof IClassFileEditorInput) {
            IClassFileEditorInput classFileEditorInput = (IClassFileEditorInput)input;
            IClassFile classFile = classFileEditorInput.getClassFile();
            cleanupBuffer(classFile);
            setupSourceMapper(classFile);
        }

        super.doSetInput(input);
    }

    protected static void cleanupBuffer(IClassFile file) {
        IBuffer buffer = BufferManager.getDefaultBufferManager().getBuffer(file);

        if (buffer != null) {
            try {
                // Remove the buffer
                Method method = BufferManager.class.getDeclaredMethod("removeBuffer", new Class[] {IBuffer.class});
                method.setAccessible(true);
                method.invoke(BufferManager.getDefaultBufferManager(), new Object[] {buffer});
            } catch (Exception e) {
                BetterDecompilerPlugin.getDefault().getLog()
                    .log(new Status(Status.ERROR, BetterDecompilerPlugin.PLUGIN_ID, 0, e.getMessage(), e));
            }
        }
    }

    protected void setupSourceMapper(IClassFile classFile) {
        try {
            // Search package fragment root and classPath
            IJavaElement packageFragment = classFile.getParent();
            IJavaElement packageFragmentRoot = packageFragment.getParent();

            if (packageFragmentRoot instanceof PackageFragmentRoot) {
                // Setup a new source mapper.
                PackageFragmentRoot root = (PackageFragmentRoot)packageFragmentRoot;

                // Location of the archive file containing classes.
                IPath basePath = root.getPath();
                File baseFile = basePath.makeAbsolute().toFile();

                if (!baseFile.exists()) {
                    IResource resource = root.getCorrespondingResource();
                    basePath = resource.getLocation();
                    baseFile = basePath.makeAbsolute().toFile();
                }

                // Class path
                String classPath = classFile.getElementName();
                String packageName = packageFragment.getElementName();
                if ((packageName != null) && (packageName.length() > 0)) {
                    classPath = packageName.replace('.', '/') + '/' + classPath;
                }

                // Location of the archive file containing source.
                IPath sourcePath = root.getSourceAttachmentPath();
                if (sourcePath == null) {
                    sourcePath = basePath;
                }

                // Location of the package fragment root within the zip
                // (empty specifies the default root).
                IPath sourceAttachmentRootPath = root.getSourceAttachmentRootPath();
                String sourceRootPath;

                if (sourceAttachmentRootPath == null) {
                    sourceRootPath = null;
                } else {
                    sourceRootPath = sourceAttachmentRootPath.toString();
                    if ((sourceRootPath != null) && (sourceRootPath.length() == 0))
                        sourceRootPath = null;
                }

                // Options
                Map<String, String> options = root.getJavaProject().getOptions(true);

                root.setSourceMapper(new BetterDecompilerSourceMapper(baseFile, sourcePath, sourceRootPath, options));
            }
        } catch (CoreException e) {
            BetterDecompilerPlugin.getDefault().getLog()
                .log(new Status(Status.ERROR, BetterDecompilerPlugin.PLUGIN_ID, 0, e.getMessage(), e));
        }
    }

    @Override
    public boolean isEditable() {
        return false;
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public boolean isEditorInputReadOnly() {
        return false;
    }

    /**
     * 关闭字节码编辑器时，移除事件监听器
     */
    @Override
    public void dispose() {
        BetterDecompilerPlugin.getDefault().getPreferenceStore().removePropertyChangeListener(this);
    }

    /**
     * 
     * 刷新反编译代码， 属性改变时触发此事件
     * 
     */
    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (super.getSourceViewer() != null) {
            super.setInput(super.getEditorInput());
        }
    }
}
