package com.example.t_t;

import android.os.AsyncTask;
import java.util.Random;

import android.util.Log;





/**


 * Created by GsolC on 2/24/2017.


 */





public class LongOperation extends AsyncTask<Void, Void, String> {
String email;
    Random random_num = new Random();
    long lower_bound = 100000;
    long OTP = lower_bound + random_num.nextInt(900000);

    long passOTP(){
        return OTP;
    }
public LongOperation(String m){
    email = m;
}

    @Override


    protected String doInBackground(Void... params) {


        try {




            GMailSender sender = new GMailSender("jainakshatanil@gmail.com", "Iitpawai@2019");


            sender.sendMail("OTP for verification",


                    "OTP:"+OTP,"jainakshatanil@gmail.com",


                    email)                   ;





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

