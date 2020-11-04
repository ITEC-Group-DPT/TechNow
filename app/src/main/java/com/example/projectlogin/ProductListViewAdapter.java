package com.example.projectlogin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


public class ProductListViewAdapter extends ArrayAdapter<Product> {
    private Context context;
    private int layoutID;
    private ArrayList<Product> Products;
    private NumberFormat format = new DecimalFormat("#,###");

    public ProductListViewAdapter(@NonNull Context context, int resource, @NonNull List<Product> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutID = resource;
        this.Products = (ArrayList<Product>) objects;
    }

    @Override
    public int getCount() {
        return Products.size();
    }

    @Nullable
    @Override
    public Product getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(layoutID, null, false);
            convertView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 800));
        } else {
            System.gc();
        }

        ImageView imageView = convertView.findViewById(R.id.iv_ava);
        TextView tv_name = convertView.findViewById(R.id.tv_name);
        RatingBar ratingBar = convertView.findViewById(R.id.rating_bar);
        TextView tv_price = convertView.findViewById(R.id.tv_price);

        Product temp = Products.get(position);

        Glide.with(context).load(temp.getAvatarURL()).into(imageView);
        tv_name.setText(temp.getName());

        ratingBar.setRating(Float.parseFloat(String.format("%.1f", temp.getRating())));

        String formattedPrice = format.format(temp.getPrice()) + "₫";
        tv_price.setText(formattedPrice);


        return convertView;
    }
}