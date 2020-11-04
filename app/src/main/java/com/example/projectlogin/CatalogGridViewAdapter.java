package com.example.projectlogin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class CatalogGridViewAdapter extends ArrayAdapter<Catalog> {
    private Context context;
    private int layoutID;
    private ArrayList<Catalog> catalogList;


    public CatalogGridViewAdapter(@NonNull Context context, int resource, @NonNull List<Catalog> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutID = resource;
        this.catalogList = (ArrayList<Catalog>) objects;
    }

    @Override
    public int getCount() {
        return catalogList.size();
    }

    @Nullable
    @Override
    public Catalog getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(layoutID, null, false);
        } else {
            System.gc();
        }

        ImageView imageView = convertView.findViewById(R.id.iv_ava_catalog);
        TextView tv_name = convertView.findViewById(R.id.tv_name_catalog);

        Catalog temp = catalogList.get(position);

        Glide.with(context).load(temp.getAvatarURL()).into(imageView);
        tv_name.setText(temp.getName());

        return convertView;
    }
}
