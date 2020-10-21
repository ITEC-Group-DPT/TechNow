package com.example.projectlogin;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import static com.example.projectlogin.Cart.clearAll;


public class MainLayout extends AppCompatActivity {

    protected LinearLayout linearLayout;
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected static TextView tv_username;
    private AlertDialog.Builder confirmSignOutBuilder;
    protected Toolbar toolbar;
    private TextView toolbar_title;
    protected CartListViewAdapter adapter;
    protected TextView noOfItemInCart;
    protected int noOfItem;

    public int getNoOfItem() {
        return noOfItem;
    }

    private ManageUser manageUser;
    public static final String SHARED_PREFS = "rememberMe";
    private SharedPreferences sharedPreferences;
    protected static String username;

    static protected ArrayList<Product> cartItems = new ArrayList<Product>();
    protected ImageButton cart_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_lo);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        linearLayout = findViewById(R.id.lnlo);
        toolbar_title = findViewById(R.id.toolbar_title);

        noOfItemInCart = findViewById(R.id.number_of_item_in_cart);
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        confirmSignOutBuilder = new AlertDialog.Builder(this);
        manageUser = new ManageUser(this);

        initDrawerLayout();
        cart_btn = findViewById(R.id.cart_btn);
        cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                intent.putExtra("noItem", noOfItem);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void initDrawerLayout() {

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case (R.id.nav_profile):
                        intent = new Intent(MainLayout.this, ReenterPassword.class);
                        intent.putExtra("username_reenter", username);
                        intent.putExtra("noItem", noOfItem);
                        startActivity(intent);
                        break;
                    case (R.id.keyboard):
                        intent = new Intent(MainLayout.this, Keyboard_Catalog.class);
                        intent.putExtra("name", username);
                        intent.putExtra("noItem", noOfItem);
                        startActivity(intent);
                        break;
                    case (R.id.mouse):
                        intent = new Intent(MainLayout.this, Mouse_Catalog.class);
                        intent.putExtra("name", username);
                        intent.putExtra("noItem", noOfItem);
                        startActivity(intent);
                        break;
                    case (R.id.screen):
                        intent = new Intent(MainLayout.this, Monitor_Catalog.class);
                        intent.putExtra("name", username);
                        intent.putExtra("noItem", noOfItem);
                        startActivity(intent);
                        break;
                    case (R.id.laptop):
                        intent = new Intent(MainLayout.this, Laptop_Catalog.class);
                        intent.putExtra("name", username);
                        intent.putExtra("noItem", noOfItem);
                        startActivity(intent);
                        break;
                    case (R.id.nav_home):
                        intent = new Intent(MainLayout.this, MainUI.class);
                        intent.putExtra("username", username);
                        intent.putExtra("noItem", noOfItem);
                        startActivity(intent);
                        break;
                    case (R.id.about):
                        intent = new Intent(MainLayout.this, AboutActivity.class);
                        startActivity(intent);
                        break;
                    case (R.id.feedback):
                        intent = new Intent(MainLayout.this, FeedbackActivity.class);
                        startActivity(intent);
                        break;
                    case (R.id.Logout):

                        confirmSignOutBuilder.setTitle("Confirmation");
                        confirmSignOutBuilder.setMessage("Do you want to sign out?");
                        confirmSignOutBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean(username, false);
                                editor.commit();
                                clearAll();
                                Intent intent1 = new Intent(getApplicationContext(), UserLogin.class);
                                startActivity(intent1);
                            }
                        });
                        confirmSignOutBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        confirmSignOutBuilder.create().show();
                }
                return true;
            }
        });
    }

    public void changeToolbarTitle(String new_title) {
        toolbar_title.setText(new_title);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("name", username);
        Log.d("@LOG", "MainActivity onSave: " + username);

        super.onSaveInstanceState(savedInstanceState);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("@LOG", "onActivityResult");
        if(requestCode==1  && resultCode==RESULT_OK) {
            noOfItem = data.getIntExtra("noItem", 0);
            if (noOfItem != 0) {
                noOfItemInCart.setVisibility(View.VISIBLE);
                noOfItemInCart.setText(String.valueOf(noOfItem));
            } else noOfItemInCart.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
}