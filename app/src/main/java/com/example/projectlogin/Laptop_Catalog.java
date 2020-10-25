package com.example.projectlogin;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Laptop_Catalog extends Fragment {

    private ArrayList<Product> laptops;
    private ListView laptop_lv;
    DatabaseReference reff;
    View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_catalog,container, false);

        laptop_lv = root.findViewById(R.id.catalog_lv);
        loadData();
        return root;
    }

    private class AsyncTaskLaptop extends AsyncTask<ArrayList,String,String>
    {

        @Override
        protected String doInBackground(ArrayList... arrayLists) {
            final ArrayList<Product> laptops = arrayLists[0];

            reff = FirebaseDatabase.getInstance().getReference("Products").child("Laptop");
            reff.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Product product = dataSnapshot.getValue(Product.class);
                        product.setType("Laptop");
                        laptops.add(product);
                    }
                    ProductListViewAdapter adapter = new ProductListViewAdapter(getContext(), R.layout.product_listview_layout, laptops);
                    adapter.setOnAddtoCartInterface(new ProductListViewAdapter.onAddToCart() {
                        @Override
                        public void onAddToCart(ImageButton imageButtonAddToCart) {
                            imageButtonAddToCart.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.icon_add_to_cart));
                            ((MainUI)getActivity()).cart_btn.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.icon_shake));

                        }
                    });
                    laptop_lv.setAdapter(adapter);
                    onPostExecute("completed");

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            laptop_lv.setVisibility(View.GONE);
            ProgressBar progressBar = root.findViewById(R.id.progress_catalog);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String command) {
            super.onPostExecute(command);

            if(command != null && command.equals("completed")){
                laptop_lv.setVisibility(View.VISIBLE);
                ProgressBar progressBar = root.findViewById(R.id.progress_catalog);
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    private void loadData() {
        laptops = new ArrayList<>();
        new AsyncTaskLaptop().execute(laptops);
        /*reff = FirebaseDatabase.getInstance().getReference("Products").child("Laptop");
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Product product = dataSnapshot.getValue(Product.class);
                    product.setType("Laptop");
                    laptops.add(product);
                }
                ProductListViewAdapter adapter = new ProductListViewAdapter(getContext(), R.layout.product_listview_layout, laptops);
                adapter.setOnAddtoCartInterface(new ProductListViewAdapter.onAddToCart() {
                    @Override
                    public void onAddToCart(ImageButton imageButtonAddToCart) {
                        imageButtonAddToCart.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.icon_add_to_cart));
                        ((MainUI)getActivity()).cart_btn.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.icon_shake));

                    }
                });

                laptop_lv = root.findViewById(R.id.catalog_lv);
                laptop_lv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
    }


}