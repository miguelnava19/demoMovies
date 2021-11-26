package com.example.moviesdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.moviesdemo.interfaces.MovieApi;
import com.example.moviesdemo.models.Movie;
import com.example.moviesdemo.models.Response;
import com.example.moviesdemo.rest.ApiClient;
import com.example.moviesdemo.rest.ApiInterface;
import com.novoda.merlin.Bindable;
import com.novoda.merlin.Connectable;
import com.novoda.merlin.Disconnectable;
import com.novoda.merlin.Merlin;
import com.novoda.merlin.NetworkStatus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements Connectable, Disconnectable, Bindable {
    private Merlin merlin;
    private RecyclerViewAdapter recyclerViewAdapter;

    String API_KEY = "63d5d9e3339a7cd0bb24b220d0473ab5";
    String LANGUAGE = "en-US";
    String CATEGORY = "popular";
    int PAGE = 1;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        merlin = new Merlin.Builder().withConnectableCallbacks().withDisconnectableCallbacks()
                .withBindableCallbacks().build(this);
        merlin.registerBindable(this);
        merlin.registerConnectable(this);
        merlin.registerDisconnectable(this);

        recyclerView = findViewById(R.id.recyclerFavorites);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    private void CallRetrofit() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Response> call = apiInterface.getMovie(CATEGORY, API_KEY, LANGUAGE, PAGE);
       call.enqueue(new Callback<Response>() {
           @Override
           public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
               List<Movie> movieList = response.body().getResults();
               recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, movieList);
               recyclerView.setAdapter(recyclerViewAdapter);
           }

           @Override
           public void onFailure(Call<Response> call, Throwable t) {

           }
       });
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (merlin != null) {
            merlin.bind();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (merlin != null) {
            merlin.unbind();
        }
    }

    @Override
    public void onBind(NetworkStatus networkStatus) {
        if (!networkStatus.isAvailable()) {
            onDisconnect();
        }
    }

    @Override
    public void onConnect() {
        Toast.makeText(getApplication(), "Conectado a Internet :)", Toast.LENGTH_SHORT).show();
        CallRetrofit();
    }

    @Override
    public void onDisconnect() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //TODO apartado para ocupar sin conexión a internet
                Toast.makeText(getApplication(), "Desconectado a Internet :(", Toast.LENGTH_SHORT).show();
            }
        });
    }
}