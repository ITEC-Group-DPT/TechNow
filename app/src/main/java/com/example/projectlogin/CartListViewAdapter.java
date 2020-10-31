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

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


public class CartListViewAdapter extends ArrayAdapter<Product> {
    private Context context;
    private int layoutID;
    private Cart cart = new Cart();
    private NumberFormat format = new DecimalFormat("#,###");
    private onRemoveItemFromCart mListner;

    public CartListViewAdapter(@NonNull Context context, int resource, @NonNull List<Product> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutID = resource;
        cart.setCartArrList((ArrayList<Product>) objects);
    }

    @Override
    public int getCount() {
        return cart.getCartArrList().size();
    }

    public interface onRemoveItemFromCart {
        void onRemoveItemFromCart();
    }

    public void setOnRemoveItemFromCart(onRemoveItemFromCart callback){
        mListner = callback;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(layoutID, null, false);
        }

        ImageView ava = convertView.findViewById(R.id.iv_cart_ava);
        TextView name = convertView.findViewById(R.id.tv_cart_name);
        TextView price = convertView.findViewById(R.id.tv_cart_price);
        final TextView quantity = convertView.findViewById(R.id.tv_cart_quantity);

        final Product temp = cart.getCartArrList().get(position);

        Glide.with(context).load(temp.getAvatarURL()).into(ava);
        name.setText(temp.getName());

        final DatabaseReference databaseReference = DatabaseRef.getDatabaseReference().child("Cart");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.getValue(Product.class).getName().equals(temp.getName())) {
                        quantity.setText(String.valueOf(dataSnapshot.child("quantity").getValue(Integer.class)));
                        temp.setQuantity(Integer.parseInt(quantity.getText().toString()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        String formattedTotal = format.format(temp.getPrice()) + "₫";
        price.setText(formattedTotal);

        ImageButton sub_quantity = convertView.findViewById(R.id.sub_quantity_btn);
        sub_quantity.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (temp.getQuantity() > 1) {
                    temp.setQuantity(temp.getQuantity() - 1);
                    quantity.setText(String.valueOf(temp.getQuantity()));
                    databaseReference.child(temp.getName()).child("quantity").setValue(temp.getQuantity());
                }
                CartActivity.setTV_totalCash(cart.calTotalCash());
            }
        });

        ImageButton plus_quantity = convertView.findViewById(R.id.plus_quantity_btn);
        plus_quantity.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp.setQuantity(temp.getQuantity() + 1);
                quantity.setText(String.valueOf(temp.getQuantity()));
                databaseReference.child(temp.getName()).child("quantity").setValue(temp.getQuantity());
                Log.d("!!@@", "quantity = " + temp.getQuantity());
                CartActivity.setTV_totalCash(cart.calTotalCash());
            }
        });

        ImageButton remove = convertView.findViewById(R.id.remove_product);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product temp1 = cart.getCartArrList().get(position);
                for (int i = 0; i < cart.getCartArrList().size(); i++) {
                    Product temp2 = cart.getCartArrList().get(i);
                    if (temp1.getName().equals(temp2.getName())) {
                        cart.getCartArrList().remove(i);
                        notifyDataSetChanged();
                        databaseReference.child(temp1.getName()).removeValue();
                        mListner.onRemoveItemFromCart();
                    }
                }
                CartActivity.setTV_totalCash(cart.calTotalCash());
            }
        });
        return convertView;
    }
}