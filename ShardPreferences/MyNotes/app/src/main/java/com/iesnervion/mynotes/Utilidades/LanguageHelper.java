package com.iesnervion.mynotes.Utilidades;

import android.app.Activity;
import android.content.res.Configuration;
import java.util.Locale;

public class LanguageHelper {
    public static final void setAppLocale(String language, Activity activity) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = activity.getResources().getConfiguration();
        config.locale = locale;
        activity.getResources().updateConfiguration(config, activity.getResources().getDisplayMetrics());
    }
}
