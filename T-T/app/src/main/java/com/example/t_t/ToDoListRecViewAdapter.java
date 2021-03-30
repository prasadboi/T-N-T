package com.example.t_t;



import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;


public class ToDoListRecViewAdapter extends RecyclerView.Adapter<ToDoListRecViewAdapter.ViewHolder> {

    List<String> taskTitles;
    List<String> taskTimes;
    List<String> taskDates;
    Context context;
    LayoutInflater inflater;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    public ToDoListRecViewAdapter(Context ctx, List<String> list,List<String> list1,List<String> list2)
    {
        taskTitles = list;
        taskTimes = list1;
        taskDates = list2;
        context = ctx;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.trigger_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.text1.setText(taskTitles.get(position));
        holder.dateText.setText(taskDates.get(position));
        holder.timeText.setText(taskTimes.get(position));
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = user.getUid();
        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("users").document(uid).collection("To_Do_List").document(taskTitles.get(position)).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            taskTitles.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position,getItemCount());
                        }
                    }
                });
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,details.class);
                intent.putExtra("Event",taskTitles.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskTitles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView text1;
        ImageView editImg;
        ImageView deleteImg;
        TextView dateText;
        TextView timeText;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            text1 = itemView.findViewById(R.id.taskText);
            dateText = itemView.findViewById(R.id.taskText2);
            timeText = itemView.findViewById(R.id.taskText3);
            cardView = itemView.findViewById(R.id.card_trigger);
            deleteImg = itemView.findViewById(R.id.trashBtn);
        }
    }
}

