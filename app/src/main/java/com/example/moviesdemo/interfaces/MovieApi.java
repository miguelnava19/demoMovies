package com.example.moviesdemo.interfaces;

import com.example.moviesdemo.models.Movie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MovieApi {
    @GET("discover/tv")
    public Call<Movie> findAll();

    @GET("discover/tv/{id}")
    public Call<Movie> find(@Path("id") String id);
}
