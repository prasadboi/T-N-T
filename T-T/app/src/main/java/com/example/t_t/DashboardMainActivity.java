package com.example.t_t;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardMainActivity<customImgButton> extends AppCompatActivity {

    //variables for main dashboard layout and recycler view
    RecyclerView dashBoard;
    List<String> sub;
    List<String> sub1;
    List<String> titles;
    List<String> categoryImages;
    FirebaseUser firebaseUser;
    GridLayoutAdapter gridLayoutAdapter;
    FirebaseAuth fAuth;
    FloatingActionButton categoryAddButton;
    Button btn;
    FirebaseFirestore db;
    TextView test;
    //variable used in selecting a custom picture for the custom category from the local storage
    Context ctx;

    ImageView customCategoryImage,logout;
    String url;
    //custom popup menu variables code here
    private AlertDialog.Builder categoryBuilder;
    private AlertDialog category;
    private EditText newCategoryName,Imageurl;

    private Button categorySaveButton, customImgButton;

    protected void onCreate(Bundle savedInstanceState) {
        final String TAG = DashboardMainActivity.class.getSimpleName();
        ctx = this;
        super.onCreate(savedInstanceState);
        //intantiating the recyclerview of the dashboard
        setContentView(R.layout.dashboard_main);
        dashBoard = findViewById(R.id.recyclerView);
        logout = findViewById(R.id.imageView5);
        fAuth = FirebaseAuth.getInstance();
        customCategoryImage = findViewById(R.id.image);
        db = FirebaseFirestore.getInstance();
        titles = new ArrayList<>();
        test = findViewById(R.id.textView);
        categoryImages = new ArrayList<>();
        sub = new ArrayList<>();
        sub1 = new ArrayList<>();
        btn = findViewById(R.id.button7);
        FirebaseUser firebaseUser = fAuth.getCurrentUser();
        String uid = firebaseUser.getUid();
        db.collection("users").document(uid).collection("Categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Map<String, Object> data3 = new HashMap<>();
                                data3 = document.getData();
                                String name = data3.get("Name").toString();
                                String url = data3.get("ImageUrl").toString();
                                sub.add(name);
                                sub1.add(url);


                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        titles.addAll(sub);
                        categoryImages.addAll(sub1);
                        gridLayoutAdapter = new GridLayoutAdapter(ctx,titles,categoryImages);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(ctx,2,GridLayoutManager.VERTICAL,false);
                        dashBoard.setLayoutManager(gridLayoutManager);
                        dashBoard.setAdapter(gridLayoutAdapter);
                    }
                });


        //declaring a grid layout adapter for the dashboard




        //implementing onclicklistener for the floating button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth.signOut();
                startActivity(new Intent(DashboardMainActivity.this,Login.class));
            }
        });
        categoryAddButton = findViewById(R.id.categoryFloatingButton);
        categoryAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardMainActivity.this,Add_category.class));
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardMainActivity.this,ToDoListActivity.class));
            }
        });


    }


    //@Override
    /*public boolean onCreateOptionsMenu(Menu menu) {
        //this function inflates the main menu and also implements search view
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.logout_action);
        //SearchView searchView = (SearchView) menuItem.getActionView();
        //searchView.setQueryHint("Search Catergories");

        //searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //@Override
            //public boolean onQueryTextSubmit(String query) {
                //return false;
            //}

            //@Override
            //public boolean onQueryTextChange(String newText) {
                //return false;
            //}
        //});
        return true;
    }

    //@Override
    /*public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {//used to define the functionality of the buttons on the menu (log out should be implemented in here)
            case R.id.share_action:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Your Body Here"; //the string here will be shared. can be changed to some other type ig.
                String shareSubject = "Your Subject Here";

                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);

                startActivity(Intent.createChooser(sharingIntent, "Share using"));
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/

    /*public void createNewContactDialog() {
        //implementation of category floating button
        categoryBuilder = new AlertDialog.Builder(this, 0);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.category_popup, null);

        newCategoryName = (EditText) contactPopupView.findViewById(R.id.editTextTextPersonName7);
        categorySaveButton = (Button) contactPopupView.findViewById(R.id.save_button);
        customImgButton = (Button) contactPopupView.findViewById(R.id.button3);
        Imageurl = (EditText) contactPopupView.findViewById(R.id.editTextTextPersonName10);
        categoryBuilder.setView(contactPopupView);
        category = categoryBuilder.create();
        category.show();

        customImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                url = Imageurl.getText().toString();
                //Glide.with(DashboardMainActivity.this).load(url).diskCacheStrategy(DiskCacheStrategy.NONE).into(customCategoryImage);
            }
        });
        categorySaveButton.setOnClickListener(new View.OnClickListener() {
            //to define the action of the save button
            @Override
            public void onClick(View v) {
                String name = newCategoryName.getText().toString().trim();
                //url = Imageurl.getText().toString();


                String userId = firebaseUser.getUid();
                DocumentReference documentReference = db.collection("users").document(userId).collection("Categories").document(name);
                Map<String, Object> categories = new HashMap<>();
                categories.put("Name", name);
                //categories.put("ImageUrl",url);


                documentReference.set(categories).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(DashboardMainActivity.this, "Added " + name + " successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(DashboardMainActivity.this, DashboardMainActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DashboardMainActivity.this, "Error ! couldn't add  " + name + "." + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


            }
        });
    }*/


    }
