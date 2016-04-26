package com.client.capturephoto.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 
 * SharedPreferences工具
 * 
 * @作者 admin
 */
public class SPUtil
{
    private static SPUtil saver;
    public static SharedPreferences sp;
    private final static int MODE = Context.MODE_PRIVATE;
    private static final String NAME = "config";
    public static final String IP = "ip";
    public static final String PORT = "port";
    
    public static SPUtil getInstance(Context context)
    {
        if (saver == null)
        {
            saver = new SPUtil();
            sp = context.getSharedPreferences(NAME, MODE);
        }
        return saver;
    }

    public void save(String key, String value)
    {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void save(String key, int value)
    {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public String getString(String key, String defValue)
    {
        return sp.getString(key, defValue);
    }

    public int getInt(String key, int defValue)
    {
        return sp.getInt(key, defValue);
    }
}
