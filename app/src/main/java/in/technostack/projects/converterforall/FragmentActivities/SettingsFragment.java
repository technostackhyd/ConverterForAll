package in.technostack.projects.converterforall.FragmentActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.TaskStackBuilder;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import in.technostack.projects.converterforall.Classes.SharedPreferencesHelper;
import in.technostack.projects.converterforall.Classes.ThemeHelper;
import in.technostack.projects.converterforall.MainActivity;
import in.technostack.projects.converterforall.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    static final String TAG = "SettingsFragmentTag";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        ListPreference themePreference = findPreference("themePref");
        if (themePreference != null) {
            themePreference.setOnPreferenceChangeListener(
                    new Preference.OnPreferenceChangeListener() {
                        @Override
                        public boolean onPreferenceChange(Preference preference, Object newValue) {
                            String themeOption = (String) newValue;
                            SharedPreferencesHelper.putString("themeSelected", themeOption, "themePrefer", Context.MODE_PRIVATE, getActivity().getBaseContext());
                            ThemeHelper.applyTheme(themeOption);
                            TaskStackBuilder.create(getActivity())
                                    .addNextIntent(new Intent(getActivity(), MainActivity.class))
                                    .addNextIntent(getActivity().getIntent())
                                    .startActivities();
                            return true;
                        }
                    });
        }
    }
}
