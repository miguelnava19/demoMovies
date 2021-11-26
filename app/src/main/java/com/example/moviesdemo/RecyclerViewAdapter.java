package com.example.moviesdemo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesdemo.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private final String TAG = this.getClass().getCanonicalName();
    private Context context;
    private List<Movie> movieList;

    public RecyclerViewAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
        Log.i(TAG, " movieList --> " + movieList);
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.item, parent, false);
        RecyclerViewAdapter.MyViewHolder holder = new RecyclerViewAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyViewHolder holder, int position) {
        final Movie movie = movieList.get(position);
        Picasso.get().load("https://image.tmdb.org/t/p/w500/" + movie.getPosterPath()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = itemView.findViewById(R.id.image_view);
        }

        @Override
        public void onClick(View v) {
            int pos = getLayoutPosition();
            final Movie movie = movieList.get(pos);
            Log.i(TAG, "CLIKC EN LA VISTA img --> " + movie.getBackdropPath());
        }
    }
}
