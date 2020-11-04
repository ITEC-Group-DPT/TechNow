package com.example.projectlogin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mhmtk.twowaygrid.TwoWayAdapterView;
import com.mhmtk.twowaygrid.TwoWayGridView;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Pattern;


public class MainUI extends AppCompatActivity {
    private ArrayList<String> imageURLList;
    private CarouselView carouselView;
    private FrameLayout frameLayout;

    protected LinearLayout linearLayout;
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected static TextView tv_username;
    private AlertDialog.Builder confirmSignOutBuilder;
    protected Toolbar toolbar;
    private TextView toolbar_title;
    protected TextView noOfItemInCart;
    protected int noOfItem;
    public static final String SHARED_PREFS = "rememberMe";
    private SharedPreferences sharedPreferences;
    protected static String username;
    protected ImageButton cart_btn;
    private FrameLayout frame_search;
    private String catalog;

    private RecyclerView recyclerView;
    private ArrayList<Product> productList;
    private ArrayList<Product> topSellerProductList;
    private DatabaseReference reff;

    private SearchView searchView;
    private ListView listView;
    private ArrayList<Product> tempArrayList;
    private SwipeRefreshLayout refreshLayout;

    private TwoWayGridView categoryGridView;
    private ArrayList<Catalog> catalogList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_u_i);

        onCreateDrawerLayout();
        navigationView.setCheckedItem(R.id.nav_home);
        refreshLayout = findViewById(R.id.refresh);
        loadData();
        loadCarouselView();
        loadTopSeller();
        loadsearchview();
        loadCategories();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                loadCarouselView();
                loadTopSeller();
                loadCategories();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void loadCategories(){
        categoryGridView = findViewById(R.id.category_grid_view);
        catalogList = new ArrayList<>();
        Catalog a =  new Catalog();
        for (int i = 0; i < 14; i++) {
            Catalog temp = new Catalog();
            temp.setName("Keyboard");
            temp.setAvatarURL("https://firebasestorage.googleapis.com/v0/b/technow-4b3ab.appspot.com/o/UI%2FbestSeller1.png?alt=media&token=02429f59-88c1-4124-85d6-d086f207345a");
            catalogList.add(temp);
        }

        CatalogGridViewAdapter adapter = new CatalogGridViewAdapter(MainUI.this, R.layout.catalog_gridview_layout, catalogList);

        categoryGridView.setAdapter(adapter);

        TwoWayAdapterView.OnItemClickListener onItemClickListener = new TwoWayAdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(TwoWayAdapterView<?> twoWayAdapterView, View view, int position, long l) {
                catalog = "Mouse";
                changeFragment(catalog);
                /*Intent intent = new Intent(getApplicationContext(), Product_Catalog.class);
                startActivity(intent);*/
            }
        };

        categoryGridView.setOnItemClickListener(onItemClickListener);
    }

    private void loadsearchview() {
        listView = findViewById(R.id.searchMain);
        searchView = findViewById(R.id.search_bar);
        searchView.setFocusable(false);
        searchView.setIconified(false);
        searchView.clearFocus();
        setSearchviewTextSize(searchView, 14);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.onActionViewExpanded();
                searchView.setQueryHint("Product names, brand names,...");
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Toast.makeText(MainUI.this, newText, Toast.LENGTH_SHORT).show();
                if (newText.isEmpty()) {
                    listView.setVisibility(View.GONE);
                    frame_search.setVisibility(View.GONE);
                    return false;
                }

                listView.setVisibility(View.VISIBLE);

                frame_search.setVisibility(View.VISIBLE);

                int length = newText.length();
                tempArrayList = new ArrayList<>();
                for (int i = 0; i < productList.size(); i++) {
                    if (length<= productList.get(i).getName().length())
                    {
                        newText = newText.toLowerCase();
                        newText= Normalizer.normalize(newText,Normalizer.Form.NFD);
                        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
                        newText = pattern.matcher(newText).replaceAll("");

                        String str = productList.get(i).getName().toLowerCase();
                        str = Normalizer.normalize(str,Normalizer.Form.NFD);
                        pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
                        str = pattern.matcher(str).replaceAll("");
                        if (str.contains(newText))
                        {
                            tempArrayList.add(productList.get(i));
                        }
                    }
                }

                Product_searchbar_Adapter adapter = new Product_searchbar_Adapter(getApplicationContext(), R.layout.product_searchbar, tempArrayList);
                listView.setAdapter(adapter);

                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Product product = tempArrayList.get(i);
                Intent intent = new Intent(getApplicationContext(), ItemDetail.class);
                intent.putExtra("itemName", product.getName());
                intent.putExtra("itemType", product.getType());
                startActivity(intent);
            }
        });
    }

    private void setSearchviewTextSize(SearchView searchView, int fontSize) {
        try {
            AutoCompleteTextView autoCompleteTextViewSearch = (AutoCompleteTextView) searchView.findViewById(searchView.getContext().getResources().getIdentifier("app:id/search_src_text", null, null));
            if (autoCompleteTextViewSearch != null) {
                autoCompleteTextViewSearch.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            } else {
                LinearLayout linearLayout1 = (LinearLayout) searchView.getChildAt(0);
                LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
                LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
                AutoCompleteTextView autoComplete = (AutoCompleteTextView) linearLayout3.getChildAt(0);
                autoComplete.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            }
        } catch (Exception e) {
            LinearLayout linearLayout1 = (LinearLayout) searchView.getChildAt(0);
            LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
            LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
            AutoCompleteTextView autoComplete = (AutoCompleteTextView) linearLayout3.getChildAt(0);
            autoComplete.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        }
    }

    private void loadData() {
        View newview = navigationView.getHeaderView(0);
        tv_username = newview.findViewById(R.id.username);
        DatabaseRef.getDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username = snapshot.getKey();
                tv_username.setText(username);
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
    }

    private void onCreateDrawerLayout() {
        frame_search = findViewById(R.id.search_view_frame);
        drawerLayout = findViewById(R.id.drawer_lo);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        linearLayout = findViewById(R.id.lnlo);
        toolbar_title = findViewById(R.id.toolbar_title);

        noOfItemInCart = findViewById(R.id.number_of_item_in_cart);
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        confirmSignOutBuilder = new AlertDialog.Builder(this);
        frameLayout = findViewById(R.id.Frame_layout);

        cart_btn = findViewById(R.id.cart_btn);
        cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
            }
        });

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
                        intent = new Intent(MainUI.this, Account_Setting.class);
                        startActivity(intent);
                        break;
                    case(R.id.nav_setting):
                        intent = new Intent(MainUI.this, ReenterPassword.class);
                        startActivity(intent);
                        break;
                    case (R.id.mouse):
                        catalog = "Mouse";
                        changeFragment(catalog);
                        break;
                    case (R.id.keyboard):
                        catalog = "Keyboard";
                        changeFragment(catalog);
                        break;
                    case (R.id.headphone):
                        catalog = "Headphone";
                        changeFragment(catalog);
                        break;
                    case (R.id.speaker):
                        catalog = "Speaker";
                        changeFragment(catalog);
                        break;
                    case (R.id.monitor):
                        catalog = "Monitor";
                        changeFragment(catalog);
                        break;
                    case(R.id.gaming_chair):
                        catalog = "Gaming Chair";
                        changeFragment(catalog);
                        break;
                    case (R.id.laptop):
                        catalog = "Laptop";
                        changeFragment(catalog);
                        break;
                    case(R.id.cpu):
                        catalog = "CPU";
                        changeFragment(catalog);
                        break;
                    case(R.id.vga):
                        catalog = "VGA";
                        changeFragment(catalog);
                        break;
                    case(R.id.ram):
                        catalog = "RAM";
                        changeFragment(catalog);
                        break;
                    case (R.id.ssd):
                        catalog = "SSD";
                        changeFragment(catalog);
                        break;
                    case (R.id.mainboard):
                        catalog = "Mainboard";
                        changeFragment(catalog);
                        break;
                    case (R.id.case_pc):
                        catalog = "Case";
                        changeFragment(catalog);
                        break;
                    case (R.id.psu):
                        catalog = "PSU";
                        changeFragment(catalog);
                        break;
                    case (R.id.nav_home):
                        intent = new Intent(MainUI.this, MainUI.class);
                        startActivity(intent);
                        break;
                    case (R.id.about):
                        intent = new Intent(MainUI.this, AboutActivity.class);
                        startActivity(intent);
                        break;
                    case (R.id.feedback):
                        intent = new Intent(MainUI.this, FeedbackActivity.class);
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

    private void loadCarouselView() {
        carouselView = findViewById(R.id.carouselView);
        imageURLList = new ArrayList<>();
        imageURLList.add("https://firebasestorage.googleapis.com/v0/b/technow-4b3ab.appspot.com/o/UI%2Fcarousel_image_0.jpg?alt=media&token=2c3a248a-4cbf-4a0a-a129-4fc6b3039b79");
        imageURLList.add("https://firebasestorage.googleapis.com/v0/b/technow-4b3ab.appspot.com/o/UI%2Fcarousel_image_1.jpg?alt=media&token=5796c932-9872-4c4a-9c2b-ec4cb5d845ca");
        imageURLList.add("https://firebasestorage.googleapis.com/v0/b/technow-4b3ab.appspot.com/o/UI%2Fcarousel_image_2.jpg?alt=media&token=3f944783-08b2-455d-86af-1b1286bc8b53");
        imageURLList.add("https://firebasestorage.googleapis.com/v0/b/technow-4b3ab.appspot.com/o/UI%2Fcarousel_image_3.jpg?alt=media&token=3b452493-3932-4604-b815-c691587678db");
        imageURLList.add("https://firebasestorage.googleapis.com/v0/b/technow-4b3ab.appspot.com/o/UI%2Fcarousel_image_4.jpg?alt=media&token=ff4d7758-27af-414e-87f0-c1270112d73a");

        carouselView.setPageCount(imageURLList.size());
        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                Glide.with(getApplicationContext()).load(imageURLList.get(position)).into(imageView);
            }
        };

        carouselView.setImageListener(imageListener);
        carouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                switch (position) {
                    case 0:
                        catalog = "Mouse";
                        break;
                    case 1:
                        catalog = "Keyboard";
                        break;
                    case 2:
                        catalog = "Monitor";
                        break;
                    case 3:
                        catalog = "Laptop";
                        break;
                }
                changeFragment(catalog);
            }
        });
    }

    private class AsyncTask_TopSeller extends AsyncTask<ArrayList<Product>,String,String>
    {

        @Override
        protected String doInBackground(ArrayList... arrayLists) {

            reff = FirebaseDatabase.getInstance().getReference("Products");
            reff.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    getProductData("Mouse", snapshot);
                    getProductData("Keyboard", snapshot);
                    getProductData("Headphone", snapshot);
                    getProductData("Speaker", snapshot);
                    getProductData("Monitor", snapshot);
                    getProductData("GamingChair", snapshot);
                    getProductData("Laptop", snapshot);
                    getProductData("CPU", snapshot);
                    getProductData("VGA", snapshot);
                    getProductData("RAM", snapshot);
                    getProductData("SSD", snapshot);
                    getProductData("Mainboard", snapshot);
                    getProductData("Case", snapshot);
                    getProductData("PSU", snapshot);

                    Collections.sort(productList, new Comparator<Product>() {
                        public int compare(Product p1, Product p2) {
                            if (p1.getSold() > p2.getSold()) return -1;
                            else if (p1.getSold() < p2.getSold()) return 1;
                            else return 0;
                        }
                    });

                    for (int i = 0; i < 10; i++) {
                        topSellerProductList.add(productList.get(i));
                    }

                    TopSellerAdapter adapter = new TopSellerAdapter(MainUI.this, topSellerProductList);
                    recyclerView = findViewById(R.id.recycler_view);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);
                    onPostExecute("Completed");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            return null;
        }

        protected void getProductData(String productType, DataSnapshot snapshot){
            DataSnapshot snapshotProduct = snapshot.child(productType);
            for (DataSnapshot dataSnapshot : snapshotProduct.getChildren()) {
                Product product = dataSnapshot.getValue(Product.class);
                product.setType(productType);
                productList.add(product);
            }
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ProgressBar progressBar = findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.GONE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null)
            {
                ProgressBar progressBar = findViewById(R.id.progress_bar);
                progressBar.setVisibility(View.GONE);
                refreshLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    private void loadTopSeller() {
        productList = new ArrayList<>();
        topSellerProductList = new ArrayList<>();

        new AsyncTask_TopSeller().execute(productList);
    }

/*    public void Card_onClick(View view) {
        switch (view.getId()) {
            case (R.id.keyboard_cv):
                catalog = "Keyboard";
                break;
            case (R.id.mouse_cv):
                catalog = "Mouse";
                break;
            case (R.id.screen_cv):
                catalog = "Monitor";
                break;
            case (R.id.laptop_cv):
                catalog = "Laptop";
                break;
        }
        changeFragment(catalog);

    }*/

    private void close_FrameLayout() {
        if (frameLayout.getVisibility() != View.GONE) {
            toolbar_title.setText("TechNow");
            frameLayout.setVisibility(View.GONE);
        }
    }

    private void open_FrameLayout() {
        if (frameLayout.getVisibility() != View.VISIBLE)
            frameLayout.setVisibility(View.VISIBLE);
    }

    private void changeFragment(String Catalog) {
        open_FrameLayout();
        drawerLayout.closeDrawer(GravityCompat.START);
        switch (Catalog) {
            case ("Mouse"):
                navigationView.setCheckedItem(R.id.mouse);
                break;
            case ("Keyboard"):
                navigationView.setCheckedItem(R.id.keyboard);
                break;
            case ("Headphone"):
                navigationView.setCheckedItem(R.id.headphone);
                break;
            case ("Speaker"):
                navigationView.setCheckedItem(R.id.speaker);
                break;
            case ("Monitor"):
                navigationView.setCheckedItem(R.id.monitor);
                break;
            case ("GamingChair"):
                navigationView.setCheckedItem(R.id.gaming_chair);
                break;
            case ("Laptop"):
                navigationView.setCheckedItem(R.id.laptop);
                break;
            case ("CPU"):
                navigationView.setCheckedItem(R.id.cpu);
                break;
            case ("VGA"):
                navigationView.setCheckedItem(R.id.vga);
                break;
            case ("RAM"):
                navigationView.setCheckedItem(R.id.ram);
                break;
            case ("SSD"):
                navigationView.setCheckedItem(R.id.ssd);
                break;
            case ("Mainboard"):
                navigationView.setCheckedItem(R.id.mainboard);
                break;
            case ("Case"):
                navigationView.setCheckedItem(R.id.case_pc);
                break;
            case ("PSU"):
                navigationView.setCheckedItem(R.id.psu);
                break;
        }

        toolbar_title.setText(Catalog);
        getSupportFragmentManager().beginTransaction().replace(R.id.Frame_layout, new Product_Catalog()).commit();
    }

    public String getCatalog()
    {
        return catalog;
    }
    @Override
    public void onBackPressed() {
        drawerLayout.closeDrawer(GravityCompat.START);
        navigationView.setCheckedItem(R.id.nav_home);

        if (listView.getVisibility() == View.VISIBLE) {
            listView.setVisibility(View.GONE);
            frame_search.setVisibility(View.GONE);
        }
        else
        {
            close_FrameLayout();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setCheckedItem(R.id.nav_home);
    }
}