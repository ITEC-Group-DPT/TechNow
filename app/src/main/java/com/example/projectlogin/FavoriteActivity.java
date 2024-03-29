package com.example.projectlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class FavoriteActivity extends AppCompatActivity {
    protected ProductListViewAdapter adapter;
    protected ListView lv_favorite;
    private Cart cart = new Cart();
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        refreshLayout = findViewById(R.id.refresh);
        lv_favorite = findViewById(R.id.lv_favorite);

        initComponent();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cart.clearAll();
                initComponent();
                refreshLayout.setRefreshing(false);
            }
        });


    }

    private void initComponent() {
        DatabaseRef.getDatabaseReference().child("Favorite").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Product product = dataSnapshot.getValue(Product.class);
                        cart.addItem(product);
                    }
                }
                adapter = new ProductListViewAdapter(getApplicationContext(), R.layout.product_searchbar, cart.getCartArrList(),true);
                lv_favorite.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        lv_favorite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Intent intent = new Intent(FavoriteActivity.this, ItemDetail.class);
                Product tempProduct = cart.getCartArrList().get(pos);
                intent.putExtra("itemName", tempProduct.getName());
                intent.putExtra("itemType", tempProduct.getType());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    public void Back_favorite(View view) {
        onBackPressed();
    }

}