package com.example.moviesdemo.rest;

import com.example.moviesdemo.models.Movie;
import com.example.moviesdemo.models.Response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {


    @GET("/3/movie/{category}")
    public Call<Response> getMovie(
            @Path("category") String category,
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("/3/discover/movie")
    public Call<Response> findAll(
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("discover/tv/{id}")
    public Call<Response> find(@Path("id") String id);
}


