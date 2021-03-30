package com.example.t_t;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrackerActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    List<ItemList> mylists = new ArrayList<>();
    RecyclerView trackerRecView;
    String category;
    FloatingActionButton newItemBtn;//new item addition floating button
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    //new item popup dialog variables
    private AlertDialog.Builder itemBuilder;
    private AlertDialog item;
    private EditText newItemName;
    private  EditText newItemCount;
    private EditText newItemUnit;
    private Button addItemBtn;
    SearchView searchView;
    Context ctx;
    ProgressBar progressBar;
    String category1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        TrackerRecViewAdapter myAdapter;
        ctx = this;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        trackerRecView = findViewById(R.id.trackerRecyclerView);
        searchView = findViewById(R.id.search_view);

        Intent data = getIntent();
        category = data.getStringExtra("Category");
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String uid = firebaseUser.getUid();

                if(category!=null) {
                    firebaseFirestore.collection("users").document(uid).collection("Categories").document(category).collection("Items").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override

                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<String, Object> dat = new HashMap<>();
                                    dat = document.getData();
                                    String name = dat.get("ItemName").toString();
                                    int count = Integer.parseInt(dat.get("ItemCount").toString());
                                    String unit = dat.get("ItemUnit").toString();
                                    String url = dat.get("ItemUrl").toString();
                                    ItemList itemList = new ItemList(name, count, unit, url);
                                    mylists.add(itemList);
                                }
                            } else {
                                Toast.makeText(TrackerActivity.this, "Error! " + task.getException(), Toast.LENGTH_LONG).show();
                            }

                            TrackerRecViewAdapter myAdapter = new TrackerRecViewAdapter(ctx, mylists, category);
                            trackerRecView.setAdapter(myAdapter);
                            trackerRecView.setLayoutManager(new LinearLayoutManager(ctx));
                            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String query) {
                                    return false;
                                }

                                @Override
                                public boolean onQueryTextChange(String newText) {
                                    myAdapter.getFilter().filter(newText);
                                    return false;
                                }
                            });
                        }
                    });

                }
                else{
                    Intent intent2 = getIntent();
                    category1 = intent2.getStringExtra("Category");
                    firebaseFirestore.collection("users").document(uid).collection("Categories").document(category1).collection("Items").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override

                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<String, Object> dat = new HashMap<>();
                                    dat = document.getData();
                                    String name = dat.get("ItemName").toString();
                                    int count = Integer.parseInt(dat.get("ItemCount").toString());
                                    String unit = dat.get("ItemUnit").toString();
                                    String url = dat.get("ItemUrl").toString();
                                    ItemList itemList = new ItemList(name, count, unit, url);
                                    mylists.add(itemList);
                                }
                            } else {
                                Toast.makeText(TrackerActivity.this, "Error! " + task.getException(), Toast.LENGTH_LONG).show();
                            }

                            TrackerRecViewAdapter myAdapter = new TrackerRecViewAdapter(ctx, mylists, category);
                            trackerRecView.setAdapter(myAdapter);
                            trackerRecView.setLayoutManager(new LinearLayoutManager(ctx));
                            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String query) {
                                    return false;
                                }

                                @Override
                                public boolean onQueryTextChange(String newText) {
                                    myAdapter.getFilter().filter(newText);
                                    return false;
                                }
                            });
                        }
                    });


                }
        newItemBtn = findViewById(R.id.new_item_floating_button);
        newItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                Intent intent = new Intent(TrackerActivity.this,Add_items.class);
                intent.putExtra("Category",category);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    /*public void createNewItemDialog()
    {
        itemBuilder = new AlertDialog.Builder(this);
        final View itemPopUpView = getLayoutInflater().inflate(R.layout.new_item_popup, null);

        newItemName = (EditText) itemPopUpView.findViewById(R.id.newItemText);
        newItemCount = (EditText) itemPopUpView.findViewById(R.id.newItemUnits);
        newItemUnit = (EditText) itemPopUpView.findViewById(R.id.newItemUnit);
        addItemBtn = (Button) itemPopUpView.findViewById(R.id.addItem);

        itemBuilder.setView(itemPopUpView);
        item = itemBuilder.create();
        item.show();

        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                String uid = user.getUid();
                String name = newItemName.getText().toString().trim();
                String count = newItemCount.getText().toString().trim();
                String unit = newItemUnit.getText().toString().trim();
                Map<String,Object> data = new HashMap<>();
                data.put("ItemName",name);
                data.put("ItemCount",count);
                data.put("ItemUnit",unit);
                firebaseFirestore.collection("users").document(uid).collection(category).document(name).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(TrackerActivity.this,TrackerActivity.class));
                        }
                        else{
                            Toast.makeText(TrackerActivity.this,"Error! "+task.getException(),Toast.LENGTH_LONG);
                        }
                    }
                });


            }
        });
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}