package in.technostack.projects.converterforall;

import android.app.Application;
import in.technostack.projects.converterforall.Classes.SharedPreferencesHelper;
import in.technostack.projects.converterforall.Classes.ThemeHelper;

public class ConverterApp extends Application {

    public void onCreate() {
        super.onCreate();
        ThemeHelper.applyTheme(SharedPreferencesHelper.getString("themeSelected", "default", "themePrefer", MODE_PRIVATE, getBaseContext()));
    }
}
