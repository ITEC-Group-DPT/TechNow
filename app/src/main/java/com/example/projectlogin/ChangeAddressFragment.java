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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.List;


public class ChangeAddressFragment extends Fragment {
    View root;

    private TextInputEditText address_tv;
    private TextInputEditText name;
    private TextInputEditText phone_number;
    private Button proceed;
    private OnDataPass dataPasser;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.change_address,null,false);
        
        initComponents();
        return root;
    }

    private void initComponents() {
        address_tv = root.findViewById(R.id.address);
        name = root.findViewById(R.id.payment_name);
        phone_number = root.findViewById(R.id.payment_phonenum);
        proceed = root.findViewById(R.id.proceed_btn);

        address_tv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    AutoCorrectAddress();
                }
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

                    InputMethodManager imm = (InputMethodManager) ((PaymentActivity)getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
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

