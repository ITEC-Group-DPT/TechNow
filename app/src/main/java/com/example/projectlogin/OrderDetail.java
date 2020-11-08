package com.example.projectlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.EventListener;

public class OrderDetail extends AppCompatActivity {

    private LinearLayout linearLayout;
    private TextView order_id;
    private TextView order_date;
    private TextView cus_nam;
    private TextView cus_phone;
    private TextView cus_address;
    private TextView notional_price;
    private TextView shipping_fees;
    private TextView total_cash;

    private String Order_id;
    private ArrayList<Product> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        initComponents();
        loadData();
    }

    private void initComponents() {
        linearLayout = findViewById(R.id.lnlo_order);
        order_id = findViewById(R.id.detail_order_id);
        order_date = findViewById(R.id.detail_order_date);
        cus_nam = findViewById(R.id.detail_customer_name);
        cus_phone = findViewById(R.id.detail_customer_phonenum);
        cus_address = findViewById(R.id.detail_customer_address);
        notional_price = findViewById(R.id.notional_price);
        shipping_fees = findViewById(R.id.shipping_fee);
        total_cash = findViewById(R.id.total_price);
    }


    private class AsyncTaskOrderDetail extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            DatabaseRef.getDatabaseReference().child("Order History").child(Order_id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.getKey().equals("Customer")) continue;
                        Product product = dataSnapshot.getValue(Product.class);
                        products.add(product);
                    }
                    DataSnapshot Customer = snapshot.child("Customer");

                    order_id.setText(Order_id);
                    order_date.setText("Order date: " + (String) Customer.child("Date").getValue());
                    cus_nam.setText((String) Customer.child("Name").getValue());
                    cus_phone.setText((String) Customer.child("Phone Number").getValue());
                    cus_address.setText((String) Customer.child("Address").getValue());

                    NumberFormat format = new DecimalFormat("#,###");

                    int notional = Customer.child("Notional Price").getValue(Integer.class);
                    String temp1 = format.format(notional) + "đ";
                    temp1 = temp1.replace(',','.');
                    notional_price.setText(temp1);

                    int shipping = Customer.child("Shipping Fee").getValue(Integer.class);
                    String temp2 = format.format(shipping) + "đ";
                    temp2 = temp2.replace(',','.');
                    shipping_fees.setText(temp2);

                    String temp3 = format.format(notional + shipping) + "đ";
                    temp3 = temp3.replace(',','.');
                    total_cash.setText(temp3);


                    for (int i = 0; i < products.size(); i++) {
                        View view = LayoutInflater.from(OrderDetail.this).inflate(R.layout.product_lnlo_orderdetail, null, false);
                        ImageView product_pic = view.findViewById(R.id.order_pic);
                        TextView product_name = view.findViewById(R.id.name_product);
                        TextView product_price_qty = view.findViewById(R.id.price_qty);

                        final Product product = products.get(i);

                        Glide.with(getApplicationContext()).load(product.getAvatarURL()).into(product_pic);
                        product_name.setText(product.getName());

                        String formattedPrice = format.format(product.getPrice()) + "₫";
                        formattedPrice = formattedPrice.replace(',', '.');
                        product_price_qty.setText(formattedPrice + " x " + product.getQuantity());

                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(OrderDetail.this, ItemDetail.class);
                                intent.putExtra("itemName", product.getName());
                                intent.putExtra("itemType", product.getType());
                                startActivity(intent);
                            }
                        });
                        linearLayout.addView(view);
                    }

                    onPostExecute("Completed");
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            return null;
        }

        @Override
        protected void onPreExecute() {
            ProgressBar progressBar = findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.VISIBLE);

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null && s.equals("Completed")) {
                ProgressBar progressBar = findViewById(R.id.progress_bar);
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    private void loadData() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Order_id = bundle.getString("Order_id");
        }

        new AsyncTaskOrderDetail().execute("");

    }

    public void Rebuy(View view) {
        final DatabaseReference database = DatabaseRef.getDatabaseReference().child("Cart");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (int i = 0; i < products.size(); i++) {
                    Product product = products.get(i);
                    product.setQuantity(1);

                    DataSnapshot dataSnapshot = snapshot.child(product.getName());
                    if (dataSnapshot.exists()) {
                        int quantity = dataSnapshot.child("quantity").getValue(Integer.class);
                        database.child(product.getName()).child("quantity").setValue(quantity + 1);
                    } else {
                        database.child(product.getName()).setValue(product);
                    }

                }
                startActivity(new Intent(OrderDetail.this, CartActivity.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void Back_favorite(View view) {
        onBackPressed();
    }
}