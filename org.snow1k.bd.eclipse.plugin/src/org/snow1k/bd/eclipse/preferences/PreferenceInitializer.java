package org.snow1k.bd.eclipse.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.snow1k.bd.eclipse.BetterDecompilerPlugin;

/**
 * Class used to initialize default preference values.<br>
 * 初始化配置项，在plugin.xml中配置扩展点
 * 
 * @author 千堆雪
 * @version 1.0.0
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {
    /**
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
     */
    @Override
    public void initializeDefaultPreferences() {
        IPreferenceStore store = BetterDecompilerPlugin.getDefault().getPreferenceStore();
        // 对每一个项，设置默认值
        store.setDefault(BetterDecompilerPlugin.PREF_ESCAPE_UNICODE_CHARACTERS, false);
        store.setDefault(BetterDecompilerPlugin.PREF_REALIGN_LINE_NUMBERS, true);
        store.setDefault(BetterDecompilerPlugin.PREF_SHOW_LINE_NUMBERS, true);
        store.setDefault(BetterDecompilerPlugin.PREF_SHOW_METADATA, true);
    }
}
