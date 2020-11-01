package com.example.projectlogin;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Monitor_Catalog extends Fragment {

    private ArrayList<Product> monitors;
    private LinearLayout lnlo;
    private ListView monitor_lv;
    private ProductListViewAdapter productListViewAdapter;
    private DatabaseReference reff;
    private ArrayList<String> spinnerList;
    private Spinner spinner;
    private View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_catalog, container, false);
        monitor_lv = root.findViewById(R.id.catalog_lv);
        lnlo = root.findViewById(R.id.lnlo);
        spinner = root.findViewById(R.id.spinner);
        loadData();
        return root;
    }


    private class AsyncTaskMonitor extends AsyncTask<ArrayList, String, String> {
        @Override
        protected String doInBackground(ArrayList... arrayLists) {
            spinnerList.add("Price lowest");
            spinnerList.add("Price highest");
            spinnerList.add("Sold lowest");
            spinnerList.add("Sold highest");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            sortPriceLowest();
                            break;
                        case 1:
                            sortPriceHighest();
                            break;
                        case 2:
                            sortSoldLowest();
                            break;
                        case 3:
                            sortSoldHighest();
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            };
            spinner.setOnItemSelectedListener((onItemSelectedListener));

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
                    productListViewAdapter = new ProductListViewAdapter(getContext(), R.layout.product_listview_layout, monitors);
                    /*adapter.setOnAddtoCartInterface(new ProductListViewAdapter.onAddToCart() {
                        @Override
                        public void onAddToCart(ImageButton imageButtonAddToCart) {
                            imageButtonAddToCart.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.icon_add_to_cart));
                            ((MainUI)getActivity()).cart_btn.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.icon_shake));

                        }
                    });*/


                    monitor_lv.setAdapter(productListViewAdapter);

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
            lnlo.setVisibility(View.GONE);
            ProgressBar progressBar = root.findViewById(R.id.progress_catalog);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String command) {
            super.onPostExecute(command);
            if (command != null && command.equals("completed")) {
                lnlo.setVisibility(View.VISIBLE);
                ProgressBar progressBar = root.findViewById(R.id.progress_catalog);
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    private void loadData() {
        monitors = new ArrayList<>();
        spinnerList = new ArrayList<>();
        new AsyncTaskMonitor().execute(monitors);
    }

    public void sortPriceHighest() {
        Collections.sort(monitors, new Comparator<Product>() {
            public int compare(Product p1, Product p2) {
                if (p1.getPrice() > p2.getPrice()) return -1;
                else if (p1.getPrice() < p2.getPrice()) return 1;
                else return 0;
            }
        });
        productListViewAdapter.notifyDataSetChanged();
    }

    public void sortPriceLowest() {
        Collections.sort(monitors, new Comparator<Product>() {
            public int compare(Product p1, Product p2) {
                if (p1.getPrice() > p2.getPrice()) return 1;
                else if (p1.getPrice() < p2.getPrice()) return -1;
                else return 0;
            }
        });
        productListViewAdapter.notifyDataSetChanged();
    }

    public void sortSoldHighest() {
        Collections.sort(monitors, new Comparator<Product>() {
            public int compare(Product p1, Product p2) {
                if (p1.getSold() > p2.getSold()) return -1;
                else if (p1.getSold() < p2.getSold()) return 1;
                else return 0;
            }
        });
        productListViewAdapter.notifyDataSetChanged();
    }

    public void sortSoldLowest() {
        Collections.sort(monitors, new Comparator<Product>() {
            public int compare(Product p1, Product p2) {
                if (p1.getSold() > p2.getSold()) return 1;
                else if (p1.getSold() < p2.getSold()) return -1;
                else return 0;
            }
        });
        productListViewAdapter.notifyDataSetChanged();
    }
}