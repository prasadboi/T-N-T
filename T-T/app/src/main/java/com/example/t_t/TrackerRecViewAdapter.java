package com.example.t_t;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.grpc.okhttp.internal.Util;

public class TrackerRecViewAdapter extends RecyclerView.Adapter<TrackerRecViewAdapter.MyViewHolder> implements Filterable {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    List<ItemList> myCustomList;
    List<ItemList> copy;
    Context trackerContext;
    String category;

    Button add_item;
    public TrackerRecViewAdapter(Context ct, List<ItemList> list,String category1){
        trackerContext = ct;
        myCustomList = list;
        category = category1;
        copy = new ArrayList<>(myCustomList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(trackerContext);
        View trackerView = inflater.inflate(R.layout.tracker_layout, parent, false);
        return new MyViewHolder(trackerView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ItemList mylist = myCustomList.get(position);
        holder.text1.setText(mylist.getText());
        holder.text2.setText(String.valueOf(mylist.getQuantity()));
        holder.text3.setText(mylist.getUnits());
        Picasso.get().load(mylist.getItemUrl()).into(holder.img3);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = user.getUid();

        holder.bShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.get().load(mylist.getItemUrl()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Uri bmpUri = null;
                        try{
                            File file = new File(trackerContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES),"share_image_"+System.currentTimeMillis()+".png");
                            FileOutputStream out = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG,90,out);
                            out.close();
                            bmpUri = FileProvider.getUriForFile(trackerContext,BuildConfig.APPLICATION_ID+".provider",file);
                        }catch(IOException e){
                            e.printStackTrace();
                        }

                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("image/*");

                        sharingIntent.putExtra(Intent.EXTRA_TEXT, "Item: "+mylist.getText()+"\n"+"Quantity: " + mylist.getQuantity() + " " + mylist.getUnits());
                        sharingIntent.putExtra(Intent.EXTRA_STREAM,bmpUri );
                        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        trackerContext.startActivity(Intent.createChooser(sharingIntent, "Share using"));
                    }



                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            }
        });
        holder.bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("users").document(uid).collection("Categories").document(category).collection("Items").document(mylist.getText()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            myCustomList.remove(mylist);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position,getItemCount());
                        }
                    }
                });
            }
        });
        holder.bIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer n = mylist.getQuantity();
                n++;

                Integer finalN = n;
                firebaseFirestore.collection("users").document(uid).collection("Categories").document(category).collection("Items").document(mylist.getText()).update("ItemCount",n).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mylist.setQuantity(finalN);
                            holder.text2.setText(String.valueOf(mylist.getQuantity()));
                        }
                        else{
                            Toast.makeText(trackerContext,task.getException().toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                });


            }
        });

        holder.bDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer n = mylist.getQuantity();
                if(n>=1)
                    n--;
                final Integer j = n;
                Integer finalN = n;
                firebaseFirestore.collection("users").document(uid).collection("Categories").document(category).collection("Items").document(mylist.getText()).update("ItemCount",n).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mylist.setQuantity(finalN);
                            holder.text2.setText(String.valueOf(mylist.getQuantity()));
                        }
                        else{
                            Toast.makeText(trackerContext,task.getException().toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return myCustomList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private  Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
          List<ItemList> filteredList = new ArrayList<>();
          if(constraint == null || constraint.length() == 0){
              filteredList.addAll(copy);
          }else{
              String filterPattern = constraint.toString().toLowerCase().trim();
              for(ItemList item : copy){
                  if(item.getText().toLowerCase().contains(filterPattern)){
                      filteredList.add(item);
                  }
              }
          }
            FilterResults results = new FilterResults();
          results.values = filteredList;
          return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            myCustomList.clear();
            myCustomList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView text1;
        TextView text2;
        TextView text3;
        ImageView img3;
        ImageView img1;
        ImageView img2;
        Button bIncrease, bDecrease;
        ImageView bDelete,bShare;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            text1 = itemView.findViewById(R.id.item_name);
            text2 = itemView.findViewById(R.id.item_count);
            text3  = itemView.findViewById(R.id.item_units);

            img3 = itemView.findViewById(R.id.imageView4);
            bIncrease = itemView.findViewById(R.id.increase_button);
            bDecrease = itemView.findViewById(R.id.decrease_button);
            bDelete = itemView.findViewById(R.id.remove_btn);
            add_item = itemView.findViewById(R.id.new_item_floating_button);
            bShare = itemView.findViewById(R.id.share_btn);
        }
    }
}