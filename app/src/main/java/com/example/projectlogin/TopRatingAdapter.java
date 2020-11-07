package com.example.projectlogin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class TopRatingAdapter extends RecyclerView.Adapter<TopRatingAdapter.MyViewHolder> {
    private ArrayList<Product> productList;
    private Context context;
    private NumberFormat format = new DecimalFormat("#,###");

    public TopRatingAdapter(@NonNull Context context, ArrayList<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_rating_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product temp = productList.get(position);
        Glide.with(context).load(temp.getAvatarURL()).into(holder.avatar);
        Glide.with(context).load("https://firebasestorage.googleapis.com/v0/b/technow-4b3ab.appspot.com/o/UI%2Ftop_rating_icon.png?alt=media&token=c72be258-7748-4c8c-af01-118517c2486e").into(holder.topRatingIV);
        holder.nameTV.setText(temp.getName());
        String formattedPrice = format.format(temp.getPrice()) + "₫";
        formattedPrice = formattedPrice.replace(',', '.');
        holder.priceTV.setText(formattedPrice);
        Log.d("@@@LOG", String.valueOf(temp.getRating()));
        holder.ratingBar.setRating(Float.parseFloat(String.format("%.1f", temp.getRating())));

        holder.setItemClickListener(new ReyclerViewItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Product product = productList.get(position);
                Intent intent = new Intent(context, ItemDetail.class);
                intent.putExtra("itemName", product.getName());
                intent.putExtra("itemType", product.getType());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public interface ReyclerViewItemClickListener {
        void onClick(View view, int position, boolean isLongClick);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView avatar;
        private ImageView topRatingIV;
        private TextView nameTV;
        private TextView priceTV;
        private RatingBar ratingBar;
        private ReyclerViewItemClickListener itemClickListener;

        MyViewHolder(View view) {
            super(view);
            avatar = view.findViewById(R.id.ava_iv);
            topRatingIV = view.findViewById(R.id.top_rating_icon);
            nameTV = view.findViewById(R.id.name_tv);
            priceTV = view.findViewById(R.id.price_tv);
            ratingBar = view.findViewById(R.id.rating_bar);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ReyclerViewItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }
    }
}
