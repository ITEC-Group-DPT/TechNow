package com.example.projectlogin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

import static com.example.projectlogin.Cart.getCartArrList;

public class ProductListViewAdapter extends ArrayAdapter<Product> {
    private Context context;
    private int layoutID;
    private ArrayList<Product> Products;
    ImageButton btn_add;
    onAddToCart myListener;
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

    public interface onAddToCart {
        void onAddToCart(ImageButton btn_add, int noOfItem);
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
        final ImageButton btn_add = convertView.findViewById(R.id.btn_add);

        Product temp = Products.get(position);

        Glide.with(context)
                .load(temp.getAvatarURL())
                .into(imageView);

        //imageView.setImageBitmap(temp.getAvatar());

        tv_name.setText(temp.getName());
        String formattedPrice = "$" + format.format(temp.getPrice()) + "₫";
        tv_price.setText(formattedPrice);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Product temp1 = Products.get(position);
                final DatabaseReference databaseReference = DatabaseRef.getDatabaseReference().child("Order");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot: snapshot.getChildren())
                        {
                            if (temp1.getName().equals(dataSnapshot.getValue(Product.class).getName())) {
                                Toast.makeText(getContext(), "Already added", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        databaseReference.child(temp1.getName()).setValue(temp1);
                        myListener.onAddToCart(btn_add, getCartArrList().size());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                /*if (getCartArrList() != null) {
                    for (int i = 0; i < getCartArrList().size(); i++) {
                        Product temp2 = getCartArrList().get(i);
                        if (temp1.getName().equals(temp2.getName())) {
                            Toast.makeText(getContext(), "Already added", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                Cart.addItem(new Product(temp1.getAvatarURL(), temp1.getName() ,temp1.getPrice()));
                Toast.makeText(getContext(), "Added successfully", Toast.LENGTH_SHORT).show();

                */
            }
        });
        return convertView;
    }
}