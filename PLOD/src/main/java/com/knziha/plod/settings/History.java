package com.knziha.plod.settings;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import androidx.preference.Preference;

import com.knziha.filepicker.settings.SettingsFragmentBase;
import com.knziha.plod.plaindict.PDICMainAppOptions;
import com.knziha.plod.plaindict.R;
import com.knziha.plod.dictionary.Utils.IU;
import com.knziha.plod.plaindict.Toastable_Activity;

public class History extends SettingsFragmentBase implements Preference.OnPreferenceClickListener {
	public final static int id=10;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		mPreferenceId = R.xml.pref_history;
		super.onCreate(savedInstanceState);
		init_switch_preference(this, "rc_no", PDICMainAppOptions.storeNothing(), null, null);
		init_switch_preference(this, "rc_click", PDICMainAppOptions.storeClick(), null, null);
		//init_switch_preference(this, "rc_query", PDICMainAppOptions.getHistoryStrategy1(), null, null);
		//init_switch_preference(this, "rc_net", PDICMainAppOptions.getHistoryStrategy2(), null, null);
		//init_switch_preference(this, "rc_float_pop", PDICMainAppOptions.getHistoryStrategy7(), null, null);
		init_number_info_preference(this, "rc_turn", PDICMainAppOptions.storePageTurn(), R.array.record_slide_info, null);
		
		init_switch_preference(this, "3rd", PDICMainAppOptions.storeAppId(), null, null);
		init_switch_preference(this, "cache", PDICMainAppOptions.storeIcon(), null, null);
		
		findPreference("permit").setOnPreferenceClickListener(this);
	}
	
	@Override
	public boolean onPreferenceClick(Preference preference) {
		if ("permit".equals(preference.getKey())) {
			try {
				Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
				startActivity(intent);
			} catch (Exception e) {
				((Toastable_Activity)getActivity()).showT(e);
			}
		}
		return false;
	}
	
	//配置变化
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		switch (preference.getKey()){
			case "rc_no":
				PDICMainAppOptions.storeNothing((Boolean) newValue);
			break;
			/* 记录各种点击 */
			case "rc_click":
				PDICMainAppOptions.storeClick((Boolean) newValue);
			break;
			/* 记录各种查询 */
			case "rc_query":
				PDICMainAppOptions.setHistoryStrategy1((Boolean) newValue);
			break;
			/* 记录各种联机 */
			case "rc_net":
				PDICMainAppOptions.setHistoryStrategy2((Boolean) newValue);
			break;
			/* 记录各种弹出 */
			case "rc_float_pop":
				PDICMainAppOptions.setHistoryStrategy7((Boolean) newValue);
			break;
			case "rc_turn":
				int val = IU.parsint(newValue);
				PDICMainAppOptions.storePageTurn(val);
			break;
			case "3rd":
				PDICMainAppOptions.storeAppId((Boolean) newValue);
			break;
			case "cache":
				PDICMainAppOptions.storeIcon((Boolean) newValue);
			break;
		}
		return true;
	}
}