package org.snow1k.bd.eclipse;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IFileEditorMapping;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.registry.EditorDescriptor;
import org.eclipse.ui.internal.registry.EditorRegistry;
import org.eclipse.ui.internal.registry.FileEditorMapping;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * Better Decompiler Eclipse Plugin<br>
 * The activator class controls the plug-in lifecycle<br>
 * 插件主类，本类用于控制插件的生命周期
 * 
 * @author 千堆雪
 * @version 0.1.4
 */
public class BetterDecompilerPlugin extends AbstractUIPlugin {
    // jd-core的版本号
    public static final String VERSION_JD_CORE = "1.1.3";

    // The plug-in IDs
    public static final String PLUGIN_ID = "org.snow1k.bd.eclipse.plugin";
    private static final String EDITOR_ID = PLUGIN_ID + ".editors.JDClassFileEditor";

    // Preferences
    public static final String PREF_ESCAPE_UNICODE_CHARACTERS = PLUGIN_ID + ".prefs.EscapeUnicodeCharacters";
    public static final String PREF_REALIGN_LINE_NUMBERS = PLUGIN_ID + ".prefs.RealignLineNumbers";
    public static final String PREF_SHOW_LINE_NUMBERS = PLUGIN_ID + ".prefs.ShowLineNumbers";
    public static final String PREF_SHOW_METADATA = PLUGIN_ID + ".prefs.ShowMetadata";
    public static final String PREF_DECOMPILE_ENGINE = PLUGIN_ID + ".prefs.Engine";

    // URLs
    public static final String URL_JDECLIPSE = "https://github.com/java-decompiler/jd-eclipse";

    // The shared instance
    private static BetterDecompilerPlugin plugin;

    public BetterDecompilerPlugin() {
        // do nothing...
    }

    /**
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;

        // Setup ".class" file associations
        Display.getDefault().syncExec(new SetupClassFileAssociationRunnable());
    }

    /*
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        // 保存插件配置
        InstanceScope.INSTANCE.getNode(PLUGIN_ID).flush();
        plugin = null;
        super.stop(context);
    }

    /**
     * 静态方法，返回唯一插件实例
     * 
     * @return the shared instance
     */
    public static BetterDecompilerPlugin getDefault() {
        return plugin;
    }

    protected static class SetupClassFileAssociationRunnable implements Runnable {
        public void run() {
            EditorRegistry registry = (EditorRegistry)WorkbenchPlugin.getDefault().getEditorRegistry();

            IFileEditorMapping[] mappings = registry.getFileEditorMappings();
            IFileEditorMapping c = null;
            IFileEditorMapping cws = null;

            // Search Class file editor mappings
            for (IFileEditorMapping mapping : mappings) {
                if (mapping.getExtension().equals("class")) {
                    // ... Helios 3.6, Indigo 3.7, Juno 4.2, Kepler 4.3, ...
                    c = mapping;
                } else if (mapping.getExtension().equals("class without source")) {
                    // Juno 4.2, Kepler 4.3, ...
                    cws = mapping;
                }
            }

            if ((c != null) && (cws != null)) {
                // Search JD editor descriptor on "class" extension
                for (IEditorDescriptor descriptor : c.getEditors()) {
                    if (descriptor.getId().equals(EDITOR_ID)) {
                        // Remove JD editor on "class" extension
                        ((FileEditorMapping)c).removeEditor((EditorDescriptor)descriptor);

                        // Set JD as default editor on "class without source" extension
                        registry.setDefaultEditor("." + cws.getExtension(), descriptor.getId());
                        break;
                    }
                }

                // Restore the default editor for "class" extension
                IEditorDescriptor defaultClassFileEditor = registry.findEditor(JavaUI.ID_CF_EDITOR);

                if (defaultClassFileEditor != null) {
                    registry.setDefaultEditor("." + c.getExtension(), JavaUI.ID_CF_EDITOR);
                }

                registry.setFileEditorMappings((FileEditorMapping[])mappings);
                registry.saveAssociations();
            }
        }
    }
}