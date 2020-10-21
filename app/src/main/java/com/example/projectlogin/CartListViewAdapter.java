package com.example.projectlogin;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.projectlogin.Cart.getCartArrList;
import static com.example.projectlogin.Cart.setCartArrList;

public class CartListViewAdapter extends ArrayAdapter<Product> {
    private Context context;
    private int layoutID;
    private NumberFormat format = new DecimalFormat("#,###");
    onListener myListener;

    public CartListViewAdapter(@NonNull Context context, int resource, @NonNull List<Product> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutID = resource;
        setCartArrList((ArrayList<Product>) objects);
    }

    @Override
    public int getCount() {
        return getCartArrList().size();
    }

    public interface onListener {
        void onListener();
    }

    public void setOnListenerInterface(onListener callback) {
        myListener = callback;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(layoutID, null, false);
        }
        Log.d("!!@@", "position = " + position);
        ImageView ava = convertView.findViewById(R.id.iv_cart_ava);
        TextView name = convertView.findViewById(R.id.tv_cart_name);
        TextView price = convertView.findViewById(R.id.tv_cart_price);
        ImageButton remove = convertView.findViewById(R.id.remove_product);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product temp1 = getCartArrList().get(position);
                for (int i = 0; i < getCartArrList().size(); i++) {
                    Product temp2 =  getCartArrList().get(i);
                    if (temp1.getName().equals(temp2.getName())) {
                        getCartArrList().remove(i);
                        notifyDataSetChanged();
                        myListener.onListener();
                    }
                }
            }
        });
      /*  final TextView quantity = convertView.findViewById(R.id.tv_cart_quantity); // TODO

        ImageButton sub_quantity = convertView.findViewById(R.id.sub_quantity_btn);
        final Product temp =  getCartArrList().get(position);
        quantity.setText(Integer.toString(temp.getQuantity()));

        temp.setQuantity(Integer.parseInt(quantity.getText().toString()));
        ava.setImageBitmap(temp.getAvatar());
        name.setText(temp.getName());
        String formatedTotal = format.format(temp.getPrice()) + "₫";
        price.setText("Price: " + formatedTotal);
        sub_quantity.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (temp.getQuantity() > 1) {
                    temp.setQuantity(temp.getQuantity() - 1);
                    quantity.setText(String.valueOf(temp.getQuantity()));
                }
            }
        });
        ImageButton plus_quantity = convertView.findViewById(R.id.plus_quantity_btn);
        plus_quantity.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp.setQuantity(temp.getQuantity() + 1);
                quantity.setText(String.valueOf(temp.getQuantity()));
                Log.d("!!@@", "quantity = " + temp.getQuantity());
            }
        });*/
        return convertView;
    }
}