package com.example.projectlogin;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
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

public class Product_Catalog extends Fragment {

    private ArrayList<Product> productList;
    private LinearLayout lnlo;
    private GridView productLV;
    private ProductListViewAdapter productListViewAdapter;
    private DatabaseReference reff;
    private ArrayList<String> spinnerList;
    private Spinner spinner;
    private View root;
    private boolean isBrand;

    public Product_Catalog(ArrayList<Product> list) {
        isBrand = true;
        productList = new ArrayList<>(list);
    }

    public Product_Catalog() {
        productList = new ArrayList<>();
        isBrand = false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_catalog, container, false);
        productLV = root.findViewById(R.id.catalog_lv);
        lnlo = root.findViewById(R.id.lnlo);
        spinner = root.findViewById(R.id.spinner);
        loadData();
        return root;
    }

    private class AsyncTaskMonitor extends AsyncTask<ArrayList, String, String> {
        @Override
        protected String doInBackground(final ArrayList... arrayLists) {
            spinnerList.add("Price lowest");
            spinnerList.add("Price highest");
            spinnerList.add("Rating lowest");
            spinnerList.add("Rating highest");
            spinnerList.add("Sold lowest");
            spinnerList.add("Sold highest");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_sort_item, spinnerList);
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
                            sortRatingLowest();
                            break;
                        case 3:
                            sortRatingHighest();
                            break;
                        case 4:
                            sortSoldLowest();
                            break;
                        case 5:
                            sortSoldHighest();
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            };
            spinner.setOnItemSelectedListener((onItemSelectedListener));

            if (!isBrand){
                final ArrayList<Product> monitors = arrayLists[0];
                final String catalog = ((MainUI) getActivity()).getCatalog();
                reff = FirebaseDatabase.getInstance().getReference("Products").child(catalog);
                reff.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Product product = dataSnapshot.getValue(Product.class);
                            product.setType(catalog);
                            monitors.add(product);
                        }
                        productListViewAdapter = new ProductListViewAdapter(getContext(), R.layout.product_listview_layout, monitors);
                        productLV.setAdapter(productListViewAdapter);

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

                        productLV.setOnItemClickListener(onItemClickListener);
                        onPostExecute("completed");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
            else{
                productListViewAdapter = new ProductListViewAdapter(getContext(), R.layout.product_listview_layout, productList);
                productLV.setAdapter(productListViewAdapter);

                AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Product product = productList.get(position);
                        Intent intent = new Intent(getContext(), ItemDetail.class);
                        intent.putExtra("itemName", product.getName());
                        intent.putExtra("itemType", product.getType());
                        startActivity(intent);
                    }
                };

                productLV.setOnItemClickListener(onItemClickListener);
                onPostExecute("completed");
            }
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
        spinnerList = new ArrayList<>();

        new AsyncTaskMonitor().execute(productList);
    }

    public void sortPriceHighest() {
        Collections.sort(productList, new Comparator<Product>() {
            public int compare(Product p1, Product p2) {
                if (p1.getPrice() > p2.getPrice()) return -1;
                else if (p1.getPrice() < p2.getPrice()) return 1;
                else return 0;
            }
        });
        productListViewAdapter.notifyDataSetChanged();
    }

    public void sortPriceLowest() {
        Collections.sort(productList, new Comparator<Product>() {
            public int compare(Product p1, Product p2) {
                if (p1.getPrice() > p2.getPrice()) return 1;
                else if (p1.getPrice() < p2.getPrice()) return -1;
                else return 0;
            }
        });
        productListViewAdapter.notifyDataSetChanged();
    }

    public void sortRatingHighest() {
        Collections.sort(productList, new Comparator<Product>() {
            public int compare(Product p1, Product p2) {
                if (p1.getRating() > p2.getRating()) return -1;
                else if (p1.getRating() < p2.getRating()) return 1;
                else return 0;
            }
        });
        productListViewAdapter.notifyDataSetChanged();
    }

    public void sortRatingLowest() {
        Collections.sort(productList, new Comparator<Product>() {
            public int compare(Product p1, Product p2) {
                if (p1.getRating() > p2.getRating()) return 1;
                else if (p1.getRating() < p2.getRating()) return -1;
                else return 0;
            }
        });
        productListViewAdapter.notifyDataSetChanged();
    }



    public void sortSoldHighest() {
        Collections.sort(productList, new Comparator<Product>() {
            public int compare(Product p1, Product p2) {
                if (p1.getSold() > p2.getSold()) return -1;
                else if (p1.getSold() < p2.getSold()) return 1;
                else return 0;
            }
        });
        productListViewAdapter.notifyDataSetChanged();
    }

    public void sortSoldLowest() {
        Collections.sort(productList, new Comparator<Product>() {
            public int compare(Product p1, Product p2) {
                if (p1.getSold() > p2.getSold()) return 1;
                else if (p1.getSold() < p2.getSold()) return -1;
                else return 0;
            }
        });
        productListViewAdapter.notifyDataSetChanged();
    }
}