package com.example.projectlogin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

    public void getProduct() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            itemName = bundle.getString("itemName");
            itemType = bundle.getString("itemType");
            Log.d("@@LOG", itemName);
            Log.d("@@LOG", itemType);
        }

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
                        loadCarouselView();
                        loadProductInfo();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
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
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                            if (product.getName().equals(dataSnapshot.getValue(Product.class).getName())) {
                                Toast.makeText(getApplicationContext(), "Already added", Toast.LENGTH_SHORT).show();
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
    }

    public void backBtn(View view) {
        onBackPressed();
    }
}