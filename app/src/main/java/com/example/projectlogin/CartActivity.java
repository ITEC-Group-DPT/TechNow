package com.example.projectlogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


import static com.example.projectlogin.Cart.getCartArrList;

public class CartActivity extends MainLayout {
    protected CartListViewAdapter adapter;
    private Intent intent;
    protected ListView lv_cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            noOfItem = bundle.getInt("noItem");
            Log.d("@LOG", "noOfItem in CartActivity: " + String.valueOf(noOfItem));
        }
        initIntent();
    }

    private void initIntent() {
        adapter = new CartListViewAdapter(getBaseContext(), R.layout.cart_listview_layout, getCartArrList());
        adapter.setOnListenerInterface(new CartListViewAdapter.onListener() {
            @Override
            public void onListener() {
                noOfItem--;
                Log.d("@LOG", "noOfItem in CartActivity after remove: " + String.valueOf(noOfItem));
            }
        });
        lv_cart = findViewById(R.id.lv_cart);
        lv_cart.setAdapter(adapter);
    }

    public void submit(View view) {
        if (adapter.getCount() == 0) {
            Toast.makeText(this,"There's nothing here",Toast.LENGTH_LONG).show();
        }
        else {
            for (int i = 0; i < getCartArrList().size(); i++) {
                Product temp =  getCartArrList().get(i);
            }
            intent = new Intent(this, PaymentActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        Log.d("@LOG", "noOfItem in onBackPressed: " + noOfItem);
        returnIntent.putExtra("noItem", noOfItem);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    public void Back_cart(View view) {
        onBackPressed();
    }
}