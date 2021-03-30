package com.example.t_t;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.StructuredQuery;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Add_events extends AppCompatActivity {
    EditText customMsg,title;
    DatePicker date;
    TimePicker time;
    Button add;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_task_popup);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        customMsg = findViewById(R.id.customMsg);
        title = findViewById(R.id.taskName);
        date = findViewById(R.id.dateSelector);
        time = findViewById(R.id.timeSelector);
        add = findViewById(R.id.taskAddBtn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = customMsg.getText().toString();
                String task1 = title.getText().toString();
                String date1 = date.getDayOfMonth()+"-"+date.getMonth()+"-"+date.getYear();
                String time1 = time.getHour()+":"+time.getMinute();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                String uid = user.getUid();
                Map<String,Object> data = new HashMap<>();
                data.put("Task",task1);
                data.put("Description",description);
                data.put("Time",time1);
                data.put("Date",date1);

                firebaseFirestore.collection("users").document(uid).collection("To_Do_List").document(task1).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            firebaseFirestore.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot document = task.getResult();
                                    Map<String,Object> dat = new HashMap<>();
                                    dat = document.getData();
                                    String username = dat.get("Username").toString();
                                    String email = dat.get("Email").toString();
                                    String phone = dat.get("Phone").toString();
                                    String profession = dat.get("Profession").toString();
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.set(date.getYear(),date.getMonth(),date.getDayOfMonth(),time.getHour(),time.getMinute(),0);
                                    long time = calendar.getTimeInMillis();
                                    long time2 = time - TimeUnit.HOURS.toMillis(1);


                                    setAlarm(time2,email,username,task1,date1,time1,description);



                                    startActivity(new Intent(Add_events.this,ToDoListActivity.class));
                                }
                            });










                        }
                        else{
                            Toast.makeText(Add_events.this,"Error "+task.getException(),Toast.LENGTH_LONG);
                        }
                    }

                    private void setAlarm(long timeInMillis,String mail,String username,String task,String date,String time,String desp ) {
                        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                        //creating a new intent specifying the broadcast receiver
                        Intent i = new Intent(Add_events.this, MyAlarm.class);
                        i.putExtra("Email",mail);
                        i.putExtra("Username",username);
                        i.putExtra("Task",task);
                        i.putExtra("Date",date);
                        i.putExtra("Time",time);
                        i.putExtra("Description",desp);
                        //creating a pending intent using the intent
                        PendingIntent pi = PendingIntent.getBroadcast(Add_events.this, 0, i, 0);
                        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,timeInMillis,pi);

                    }
                });
            }
        });
    }
}