package com.example.t_t;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Add_items extends AppCompatActivity {
    EditText name,count,unit,url;
    Button addItemBtn,addImageBtn,btn;
    ImageView image;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String username;
    String profession;
    String phone;
    String email;
    String category;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_item_popup);
        Intent data = getIntent();
        category = data.getStringExtra("Category");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = user.getUid();

        btn = findViewById(R.id.new_item_floating_button);
        name = findViewById(R.id.newItemText);
        count = findViewById(R.id.newItemUnits);
        unit = findViewById(R.id.newItemUnit);
        url = findViewById(R.id.editTextTextPersonName5);
        image = findViewById(R.id.imageView2);
        addImageBtn = findViewById(R.id.button6);
        addItemBtn = findViewById(R.id.addItem);
        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imageUrl = url.getText().toString();
                Picasso.get().load(imageUrl).into(image);
            }
        });
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                String uid = user.getUid();
                String name1 = name.getText().toString().trim();
                String count1 = count.getText().toString().trim();
                String unit1 = unit.getText().toString().trim();
                String url1 = url.getText().toString().trim();
                Map<String,Object> data = new HashMap<>();
                data.put("ItemName",name1);
                data.put("ItemCount",count1);
                data.put("ItemUnit",unit1);
                data.put("ItemUrl",url1);
                firebaseFirestore.collection("users").document(uid).collection("Categories").document(category).collection("Items").document(name1).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(Add_items.this,TrackerActivity.class);
                            intent.putExtra("Category",category);
                            startActivity(intent);

                        }
                        else{
                            Toast.makeText(Add_items.this,"Error! "+task.getException(),Toast.LENGTH_LONG);
                        }
                    }
                });


            }
        });
    }
}