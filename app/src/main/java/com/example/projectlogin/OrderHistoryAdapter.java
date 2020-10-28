package com.example.projectlogin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class OrderHistoryAdapter extends ArrayAdapter<Product> {
    private Context context;
    private int layoutID;
    private OrderHistory orderHistory = new OrderHistory();
    private Cart cart = new Cart();
    private NumberFormat format = new DecimalFormat("#,###");

    public OrderHistoryAdapter(@NonNull Context context, int resource, @NonNull List<Product> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutID = resource;
        cart.setCartArrList((ArrayList<Product>) objects);
    }

    @Override
    public int getCount() {
        return cart.getNoOfItem();
        //return orderHistory.getOrderHistory().size();
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(layoutID, null, false);
        }

        ImageView iv_order_ava = convertView.findViewById(R.id.iv_order_ava);
        TextView tv_orderID = convertView.findViewById(R.id.tv_shipping_status);
        final TextView tv_order_name = convertView.findViewById(R.id.tv_order_name);
        final TextView tv_order_quantity_price = convertView.findViewById(R.id.tv_order_quantity_price);

        //final Cart tempCart = orderHistory.getOrderHistory().get(position);

        final Product tempProduct = cart.getCartArrList().get(position);
        Glide.with(context).load(tempProduct.getAvatarURL()).into(iv_order_ava);
        tv_order_name.setText(tempProduct.getName());


        int price = tempProduct.getQuantity() * tempProduct.getPrice();
        String formatedTotal = "Quantity: " + tempProduct.getQuantity() + " | $" + format.format(price) + "₫";
        tv_order_quantity_price.setText(formatedTotal);

        /*final DatabaseReference databaseReference = DatabaseRef.getDatabaseReference().child("Order History");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.child(tempCart.getID()).getChildren()) {
                    if (dataSnapshot.getValue(Product.class).getName().equals(tempProduct.getName())) {
                        int quantity = dataSnapshot.child("quantity").getValue(Integer.class);
                        int price = quantity * dataSnapshot.child("price").getValue(Integer.class);
                        String formatedTotal = "Quantity: " + quantity + " | $" + format.format(price) + "₫";
                        tv_order_quantity_price.setText(formatedTotal);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });*/
        tv_orderID.setText(tempProduct.getOrderID());
        Button btn_order_detail = convertView.findViewById(R.id.btn_order_detail);
        btn_order_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Button btn_rebuy = convertView.findViewById(R.id.btn_rebuy);
        btn_rebuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ItemDetail.class);
                intent.putExtra("itemName", tempProduct.getName());
                intent.putExtra("itemType", tempProduct.getType());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        TextView tv_review = convertView.findViewById(R.id.tv_review);
        tv_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ReviewPurchasedProduct.class);
                intent.putExtra("P_Name", tempProduct.getName());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
