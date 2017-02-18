package com.ariorick.uber777.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

/**
 * Created by arior on 06.02.2017.
 * Class for getting TypeFaces faster
 */

public class FontCache {

    private static Hashtable<String, Typeface> fontCache = new Hashtable<>();

    public static Typeface get(String name, Context context) {
        Typeface tf = fontCache.get(name);
        if (tf == null) {
            try {
                tf = Typeface.createFromAsset(context.getAssets(), name);
            } catch (Exception e) {
                return null;
            }
            fontCache.put(name, tf);
        }
        return tf;
    }
}