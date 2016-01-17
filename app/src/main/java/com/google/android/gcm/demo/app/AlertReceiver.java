package com.google.android.gcm.demo.app;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import java.util.Calendar;

/**
 * Created by Wei Hao on 1/6/2016.
 */
public class AlertReceiver extends BroadcastReceiver {
    
    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("Receive");
        createNotification(context, "Alert", "blsh", "Alert");
        this.context=context;
    }

    public void createNotification(Context context,String msg,String msgTextm,String msgalert) {

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, DemoActivity.class), 0);

        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setContentTitle(msg)
                .setTicker(msgalert)
                .setSmallIcon(R.drawable.notification_template_icon_bg)
                .setContentText(msgTextm);


        builder.setContentIntent(pendingIntent);

        builder.setDefaults(NotificationCompat.DEFAULT_SOUND);

        builder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    public void setAlarms(){
        Calendar timeOff = Calendar.getInstance();
        int days;
        Intent alertIntent;
        AlarmManager alarmManager;
        System.out.println(timeOff.get(Calendar.DAY_OF_WEEK));
        switch (timeOff.get(Calendar.DAY_OF_WEEK)){
            case 5:
                days = Calendar.FRIDAY+ (7 - timeOff.get(Calendar.DAY_OF_WEEK));
                if(days>7)
                    days-=7;
                System.out.println("days"+days);
                timeOff.add(Calendar.DATE, days);
                timeOff.set(Calendar.HOUR, 12);
                timeOff.set(Calendar.MINUTE, 0);
                timeOff.set(Calendar.SECOND, 0);
                alertIntent=new Intent(context,AlertReceiver.class);
                alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                System.out.println(timeOff);
                alarmManager.set(AlarmManager.RTC_WAKEUP, timeOff.getTimeInMillis(), PendingIntent.getBroadcast(context, 1, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                break;
            case 6:
                days = Calendar.SATURDAY+ (7 - timeOff.get(Calendar.DAY_OF_WEEK));
                if(days>7)
                    days-=7;
                System.out.println("days"+days);
                timeOff.add(Calendar.DATE, days);
                timeOff.set(Calendar.HOUR, 12);
                timeOff.set(Calendar.MINUTE, 0);
                timeOff.set(Calendar.SECOND, 0);
                alertIntent=new Intent(context,AlertReceiver.class);
                alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                System.out.println(timeOff);
                alarmManager.set(AlarmManager.RTC_WAKEUP, timeOff.getTimeInMillis(), PendingIntent.getBroadcast(context, 1, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                break;
            case 7:
                days = Calendar.SUNDAY+ (7 - timeOff.get(Calendar.DAY_OF_WEEK));
                if(days>7)
                    days-=7;
                System.out.println("days"+days);
                timeOff.add(Calendar.DATE, days);
                timeOff.set(Calendar.HOUR, 12);
                timeOff.set(Calendar.MINUTE, 0);
                timeOff.set(Calendar.SECOND, 0);
                alertIntent=new Intent(context,AlertReceiver.class);
                alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                System.out.println(timeOff);
                alarmManager.set(AlarmManager.RTC_WAKEUP, timeOff.getTimeInMillis(), PendingIntent.getBroadcast(context, 1, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                break;
            default:
                days = Calendar.THURSDAY+ (7 - timeOff.get(Calendar.DAY_OF_WEEK));
                if(days>7)
                    days-=7;
                System.out.println("days" + days);
                timeOff.add(Calendar.DATE, days);
                timeOff.set(Calendar.HOUR, 12);
                timeOff.set(Calendar.MINUTE, 0);
                timeOff.set(Calendar.SECOND, 0);
                alertIntent=new Intent(context,AlertReceiver.class);
                alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                System.out.println(timeOff);
                alarmManager.set(AlarmManager.RTC_WAKEUP, timeOff.getTimeInMillis(), PendingIntent.getBroadcast(context, 1, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                break;

        }


    }

    }


