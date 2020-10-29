package com.example.projectlogin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class FavoriteListViewAdapter extends ArrayAdapter<Product> {
    private Context context;
    private int layoutID;
    private Cart cart = new Cart();
    private NumberFormat format = new DecimalFormat("#,###");

    public FavoriteListViewAdapter(@NonNull Context context, int resource, @NonNull List<Product> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutID = resource;
        cart.setCartArrList((ArrayList<Product>) objects);
    }

    @Override
    public int getCount() {
        return cart.getCartArrList().size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(layoutID, null, false);
        }
        ImageView avatar = convertView.findViewById(R.id.iv_favorite_ava);
        TextView name = convertView.findViewById(R.id.tv_favorite_name);
        TextView price = convertView.findViewById(R.id.tv_favorite_price);
        final Product temp = cart.getCartArrList().get(position);
        Glide.with(context).load(temp.getAvatarURL()).into(avatar);
        name.setText(temp.getName());
        String formattedTotal = format.format(temp.getPrice()) + "₫";
        price.setText(formattedTotal);
        return convertView;
    }
}
