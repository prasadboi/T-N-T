package com.example.t_t;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GridLayoutAdapter extends RecyclerView.Adapter<GridLayoutAdapter.ViewHolder> {

    private List<String> titles;
    private List<String> categoryImages;
    private Context context;
    LayoutInflater inflater;
    Button categoryAddButton;
    CardView cardView;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    public GridLayoutAdapter( Context ctx, List<String> titles, List<String> img)
    {
        this.titles = titles;
        this.categoryImages = img;
        this.inflater = LayoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = inflater.inflate(R.layout.custom_grid_layout, parent,false);
        ViewHolder holder = new ViewHolder(view);
        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull GridLayoutAdapter.ViewHolder holder, int position) {
        holder.title.setText(titles.get(position));

        Picasso.get().load(categoryImages.get(position)).into(holder.gridIcon);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = user.getUid();
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("users").document(uid).collection("Categories").document(titles.get(position)).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        titles.remove(titles.get(position));
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,getItemCount());
                    }
                });

            }
        });
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,TrackerActivity.class);
                intent.putExtra("Category",titles.get(position));


                context.startActivity(intent);
            }
        });
        /*holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, categoryImages.get(position), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, TrackerActivity.class);
                context.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {

        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        ImageView gridIcon;
        RelativeLayout parentLayout;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textView);
            gridIcon = itemView.findViewById(R.id.imageView);
            parentLayout = itemView.findViewById(R.id.grid_layout);
            cardView = itemView.findViewById(R.id.Clicka);
            context = itemView.getContext();
            imageView = itemView.findViewById(R.id.delete_category);
            //itemView.setOnClickListener(new View.OnClickListener() {
                //@Override
                //public void onClick(View v) {
                    //Intent intent = new Intent(context, TrackerActivity.class);

                    //context.startActivity(intent);
                //}
            //});
        }
    }
}
