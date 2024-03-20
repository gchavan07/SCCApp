package com.project.roomeet;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class PrefHelper {


    public static final String EMAIL = "EmailKey";

    public static final String NAME = "NameKey";

    private static PrefHelper instance;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    private final String SHARED_PREFERENCE_NAME = "SHARED_PREFERENCE";


    static Context mContext;

    public static void init(Context context) {
        mContext = context;
        if (instance == null) instance = new PrefHelper(context);

    }


    public PrefHelper(Context context) {
        mSharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        sharedPreferencesEditor = mSharedPreferences.edit();


    }

    public static PrefHelper getInstance() {
        if (instance == null) {
            instance = new PrefHelper(mContext);
            Log.e("pref instance", "null");
        }
        return instance;
    }

    public void saveInt(String key, int value) {
        sharedPreferencesEditor.putInt(key, value);
        sharedPreferencesEditor.apply();
    }

    public int getInt(String key) {
        return mSharedPreferences.getInt(key, 0);
    }

    public void saveString(String key, String value) {
        sharedPreferencesEditor.putString(key, value);
        sharedPreferencesEditor.apply();
    }

    public String getString(String key) {
        return mSharedPreferences.getString(key, "");
    }


    public void saveBoolean(String key, boolean value) {
        sharedPreferencesEditor.putBoolean(key, value);
        sharedPreferencesEditor.apply();
    }

    public boolean getBoolean(String key) {
        return mSharedPreferences.getBoolean(key, false);
    }

    public boolean getBooleanDefaultTrue(String key) {
        return mSharedPreferences.getBoolean(key, true);
    }

    public void saveLong(String key, long value) {
        sharedPreferencesEditor.putLong(key, value);
        sharedPreferencesEditor.apply();
    }

    public long getLong(String key) {
        return mSharedPreferences.getLong(key, 0);
    }

    public void saveFloat(String key, float value) {
        sharedPreferencesEditor.putFloat(key, value);
        sharedPreferencesEditor.apply();
    }

    public float getFloat(String key) {
        return mSharedPreferences.getFloat(key, 0);
    }

    public void resetLoginPreferences() {
        // by kp
        sharedPreferencesEditor.clear();
        sharedPreferencesEditor.commit();
    }


}
