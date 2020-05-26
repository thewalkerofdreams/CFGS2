package com.example.adventuremaps.Activities.AutoRestartApp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.adventuremaps.Activities.MainActivity;
import com.example.adventuremaps.Management.ApplicationConstants;

public class MyExceptionHandler implements Thread.UncaughtExceptionHandler {//Saltará cuando la aplicación se cierre por algún error

    private Activity activity;

    public MyExceptionHandler(Activity a) {
        activity = a;
    }

    @Override
    public void uncaughtException(@NonNull Thread thread, @NonNull Throwable ex) {
        Intent intent = new Intent(activity, MainActivity.class);//Indicamos la actividad a cargar en caso de reinicio
        intent.putExtra(ApplicationConstants.AUTORESTART_APP_CRASH, true);//Indicamos que la app crasheo
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP //Elimina las demás actividades existentes, instanciando una nueva en el task principal
                | Intent.FLAG_ACTIVITY_CLEAR_TASK      //Limpiamos todos las tareas existentes
                | Intent.FLAG_ACTIVITY_NEW_TASK);      //Creamos una nueva tarea

        //Aunque la actividad actual muera, esto nos permitirá volver a recargarla
        PendingIntent pendingIntent = PendingIntent.getActivity(MyApplication.getInstance().getBaseContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) MyApplication.getInstance().getBaseContext().getSystemService(Context.ALARM_SERVICE);//Creamos una alarma
        if(mgr != null)//Si la alarma es diferente de null
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);//La alarma se ejecutara en un segundo
        activity.finish();//Cerramos la aplicación
    }
}