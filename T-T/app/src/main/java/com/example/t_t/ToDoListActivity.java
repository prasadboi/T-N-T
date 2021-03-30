package com.example.t_t;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToDoListActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    List<String> time = new ArrayList<>();
    List<String> date = new ArrayList<>();
    List<String> taskNames = new ArrayList<>();
    List<String> taskCustomMsg = new ArrayList<>();
    RecyclerView toDoList;

    Context ctx;
    Button newTaskBtn;

    private AlertDialog.Builder taskBuilder;
    private AlertDialog task;
    private EditText newTaskName;
    private TimePicker newTimePicker;
    private DatePicker newDatePicker;
    private EditText newCustomMsg;
    private Button addTaskButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        firebaseAuth  =FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        toDoList = findViewById(R.id.toDoRecyclerView);
        ctx = this;
        String uid = user.getUid();
         firebaseFirestore.collection("users").document(uid).collection("To_Do_List").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
             @Override
             public void onComplete(@NonNull Task<QuerySnapshot> task) {
             if(task.isSuccessful()){
                 for (QueryDocumentSnapshot document : task.getResult()){
                     Map<String,Object> dat = new HashMap<>();
                     dat = document.getData();
                     String task1 = dat.get("Task").toString();
                     String time1 = dat.get("Time").toString();
                     String date1 = dat.get("Date").toString();
                     String desp1 = dat.get("Description").toString();
                     taskNames.add(task1);
                     taskCustomMsg.add(desp1);
                     time.add(time1);
                     date.add(date1);

                 }
                 ToDoListRecViewAdapter tDLAdapter = new ToDoListRecViewAdapter(ctx, taskNames,time,date);
                 toDoList.setAdapter(tDLAdapter);
                 toDoList.setLayoutManager(new LinearLayoutManager(ctx));
             }

             else{
                 Toast.makeText(ToDoListActivity.this,"Error "+task.getException(),Toast.LENGTH_LONG).show();
             }

             }
         });




        newTaskBtn = findViewById(R.id.newTaskButton);
        newTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(ToDoListActivity.this,Add_events.class));
            }
        });

    }

    //public void createNewTaskDialog()
    //{
        //taskBuilder = new AlertDialog.Builder(this);
        //final View taskPopUpView = getLayoutInflater().inflate(R.layout.new_task_popup, null);

        //newTaskName = (EditText) taskPopUpView.findViewById(R.id.taskName);
        //newTimePicker = (TimePicker) taskPopUpView.findViewById(R.id.timeSelector);
        //newDatePicker = (DatePicker) taskPopUpView.findViewById(R.id.dateSelector);
        //newCustomMsg = (EditText) taskPopUpView.findViewById(R.id.customMsg);
        //addTaskButton = (Button) taskPopUpView.findViewById(R.id.taskAddBtn);

        //taskBuilder.setView(taskPopUpView);
        //task = taskBuilder.create();
        //task.show();


        //addTaskButton.setOnClickListener(new View.OnClickListener() {
            //@RequiresApi(api = Build.VERSION_CODES.M)
            //public void onClick(View v) {
                //taskNames.add(newTaskName.getText().toString());
                //taskCustomMsg.add(newCustomMsg.getText().toString());

                //Calendar calendar = Calendar.getInstance();
                //calendar.set(
                        //calendar.get(Calendar.YEAR),
                        //calendar.get(Calendar.MONTH),
                        //calendar.get(Calendar.DAY_OF_MONTH),
                        //newTimePicker.getHour(),
                        //newTimePicker.getMinute(),
                        //0
                //);
                //task.dismiss();

            //}
        //});
    }

//}