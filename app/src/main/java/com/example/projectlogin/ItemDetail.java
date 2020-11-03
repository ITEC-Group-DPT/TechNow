package com.example.projectlogin;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.dk.animation.circle.CircleAnimationUtil;
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
    private TextView productName_TV, productPrice_TV, sold_TV, description_TV, fullDetail_TV, shortDetail_TV, shortDetailLastLine_TV, btnViewMore;
    private ImageView arrow_IV;
    private boolean expanded = false;
    private NumberFormat format = new DecimalFormat("#,###");
    private RatingBar ratingBar;
    private TextView noOfRating;

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

    private class AsyncTaskDetail extends AsyncTask<String, String, String> {

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
            if (command == null) return;
            if (command.equals("loadAllData")) {
                loadCarouselView();
                loadProductInfo();

                LinearLayout layout = findViewById(R.id.progress_lnlo);
                layout.setVisibility(View.GONE);
            }

            DatabaseRef.getDatabaseReference().child("Favorite").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (product != null && dataSnapshot.getKey().equals(product.getName())) {
                            ImageButton btn_favorite = findViewById(R.id.favorite_icon);
                            btn_favorite.setImageResource(R.drawable.favorite_icon_selected);
                            btn_favorite.setColorFilter(Color.RED);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
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
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
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
        shortDetail_TV = findViewById(R.id.short_detail_TV);
        shortDetailLastLine_TV = findViewById(R.id.short_detail_last_line_TV);
        btnViewMore = findViewById(R.id.view_more_btn);
        arrow_IV = findViewById(R.id.expand_arrrow_IV);
        fullDetail_TV = findViewById(R.id.full_detail_TV);
        ratingBar = findViewById(R.id.ID_ratingbar);
        noOfRating = findViewById(R.id.no_of_rating);

        noOfRating.setText("Total: " + product.getRateNo());
        if (product.getRating() != 0)
            ratingBar.setRating(Float.parseFloat(String.format("%.1f", product.getRating())));

        productName_TV.setText(product.getName());
        String formattedPrice = format.format(product.getPrice()) + "₫";
        productPrice_TV.setText(formattedPrice);
        sold_TV.setText("  |  Sold: " + product.getSold());

        if (product.getDesc().isEmpty())
            product.setDesc("Updating...");
        if (product.getDetail().isEmpty())
            product.setDetail("Updating...");
        description_TV.setText(product.getDesc());

        String fullDetailStr = product.getDetail();
        fullDetail_TV.setText(fullDetailStr);

        if (countLines(fullDetailStr) > 4) {
            String[] shortDetailLineArray = new String[4];
            String shortDetailStr;
            String shortDetailLastLineStr;

            for (int i = 0; i < 4; i++) {
                shortDetailLineArray = fullDetailStr.split("\r\n|\r|\n");
            }

            shortDetailStr = shortDetailLineArray[0] + "\n" + shortDetailLineArray[1] + "\n" + shortDetailLineArray[2];
            shortDetail_TV.setText(shortDetailStr);
            shortDetailLastLineStr = shortDetailLineArray[3];
            shortDetailLastLine_TV.setText(shortDetailLastLineStr);
        } else {
            fullDetail_TV.setVisibility(View.VISIBLE);
            shortDetail_TV.setVisibility(View.GONE);
            shortDetailLastLine_TV.setVisibility(View.GONE);
            btnViewMore.setVisibility(View.GONE);
            arrow_IV.setVisibility(View.GONE);
        }

        cart_btn = findViewById(R.id.cart_btn);
        final RelativeLayout btn_add = findViewById(R.id.btn_add);

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
                                databaseReference.child(product.getName()).child("quantity").setValue(quantity + 1);
                                addToCartAnimation(carouselView, cart_btn);
                                return;
                            }
                        }
                        databaseReference.child(product.getName()).setValue(product);
                        addToCartAnimation(carouselView, cart_btn);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }

    public int countLines(String str){
        String[] lines = str.split("\r\n|\r|\n");
        return lines.length;
    }
    public void viewMoreOnClick(View view) {
        if (expanded) {
            fullDetail_TV.setVisibility(View.GONE);
            shortDetail_TV.setVisibility(View.VISIBLE);
            shortDetailLastLine_TV.setVisibility(View.VISIBLE);
            btnViewMore.setText("View More");
            arrow_IV.setImageResource(R.drawable.drop_down_arrow);
            expanded = false;
        } else {
            shortDetail_TV.setVisibility(View.GONE);
            shortDetailLastLine_TV.setVisibility(View.GONE);
            fullDetail_TV.setVisibility(View.VISIBLE);
            btnViewMore.setText("View Less");
            arrow_IV.setImageResource(R.drawable.expand_less_arrow);
            expanded = true;
        }
    }

    public void addToCartAnimation(CarouselView carouselView, ImageButton cartBtn) {
        //Toast.makeText(getApplicationContext(), "Added successfully", Toast.LENGTH_SHORT).show();
        new CircleAnimationUtil().attachActivity(ItemDetail.this).setTargetView(carouselView).setDestView(cartBtn).setMoveDuration(800).startAnimation();
        carouselView.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                cart_btn.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.icon_shake));
            }
        }, 2000);
    }

    public void addToFavorite(View view) {
        final DatabaseReference databaseReference = DatabaseRef.getDatabaseReference().child("Favorite");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ImageButton btn_favorite = findViewById(R.id.favorite_icon);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (product.getName().equals(dataSnapshot.getKey())) {
                        btn_favorite.setColorFilter(Color.BLACK);
                        btn_favorite.setImageResource(R.drawable.favorite_icon);
                        databaseReference.child(product.getName()).removeValue();
                        return;
                    }
                }
                databaseReference.child(product.getName()).setValue(product);
                btn_favorite.setImageResource(R.drawable.favorite_icon_selected);
                btn_favorite.setColorFilter(Color.RED);
                btn_favorite.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.icon_shake));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void backBtn(View view) {
        onBackPressed();
    }
}