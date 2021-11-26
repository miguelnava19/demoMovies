package com.example.moviesdemo.rest;

import com.example.moviesdemo.models.Response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {


    @GET("/3/{type}/{category}")
    public Call<Response> getFromCategory(
            @Path("type") String type,
            @Path("category") String category,
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("page") int page
    );



    @GET("/3/{type}/{id}")
    public Call<Response> getDetail(
            @Path("type") String type,
            @Path("id") int id,
            @Query("api_key") String api_key,
            @Query("page") int page
    );

    @GET("/3/{type}/{id}/recommendations")
    public Call<Response> getRecommendation(
            @Path("type") String type,
            @Path("id") int id,
            @Query("api_key") String api_key,
            @Query("page") int page
    );

    @GET("/3/discover/{type}")
    public Call<Response> findAll(
            @Path("type") String type,
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("page") int page
    );
}


