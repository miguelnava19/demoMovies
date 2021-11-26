package com.example.moviesdemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesdemo.models.Movie;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> implements Filterable {
    private LayoutInflater inflater;
    private List<Movie> moviesArrayList;
    private String url;
    private Context context;
    private final String TAG = this.getClass().getCanonicalName();


    public RecyclerViewAdapter(Context context, List<Movie> moviesArrayList) {
        Log.i(TAG, " movies list --> " + moviesArrayList);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.moviesArrayList = moviesArrayList;
    }

    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerViewAdapter.MyViewHolder holder, int position) {
        final Movie movie = moviesArrayList.get(position);
        Log.i(TAG, "IMAGE URL      https://image.tmdb.org/t/p/w500/" + movie.getPosterPath());
        Picasso.get().load("https://image.tmdb.org/t/p/w500/" + movie.getPosterPath()).into(holder.imgMovie);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return moviesArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imgMovie;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imgMovie = (ImageView) itemView.findViewById(R.id.imgMovie);
        }

        @Override
        public void onClick(View view) {
            int pos = getLayoutPosition();
            itemClick(pos);
        }

        public void itemClick(int posicion) {
            Log.d(TAG, "CLIKC EN LA VISTA");
            Movie movie = moviesArrayList.get(posicion);
        }
    }
}
