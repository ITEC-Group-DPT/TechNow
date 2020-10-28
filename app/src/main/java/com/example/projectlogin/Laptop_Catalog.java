package com.example.projectlogin;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
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
    private DatabaseReference reff;
    private EditText etSearch;
    private View root;
    private ProductListViewAdapter a;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_catalog,container, false);
        laptop_lv = root.findViewById(R.id.catalog_lv);
        etSearch = root.findViewById(R.id.etSearch);

        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                int textlength = cs.length();
                ArrayList<Product> tempArrayList = new ArrayList<Product>();
                for (int i = 0; i < laptops.size(); i++) {
                    if (textlength<= laptops.get(i).getName().length())
                    {
                        if (laptops.get(i).getName().toLowerCase().contains(cs.toString().toLowerCase()))
                        {
                            tempArrayList.add(laptops.get(i));
                        }
                    }
                }
                ProductListViewAdapter adapter = new ProductListViewAdapter(getContext(), R.layout.product_listview_layout, tempArrayList);
                laptop_lv.setAdapter(adapter);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

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
                    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Product product = laptops.get(position);
                            Intent intent = new Intent(getContext(), ItemDetail.class);
                            intent.putExtra("itemName", product.getName());
                            intent.putExtra("itemType", product.getType());
                            startActivity(intent);
                        }
                    };

                    laptop_lv.setOnItemClickListener(onItemClickListener);
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