package org.snow1k.bd.eclipse.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.snow1k.bd.eclipse.BetterDecompilerPlugin;

/**
 * ui配置页，可在Preferences->Java->Better Decompiler中进行设置。<br>
 * 在plugin.xml中配置扩展点
 * 
 * @author 千堆雪
 * @version 1.0.0
 * 
 * 
 */
public final class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public PreferencePage() {
        super(SWT.NONE);
        // setDescription("Better Decompiler Preference");
    }

    /**
     * Creates the field editors. Field editors are abstractions of the common GUI blocks needed to manipulate various
     * types of preferences. Each field editor knows how to save and restore itself.
     */
    public void createFieldEditors() {
        Composite fieldEditorParent = getFieldEditorParent();

        // 此句代码仅仅是为了添加一个空行
        // new Label(fieldEditorParent, SWT.NONE);

        addField(new ComboFieldEditor(BetterDecompilerPlugin.PREF_DECOMPILE_ENGINE, "Engine",
            new String[][] {{"JD-Core", "JD-Core"}, {"Procyon", "Procyon"}, {"FernFlower", "FernFlower"}},
            fieldEditorParent));

        // 此句代码仅仅是为了添加一个空行
        new Label(fieldEditorParent, SWT.NONE);

        addField(new BooleanFieldEditor(BetterDecompilerPlugin.PREF_ESCAPE_UNICODE_CHARACTERS,
            "Escape unicode characters", fieldEditorParent));

        addField(new BooleanFieldEditor(BetterDecompilerPlugin.PREF_REALIGN_LINE_NUMBERS, "Realign line numbers",
            fieldEditorParent));

        addField(new BooleanFieldEditor(BetterDecompilerPlugin.PREF_SHOW_LINE_NUMBERS, "Show original line numbers",
            fieldEditorParent));

        addField(new BooleanFieldEditor(BetterDecompilerPlugin.PREF_SHOW_METADATA, "Show metadata", fieldEditorParent));

        // 此句代码仅仅是为了添加一个空行
        new Label(fieldEditorParent, SWT.NONE);

        Group group = new Group(fieldEditorParent, SWT.NONE);
        group.setText("关于");
        group.setLayout(new GridLayout());
        Label about = new Label(group, SWT.NONE);
        about.setText("重要：请勿将此插件用于非法用途。");

        Composite comp = new Composite(group, SWT.NONE);
        Label author = new Label(comp, SWT.RIGHT);
        author.setText("——千堆雪，2020-06-17");
        author.setBounds(0, 20, 350, 24);
        // author.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_CYAN));

    }

    /**
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    @Override
    public void init(IWorkbench workbench) {
        setPreferenceStore(BetterDecompilerPlugin.getDefault().getPreferenceStore());
    }
}