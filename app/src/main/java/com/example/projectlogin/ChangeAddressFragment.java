package com.example.projectlogin;

import android.content.Context;
import android.content.res.ColorStateList;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;


public class ChangeAddressFragment extends Fragment {
    View root;

    private TextInputEditText address_tv;
    private TextInputEditText name;
    private TextInputEditText phone_number;
    private Button proceed;
    private OnDataPass dataPasser;

    private LinearLayout address_change;
    private LinearLayout choose_address_lnlo;

    private LinearLayout input_address;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.change_address,null,false);

        initComponents();
        return root;
    }

    private void initComponents() {
        choose_address_lnlo = root.findViewById(R.id.choose_address_lnlo);
        address_tv = root.findViewById(R.id.address);
        name = root.findViewById(R.id.payment_name);
        phone_number = root.findViewById(R.id.payment_phonenum);
        proceed = root.findViewById(R.id.proceed_btn);
        address_change = root.findViewById(R.id.address_book_change);
        input_address = root.findViewById(R.id.input_address);


        DatabaseReference databaseReference =  DatabaseRef.getDatabaseReference().child("Address book");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    View view = LayoutInflater.from(getContext()).inflate(R.layout.choose_address, address_change, false);
                    TextView name_phone = view.findViewById(R.id.namephonenum_choose);
                    TextView add = view.findViewById(R.id.address_change);

                    final String name = (String) dataSnapshot.child("Name").getValue();
                    final String phonenum = (String) dataSnapshot.child("Phone Number").getValue();
                    final String address_s = (String) dataSnapshot.child("Address").getValue();

                    name_phone.setText( name + " - " + phonenum);
                    add.setText(address_s);

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dataPasser.onDataPass(name,phonenum,address_s);
                            ((PaymentActivity)getActivity()).close_frame();
                        }
                    });

                    address_change.addView(view);
                }
                if (snapshot.getChildrenCount() == 0) {
                    input_address.setVisibility(View.VISIBLE);
                    choose_address_lnlo.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        address_tv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    AutoCorrectAddress();
                }
            }
        });

        root.findViewById(R.id.back_to_payment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input_address.setVisibility(View.VISIBLE);
                choose_address_lnlo.setVisibility(View.GONE);
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (address_tv.isFocusable()) {
                    AutoCorrectAddress();
                }
                if (Check_null()) {
                    Toast.makeText(getContext(), "Please fill in all of your information", Toast.LENGTH_SHORT).show();
                } else {
                    dataPasser.onDataPass(name.getText().toString(), phone_number.getText().toString()
                                            ,address_tv.getText().toString());
                    ((PaymentActivity)getActivity()).close_frame();

                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                }
            }
        });

    }

    private void AutoCorrectAddress() {
        TextView textView = root.findViewById(R.id.address);
        try {
            String location = textView.getText().toString();
            if (location.isEmpty()) return;
            List<Address> addressList = null;
            if (location != null || !location.equals("")) {
                Geocoder geocoder = new Geocoder(getContext());
                try {
                    addressList = geocoder.getFromLocationName(location, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addressList.size() == 0) {
                    Toast.makeText(getContext(), "Hãy gõ có dấu hoặc gõ thêm chi tiết", Toast.LENGTH_SHORT).show();
                }
                Address address = addressList.get(0);
                textView.setText(address.getAddressLine(0));
                TextInputEditText temp = root.findViewById(R.id.address);
                TextInputLayout textInputLayout = (TextInputLayout) temp.getParent().getParent();
                textInputLayout.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorBlack)));
                textInputLayout.setStartIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBlack)));
                textInputLayout.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorBlack)));

            }
        } catch (Exception e) {
            textView.setText(null);
            Toast.makeText(getContext(), "Invalid address", Toast.LENGTH_SHORT).show();
            TextInputEditText temp = root.findViewById(R.id.address);
            TextInputLayout textInputLayout = (TextInputLayout) temp.getParent().getParent();
            textInputLayout.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
            textInputLayout.setStartIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
            textInputLayout.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));

        }
    }

    public interface OnDataPass {
        void onDataPass(String name, String phone, String address);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }

    private boolean Check_null() {
        int[] paymentInfo = {R.id.payment_name, R.id.payment_phonenum, R.id.address};
        for (int j = 0; j < paymentInfo.length; j++) {
            TextInputEditText test = root.findViewById(paymentInfo[j]);
            if (test.getText().toString().isEmpty()) {
                for (int i = 0; i < paymentInfo.length; i++) {
                    TextInputEditText temp = root.findViewById(paymentInfo[i]);
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
}

