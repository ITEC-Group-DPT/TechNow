package com.example.projectlogin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EventListener;
import java.util.List;


public class PaymentActivity extends AppCompatActivity {
    private Cart cart = new Cart();
    private TextView tv_total;
    private NumberFormat format = new DecimalFormat("#,###");
    private String formattedTotalCash;
    private TextInputEditText address;

    private TextInputEditText name;
    private TextInputEditText phone_number;

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
                int temp = cart.calTotalCash();
                formattedTotalCash = format.format(temp) + "₫";
                tv_total = findViewById(R.id.total_cash);
                tv_total.setText(formattedTotalCash);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        address = findViewById(R.id.address);
        name = findViewById(R.id.payment_name);
        phone_number = findViewById(R.id.payment_phonenum);

        address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    AutoCorrectAddress();
                }
            }
        });


    }

    private void AutoCorrectAddress() {
        TextView textView = findViewById(R.id.address);
        try {
            String location = textView.getText().toString();
            if (location.isEmpty()) return;
            List<Address> addressList = null;
            if (location != null || !location.equals("")) {
                Geocoder geocoder = new Geocoder(PaymentActivity.this);
                try {
                    addressList = geocoder.getFromLocationName(location, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addressList.size() == 0) {
                    Toast.makeText(PaymentActivity.this, "Check gõ có dấu hoặc gõ thêm chi tiết", Toast.LENGTH_SHORT).show();
                }
                Address address = addressList.get(0);
                textView.setText(address.getAddressLine(0));
            }
        } catch (Exception e) {
            textView.setText(null);
            Toast.makeText(PaymentActivity.this, "Invalid address", Toast.LENGTH_SHORT).show();
        }
    }

    public void Check_out(View view) {
        if (address.isFocusable()) {
            AutoCorrectAddress();
        }
        if (Check_null()) {
            Toast.makeText(this, "Please fill in all of your information", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder note = new AlertDialog.Builder(this);
            note.setTitle("Confirm checkout");
            note.setMessage("Confirm all your info?");
            note.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int id) {
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

                            tempOrder.child(id).child("Customer").child("Name").setValue(name.getText().toString());
                            tempOrder.child(id).child("Customer").child("Address").setValue(address.getText().toString());
                            tempOrder.child(id).child("Customer").child("Phone Number").setValue(phone_number.getText().toString());
                            tempOrder.child(id).child("Customer").child("Date").setValue(format.format(currentTime));


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

    public void Back_cart(View view) {
        onBackPressed();
    }

    private boolean Check_null() {
        int[] paymentInfo = {R.id.payment_name, R.id.payment_phonenum, R.id.address};
        for (int j = 0; j < paymentInfo.length; j++) {
            TextInputEditText test = findViewById(paymentInfo[j]);
            if (test.getText().toString().isEmpty()) {
                for (int i = 0; i < paymentInfo.length; i++) {
                    TextInputEditText temp = findViewById(paymentInfo[i]);
                    if (temp.getText().toString().isEmpty()) {
                        TextInputLayout textInputLayout = (TextInputLayout) temp.getParent().getParent();
                        textInputLayout.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                        textInputLayout.setStartIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                        textInputLayout.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                        // TODO: 10/28/2020: add check valid address
                    } else {
                        TextInputLayout textInputLayout = (TextInputLayout) temp.getParent().getParent();
                        textInputLayout.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorBlack)));
                        textInputLayout.setStartIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBlack)));
                        textInputLayout.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorBlack)));
                    }
                }
                return true;
            }
        }
        return false;
    }


    public void onasd(View view) {
            /*Intent  intent = new Intent(getApplicationContext(),AddressMapsAPI.class);
            startActivity(intent);*/
    }

    /*@Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        int[] paymentInfo = {R.id.payment_name, R.id.payment_phonenum};
        for (int j = 0; j < paymentInfo.length; j++) {
            TextInputEditText test = findViewById(paymentInfo[j]);

    }*/

    //@Override
   /* protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (!intent.hasExtra("Address")) return;
        float x = 0;
        x = intent.getFloatExtra("Distance", 0);
        String a = null;
        a = intent.getStringExtra("Address");
        TextView add = findViewById(R.id.address);
        add.setText(a);
        Toast.makeText(this, Float.toString(x), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, a, Toast.LENGTH_SHORT).show();

    }*/


    public void Back_Payment(View view) {
        onBackPressed();
    }
}