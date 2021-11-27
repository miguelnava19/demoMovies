package com.example.moviesdemo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesdemo.activity.DetalleActivity;
import com.example.moviesdemo.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {
    private final String TAG = this.getClass().getCanonicalName();
    private Context context;
    private List<Movie> movieList;

    public ListAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
//        Log.i(TAG, " movieList --> " + movieList);
    }

    @NonNull
    @Override
    public ListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.item, parent, false);
        ListAdapter.MyViewHolder holder = new ListAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.MyViewHolder holder, int position) {
        final Movie movie = movieList.get(position);
        Picasso.get().load("https://image.tmdb.org/t/p/w500" + movie.getPosterPath()).into(holder.imageView);

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
            Log.i(TAG, "CLIKC EN LA VISTA img --> https://image.tmdb.org/t/p/w500/" + movie.getPosterPath());
            Intent intent = new Intent(context, DetalleActivity.class);
            intent.putExtra("movie", movie);
            context.startActivity(intent);
        }
    }
}
