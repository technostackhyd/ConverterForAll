package in.technostack.projects.converterforall.Classes;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {
    public static void putInt(String key, int value, String prefName, int MODE, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(prefName, MODE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }
    public static void putString(String key, String value, String prefName, int MODE, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(prefName, MODE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public static void putBoolean(String key, Boolean value, String prefName, int MODE, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(prefName, MODE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static String getString(String key, String def, String prefName, int MODE, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(prefName, MODE);
        return preferences.getString(key, def);
    }
    public static int getInt(String key, int def, String prefName, int MODE, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(prefName, MODE);
        return preferences.getInt(key, def);
    }
    public static Boolean getBoolean(String key, Boolean def, String prefName, int MODE, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(prefName, MODE);
        return preferences.getBoolean(key, def);
    }
}
