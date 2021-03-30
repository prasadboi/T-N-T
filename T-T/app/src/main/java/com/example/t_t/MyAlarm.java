package com.example.t_t;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

//class extending the Broadcast Receiver
public class MyAlarm extends BroadcastReceiver {

    //the method will be fired when the alarm is triggerred
    @Override
    public void onReceive(Context context, Intent intent) {
        String email = intent.getStringExtra("Email");
        String username = intent.getStringExtra("Username");
        String task = intent.getStringExtra("Task");
        String time = intent.getStringExtra("Time");
        String date = intent.getStringExtra("Date");
        String desp = intent.getStringExtra("Description");
        MediaPlayer mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
        mediaPlayer.start();
        //you can check the log that it is fired
        //Here we are actually not doing anything
        //but you can do any task here that you want to be done at a specific time everyday
        Log.d("MyAlarmBelal", "Alarm just fired");
        try
        {
            LongOperation2 l = new LongOperation2(email,username,time,date,task,desp);
            l.execute();  //sends the email in background
            Toast.makeText(context.getApplicationContext(), l.get().toString(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
        }

    }
    }


