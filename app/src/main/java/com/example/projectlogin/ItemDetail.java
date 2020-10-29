package com.example.projectlogin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class ItemDetail extends AppCompatActivity {
    private String itemName;
    private String itemType;
    private Product product;
    private TextView noOfItemInCart;
    private int noOfItem;
    private ImageButton cart_btn;
    private ArrayList<String> imageURLList;
    private CarouselView carouselView;
    private TextView productName_TV, productPrice_TV, sold_TV, description_TV, detail_TV;
    private NumberFormat format = new DecimalFormat("#,###");
    private ImageButton btn_favorite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        loadData();
        getProduct();
    }

    public void loadData() {
        noOfItemInCart = findViewById(R.id.number_of_item_in_cart);
        DatabaseRef.getDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                noOfItem = (int) snapshot.child("Cart").getChildrenCount();
                if (noOfItem == 0) {
                    noOfItemInCart.setVisibility(View.GONE);
                } else {
                    noOfItemInCart.setVisibility(View.VISIBLE);
                    noOfItemInCart.setText(String.valueOf(noOfItem));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        cart_btn = findViewById(R.id.cart_btn);
        cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
            }
        });
    }

    private class AsyncTaskDetail extends AsyncTask<String,String, String>
    {

        @Override
        protected String doInBackground(String... strings) {
            DatabaseReference reff = FirebaseDatabase.getInstance().getReference("Products");
            reff.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    DataSnapshot snapshotChild = snapshot.child(itemType);
                    for (DataSnapshot dataSnapshot : snapshotChild.getChildren()) {
                        Product temp = dataSnapshot.getValue(Product.class);
                        if (temp.getName().equals(itemName)) {
                            product = temp;
                            product.setType(itemType);
                            onPostExecute("loadAllData");
                            break;
                        }
                    }
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

            LinearLayout layout = findViewById(R.id.progress_lnlo);
            layout.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String command) {
            super.onPostExecute(command);
            if (command != null && command.equals("loadAllData"))
            {
                loadCarouselView();
                loadProductInfo();

                LinearLayout layout = findViewById(R.id.progress_lnlo);
                layout.setVisibility(View.GONE);
            }
        }
    }

    public void getProduct() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            itemName = bundle.getString("itemName");
            itemType = bundle.getString("itemType");
            Log.d("@@LOG", itemName);
            Log.d("@@LOG", itemType);
        }

        new AsyncTaskDetail().execute("");
    }

    public void loadCarouselView() {
        carouselView = findViewById(R.id.carouselView);
        imageURLList = new ArrayList<>();

        if (!product.getAvatarURL().equals("")) {
            imageURLList.add(product.getAvatarURL());
        }
        if (!product.getAvatarURL1().equals("")) {
            imageURLList.add(product.getAvatarURL1());
        }
        if (!product.getAvatarURL2().equals("")) {
            imageURLList.add(product.getAvatarURL2());
        }
        if (!product.getAvatarURL3().equals("")) {
            imageURLList.add(product.getAvatarURL3());
        }
        /*if (!product.getAvatarURL4().equals("")) {
            imageURLList.add(product.getAvatarURL4());
        }*/

        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                Glide.with(getApplicationContext()).load(imageURLList.get(position)).into(imageView);
            }
        };
        carouselView.setImageListener(imageListener);
        carouselView.setPageCount(imageURLList.size());
    }

    public void loadProductInfo() {
        productName_TV = findViewById(R.id.productName_TV);
        productPrice_TV = findViewById(R.id.productPrice_TV);
        sold_TV = findViewById(R.id.sold_TV);
        description_TV = findViewById(R.id.description_TV);
        detail_TV = findViewById(R.id.detail_TV);

        productName_TV.setText(product.getName());
        String formattedPrice = "Price: " + format.format(product.getPrice()) + "₫";
        productPrice_TV.setText(formattedPrice);
        sold_TV.setText("Sold: " + product.getSold());

        if(product.getDesc().isEmpty())
            product.setDesc("Updating...");
        if (product.getDetail().isEmpty())
            product.setDetail("Updating...");
        description_TV.setText(product.getDesc());
        detail_TV.setText(product.getDetail());

        cart_btn = findViewById(R.id.cart_btn);
        final ImageButton btn_add = findViewById(R.id.btn_add);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference databaseReference = DatabaseRef.getDatabaseReference().child("Cart");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (product.getName().equals(dataSnapshot.getKey())) {
                                int quantity = dataSnapshot.getValue(Product.class).getQuantity();
                                databaseReference.child(product.getName()).child("quantity").setValue(quantity+1);

                                btn_add.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.icon_add_to_cart));
                                cart_btn.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.icon_shake));
                                Toast.makeText(getApplicationContext(), "Added successfully", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        databaseReference.child(product.getName()).setValue(product);
                        btn_add.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.icon_add_to_cart));
                        cart_btn.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.icon_shake));
                        Toast.makeText(getApplicationContext(), "Added successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
/*        final DatabaseReference databaseReference = DatabaseRef.getDatabaseReference().child("Favorite");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (product.getName().equals(dataSnapshot.getValue(Product.class).getName())) {
                        btn_favorite.setColorFilter(Color.RED);
                        return;
                    }
                }
                btn_favorite.setColorFilter(Color.BLACK);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });*/
   /*     btn_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference databaseReference = DatabaseRef.getDatabaseReference().child("Favorite");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (product.getName().equals(dataSnapshot.getValue(Product.class).getName())) {
                                btn_favorite.setColorFilter(Color.BLACK);
                                databaseReference.child(product.getName()).removeValue();
                                Toast.makeText(getApplicationContext(), "Removed from favorite", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        databaseReference.child(product.getName()).setValue(product);
                        btn_favorite.setColorFilter(Color.RED);
                        Toast.makeText(getApplicationContext(), "Added to favorite successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });*/

    }

    public void backBtn(View view) {
        onBackPressed();
    }

}