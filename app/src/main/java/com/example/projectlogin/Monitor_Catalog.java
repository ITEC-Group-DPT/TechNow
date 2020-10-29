package com.example.projectlogin;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
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

public class Monitor_Catalog extends Fragment {

    private ArrayList<Product> monitors;
    private ListView monitor_lv;
    private DatabaseReference reff;
    private View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_catalog, container, false);
        monitor_lv = root.findViewById(R.id.catalog_lv);
        loadData();
        return root;
    }


    private class AsyncTaskMonitor extends AsyncTask<ArrayList, String, String> {
        @Override
        protected String doInBackground(ArrayList... arrayLists) {
            final ArrayList<Product> monitors = arrayLists[0];

            reff = FirebaseDatabase.getInstance().getReference("Products").child("Monitor");
            reff.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Product product = dataSnapshot.getValue(Product.class);
                        product.setType("Monitor");
                        monitors.add(product);
                    }
                    ProductListViewAdapter adapter = new ProductListViewAdapter(getContext(), R.layout.product_listview_layout, monitors);
                    /*adapter.setOnAddtoCartInterface(new ProductListViewAdapter.onAddToCart() {
                        @Override
                        public void onAddToCart(ImageButton imageButtonAddToCart) {
                            imageButtonAddToCart.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.icon_add_to_cart));
                            ((MainUI)getActivity()).cart_btn.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.icon_shake));

                        }
                    });*/


                    monitor_lv.setAdapter(adapter);

                    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Product product = monitors.get(position);
                            Intent intent = new Intent(getContext(), ItemDetail.class);
                            intent.putExtra("itemName", product.getName());
                            intent.putExtra("itemType", product.getType());
                            startActivity(intent);
                        }
                    };

                    monitor_lv.setOnItemClickListener(onItemClickListener);
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
            monitor_lv.setVisibility(View.GONE);
            ProgressBar progressBar = root.findViewById(R.id.progress_catalog);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String command) {
            super.onPostExecute(command);
            if (command != null && command.equals("completed")) {
                monitor_lv.setVisibility(View.VISIBLE);
                ProgressBar progressBar = root.findViewById(R.id.progress_catalog);
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    private void loadData() {
        monitors = new ArrayList<>();
        new AsyncTaskMonitor().execute(monitors);
        /*reff = FirebaseDatabase.getInstance().getReference("Products").child("Monitor");
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Product product = dataSnapshot.getValue(Product.class);
                    product.setType("Monitor");
                    monitors.add(product);
                }
                ProductListViewAdapter adapter = new ProductListViewAdapter(getContext(), R.layout.product_listview_layout, monitors);
                adapter.setOnAddtoCartInterface(new ProductListViewAdapter.onAddToCart() {
                    @Override
                    public void onAddToCart(ImageButton imageButtonAddToCart) {
                        imageButtonAddToCart.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.icon_add_to_cart));
                        ((MainUI)getActivity()).cart_btn.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.icon_shake));

                    }
                });
                monitor_lv = root.findViewById(R.id.catalog_lv);
                monitor_lv.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
    }
}