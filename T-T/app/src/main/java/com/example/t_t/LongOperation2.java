package com.example.t_t;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Date;

public class LongOperation2 extends AsyncTask<Void, Void, String>{
String Email;
String Username;
String Time;
String Date;
String Task;
String Description;


    public LongOperation2(String email,String username,String time,String date,String task,String desp){
        Email = email;
        Username = username;
        Time = time;
        Date = date;
        Task = task;
        Description = desp;
    }


    @Override
    protected String doInBackground(Void... params) {
        try {
            GMailSender sender = new GMailSender("jainakshatanil@gmail.com", "Iitpawai@2019");
            sender.sendMail("Alert!"+Username,
                    "Task:"+Task+"\n"+"Date:"+Date+"\n"+"Time:"+Time+"\n"+"Description:"+Description+"\n","apeinsteinz@gmail.com",
                    Email);
        } catch (Exception e) {
            Log.e("error", e.getMessage(), e);
            return "Email Not Sent";
        }
        return "Email Sent";
    }

    @Override
    protected void onPostExecute(String result) {
        Log.e("LongOperation",result+"");
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
}
