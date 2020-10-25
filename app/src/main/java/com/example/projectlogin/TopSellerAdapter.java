package com.example.projectlogin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class TopSellerAdapter extends RecyclerView.Adapter<TopSellerAdapter.MyViewHolder> {
    private ArrayList<Product> productList;
    private Context context;
    private NumberFormat format = new DecimalFormat("#,###");

    public TopSellerAdapter(@NonNull Context context, ArrayList<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_seller_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product temp = productList.get(position);
        Glide.with(context).load(temp.getAvatarURL()).into(holder.avatar);
        Glide.with(context).load("https://firebasestorage.googleapis.com/v0/b/technow-4b3ab.appspot.com/o/UI%2FbestSeller1.png?alt=media&token=02429f59-88c1-4124-85d6-d086f207345a").into(holder.topSellerIV);
        holder.nameTV.setText(temp.getName());
        String formattedPrice = "$" + format.format(temp.getPrice()) + "₫";
        holder.priceTV.setText(formattedPrice);
        holder.soldTV.setText("Sold: " + temp.getSold());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        ImageView topSellerIV;
        TextView nameTV;
        TextView priceTV;
        TextView soldTV;

        MyViewHolder(View view) {
            super(view);
            avatar = view.findViewById(R.id.ava_iv);
            topSellerIV = view.findViewById(R.id.top_seller_icon);
            nameTV = view.findViewById(R.id.name_tv);
            priceTV = view.findViewById(R.id.price_tv);
            soldTV = view.findViewById(R.id.sold_tv);
        }
    }
}
