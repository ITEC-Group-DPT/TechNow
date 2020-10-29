package com.example.projectlogin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


public class ProductListViewAdapter extends ArrayAdapter<Product> {
    private Context context;
    private int layoutID;
    private ArrayList<Product> Products;
    private ArrayList<Product> ProductsOri;
    private onAddToCart myListener;
    private NumberFormat format = new DecimalFormat("#,###");

    public ProductListViewAdapter(@NonNull Context context, int resource, @NonNull List<Product> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutID = resource;
        this.Products = (ArrayList<Product>) objects;
    }

    @Override
    public int getCount() {
        return Products.size();
    }

    @Nullable
    @Override
    public Product getItem(int position) {
        return super.getItem(position);
    }

    public interface onAddToCart {
        void onAddToCart(ImageButton btn_add);
    }

    public void setOnAddtoCartInterface(onAddToCart callback) {
        myListener = callback;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(layoutID, null, false);
        } else {
            System.gc();
        }
        ImageView imageView = convertView.findViewById(R.id.iv_ava);
        TextView tv_name = convertView.findViewById(R.id.tv_name);
        TextView tv_price = convertView.findViewById(R.id.tv_price);
        //final ImageButton btn_add = convertView.findViewById(R.id.btn_add);
        Product temp = Products.get(position);
        Glide.with(context).load(temp.getAvatarURL()).into(imageView);
        tv_name.setText(temp.getName());
        String formattedPrice = "$" + format.format(temp.getPrice()) + "₫";
        tv_price.setText(formattedPrice);

       /* btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Product temp1 = Products.get(position);
                final DatabaseReference databaseReference = DatabaseRef.getDatabaseReference().child("Cart");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                            if (temp1.getName().equals(dataSnapshot.getValue(Product.class).getName())) {
                                Toast.makeText(getContext(), "Already added", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        databaseReference.child(temp1.getName()).setValue(temp1);
                        Toast.makeText(getContext(), "Added successfully", Toast.LENGTH_SHORT).show();
                        myListener.onAddToCart(btn_add);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });*/
        return convertView;
    }

  /*  @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                Products = (ArrayList<Product>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<Product> FilteredArrList = new ArrayList<Product>();

                if (ProductsOri == null) {
                    ProductsOri = new ArrayList<Product>(ProductsOri); // saves the original data in mOriginalValues
                }

                *//********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********//*
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = ProductsOri.size();
                    results.values = ProductsOri;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < ProductsOri.size(); i++) {
                        String data = ProductsOri.get(i).getName();
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(new Product(ProductsOri.get(i).getAvatarURL(),ProductsOri.get(i).getName(),ProductsOri.get(i).getPrice()));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }*/

}