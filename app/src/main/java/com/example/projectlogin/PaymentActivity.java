package com.example.projectlogin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class PaymentActivity extends AppCompatActivity implements ChangeAddressFragment.OnDataPass {
    private Cart cart = new Cart();
    private TextView tv_total;
    private NumberFormat format = new DecimalFormat("#,###");
    private String formattedTotalCash;
    private int Cart_totalcash;

    private double shippingFee;
    private String cus_name = "";
    private String cus_phone = "";
    private String cus_address = "";


    @Override
    public void onDataPass(String name, String phone, String address) {

        cus_name = name;
        cus_phone = phone;
        cus_address = address;

        TextView cus_namephone_tv = findViewById(R.id.customer_namephonenum);
        TextView cus_address_tv = findViewById(R.id.customer_address);

        cus_namephone_tv.setText(name + " - " + phone);
        cus_address_tv.setText(address);

        double distance = Calc_Distance(address);
                shippingFee = distance * 2;
                shippingFee = Math.round(shippingFee / 100) * 100;
                if (shippingFee < 10000) shippingFee = 0;
                if (shippingFee > 50000) shippingFee = 50000;

                TextView tv_shipping = findViewById(R.id.shipping_fee_payment);

                tv_shipping.setText(format.format(shippingFee) + "₫");


                tv_total = findViewById(R.id.total_cash);
                tv_total.setText(format.format(shippingFee + Cart_totalcash) + "₫");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        DatabaseRef.getDatabaseReference().child("Cart").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Product product = dataSnapshot.getValue(Product.class);
                        cart.addItem(product);
                    }
                }
                Cart_totalcash = cart.calTotalCash();
                formattedTotalCash = format.format(Cart_totalcash) + "₫";
                TextView notional_price = findViewById(R.id.notional_price_payment);
                notional_price.setText(formattedTotalCash);
                packageDetailonCreate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void packageDetailonCreate() {

        RadioButton radioButton = findViewById(R.id.radio_button);
        radioButton.setChecked(true);
        LinearLayout linearLayout = findViewById(R.id.package_detail);
        for (int i = 0; i < cart.getCartArrList().size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.product_lnlo_orderdetail, null, false);
            ImageView product_pic = view.findViewById(R.id.order_pic);
            TextView product_name = view.findViewById(R.id.name_product);
            TextView product_price_qty = view.findViewById(R.id.price_qty);

            final Product product = cart.getCartArrList().get(i);

            Glide.with(getApplicationContext()).load(product.getAvatarURL()).into(product_pic);
            product_name.setText(product.getName());

            NumberFormat format = new DecimalFormat("#,###");
            String formattedPrice = format.format(product.getPrice()) + " ₫";
            product_price_qty.setText(formattedPrice + " x" + product.getQuantity());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(PaymentActivity.this, ItemDetail.class);
                    intent.putExtra("itemName", product.getName());
                    intent.putExtra("itemType", product.getType());
                    startActivity(intent);
                }
            });
            linearLayout.addView(view);
        }
    }


    public void Check_out(View view) {

        if (cus_name.isEmpty() || cus_address.isEmpty() || cus_phone.isEmpty())
            Toast.makeText(this, "Please check your information!!", Toast.LENGTH_SHORT).show();
        else {
            AlertDialog.Builder note = new AlertDialog.Builder(this);
            note.setTitle("Confirm checkout");
            note.setMessage("Confirm all your info?");
            note.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int id) {
                    //// TODO: 11/3/2020 cal distance
                    final DatabaseReference tempOrder = DatabaseRef.getDatabaseReference().child("Order History");
                    tempOrder.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String id = "Order-" + snapshot.getChildrenCount();
                            for (int i = 0; i < cart.getNoOfItem(); i++) {
                                tempOrder.child(id).child(cart.getCartArrList().get(i).getName()).setValue(cart.getCartArrList().get(i));
                            }

                            Date currentTime = Calendar.getInstance().getTime();
                            SimpleDateFormat format = new SimpleDateFormat("hh:mm, dd/MM/yyyy");

                            tempOrder.child(id).child("Customer").child("Name").setValue(cus_name);
                            tempOrder.child(id).child("Customer").child("Address").setValue(cus_address);
                            tempOrder.child(id).child("Customer").child("Phone Number").setValue(cus_phone);
                            tempOrder.child(id).child("Customer").child("Date").setValue(format.format(currentTime));
                            tempOrder.child(id).child("Customer").child("Shipping Fee").setValue(shippingFee);
                            tempOrder.child(id).child("Customer").child("Notional Price").setValue(Cart_totalcash);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    DatabaseReference reff = FirebaseDatabase.getInstance().getReference("Products");
                    for (int i = 0; i < cart.getNoOfItem(); i++) {
                        final String type = cart.getCartArrList().get(i).getType();
                        final String name = cart.getCartArrList().get(i).getName();
                        final int quantity = cart.getCartArrList().get(i).getQuantity();
                        reff.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                DataSnapshot snapshotChild = snapshot.child(type);
                                for (DataSnapshot dataSnapshot : snapshotChild.getChildren()) {
                                    Product product = dataSnapshot.getValue(Product.class);
                                    int oldSold = product.getSold();
                                    String key = dataSnapshot.getKey();
                                    if (product.getName().equals(name)) {
                                        FirebaseDatabase.getInstance().getReference("Products").child(type).child(key).child("sold").setValue(oldSold + quantity);
                                        break;
                                    }
                                }
                                DatabaseRef.getDatabaseReference().child("Cart").removeValue();
                                Intent intent = new Intent(PaymentActivity.this, MainUI.class);
                                startActivity(intent);
                                Toast.makeText(getBaseContext(), "Successfully ordered", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                }
            });
            note.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int id) {
                    dialogInterface.cancel();
                }
            });
            AlertDialog showNote = note.create();
            showNote.show();
        }
    }


    public void Back_Payment(View view) {
        FrameLayout frameLayout = findViewById(R.id.frame_payment);

        if(frameLayout.getVisibility() == View.VISIBLE)
            frameLayout.setVisibility(View.GONE);
        else
            onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        FrameLayout frameLayout = findViewById(R.id.frame_payment);

        if(frameLayout.getVisibility() == View.VISIBLE)
            frameLayout.setVisibility(View.GONE);
        else
            onBackPressed();
    }

    public void close_frame()
    {
        FrameLayout frameLayout = findViewById(R.id.frame_payment);

        if(frameLayout.getVisibility() == View.VISIBLE)
            frameLayout.setVisibility(View.GONE);
    }

    private double Calc_Distance(String location) {
        //latlng cho dia chi nguoi dung
        List<Address> addressList = null;
        LatLng latLng1 = null, latLng2;
        Geocoder geocoder = new Geocoder(this);
        try {
            addressList = geocoder.getFromLocationName(location, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addressList.size() == 0) {
            Toast.makeText(this, "Invalid address", Toast.LENGTH_SHORT).show();
            return 0;
        }
        Address address = addressList.get(0);
        latLng1 = new LatLng(address.getLatitude(), address.getLongitude());
        //// TODO: 11/3/2020 update warehouse string in database


        //dia chi warehouse to latlng
        addressList = null;
        geocoder = new Geocoder(this);
        try {
            addressList = geocoder.getFromLocationName("hcmus", 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        address = addressList.get(0);
        latLng2 = new LatLng(address.getLatitude(), address.getLongitude());

        //distance
        float[] results = new float[1];
        Location.distanceBetween(latLng1.latitude, latLng1.longitude, latLng2.latitude, latLng2.longitude, results);
        double res = results[0];
        return res;
    }

    public void change_customer_detail(View view) {
        FrameLayout frameLayout = findViewById(R.id.frame_payment);
        frameLayout.setVisibility(View.VISIBLE);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_payment, new ChangeAddressFragment()).commit();
    }
}