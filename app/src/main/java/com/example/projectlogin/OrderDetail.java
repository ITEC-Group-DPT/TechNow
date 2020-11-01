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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class OrderDetail extends AppCompatActivity {

    private LinearLayout linearLayout;
    private TextView order_id;
    private TextView order_date;
    private TextView cus_nam;
    private TextView cus_phone;
    private TextView cus_address;

    private String Order_id;
    private ArrayList<Product> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        linearLayout = findViewById(R.id.lnlo_order);
        order_id = findViewById(R.id.detail_order_id);
        order_date = findViewById(R.id.detail_order_date);
        cus_nam = findViewById(R.id.detail_customer_name);
        cus_phone = findViewById(R.id.detail_customer_phonenum);
        cus_address = findViewById(R.id.detail_customer_address);
        loadData();
    }

    private class AsyncTaskOrderDetail extends AsyncTask<String, String, String>
    {

        @Override
        protected String doInBackground(String... strings) {

            DatabaseRef.getDatabaseReference().child("Order History").child(Order_id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot dataSnapshot: snapshot.getChildren())
                    {
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

                    for (int i = 0; i < products.size();i++) {
                        View view = LayoutInflater.from(OrderDetail.this).inflate(R.layout.product_lnlo_orderdetail, null, false);
                        ImageView product_pic = view.findViewById(R.id.order_pic);
                        TextView product_name = view.findViewById(R.id.name_product);
                        TextView product_price_qty = view.findViewById(R.id.price_qty);

                        final Product product = products.get(i);

                        Glide.with(getApplicationContext()).load(product.getAvatarURL()).into(product_pic);
                        product_name.setText(product.getName());

                        NumberFormat format = new DecimalFormat("#,###");
                        String formattedPrice = format.format(product.getPrice()) + " ₫";
                        product_price_qty.setText(formattedPrice + " x" + product.getQuantity());

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

            if (s != null && s.equals("Completed"))
            {
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
    public void Back_favorite(View view) {
        onBackPressed();
    }
}