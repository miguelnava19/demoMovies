package com.example.moviesdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.moviesdemo.interfaces.MovieApi;
import com.example.moviesdemo.models.Movie;
import com.novoda.merlin.Bindable;
import com.novoda.merlin.Connectable;
import com.novoda.merlin.Disconnectable;
import com.novoda.merlin.Merlin;
import com.novoda.merlin.NetworkStatus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements Connectable, Disconnectable, Bindable {
    private Merlin merlin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        merlin = new Merlin.Builder().withConnectableCallbacks().withDisconnectableCallbacks()
                .withBindableCallbacks().build(this);
        merlin.registerBindable(this);
        merlin.registerConnectable(this);
        merlin.registerDisconnectable(this);
    }

    private void findAll(int numPage) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        MovieApi movieApi = retrofit.create(MovieApi.class);
        Call<Movie> call = movieApi.findAll();
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                try {
                    if (response.isSuccessful()) {

                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

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
        findAll(1);
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