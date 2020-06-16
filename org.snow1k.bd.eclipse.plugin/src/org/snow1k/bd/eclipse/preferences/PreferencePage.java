package org.snow1k.bd.eclipse.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
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
        setDescription("Better Decompiler preference page");
    }

    /**
     * Creates the field editors. Field editors are abstractions of the common GUI blocks needed to manipulate various
     * types of preferences. Each field editor knows how to save and restore itself.
     */
    public void createFieldEditors() {
        Composite fieldEditorParent = getFieldEditorParent();

        // 此句代码仅仅是为了添加一个空行
        new Label(fieldEditorParent, SWT.NONE);

        addField(new BooleanFieldEditor(BetterDecompilerPlugin.PREF_ESCAPE_UNICODE_CHARACTERS,
            "Escape unicode characters", fieldEditorParent));

        addField(new BooleanFieldEditor(BetterDecompilerPlugin.PREF_REALIGN_LINE_NUMBERS, "Realign line numbers",
            fieldEditorParent));

        addField(new BooleanFieldEditor(BetterDecompilerPlugin.PREF_SHOW_LINE_NUMBERS, "Show original line numbers",
            fieldEditorParent));

        addField(new BooleanFieldEditor(BetterDecompilerPlugin.PREF_SHOW_METADATA, "Show metadata", fieldEditorParent));

        Composite com = new Composite(fieldEditorParent, SWT.NONE);
        com.setBackground(fieldEditorParent.getDisplay().getSystemColor(SWT.COLOR_RED));
        System.out.println(fieldEditorParent.getBounds().toString());
        ？？？？？？
        // 给composite设置而已，以使labl居右
        Label lbl = new Label(com, SWT.RIGHT);
        lbl.setText("千堆雪");
        lbl.setLocation(5, 5);
        lbl.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_CYAN));

    }

    /**
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    @Override
    public void init(IWorkbench workbench) {
        setPreferenceStore(BetterDecompilerPlugin.getDefault().getPreferenceStore());
    }
}