package com.example.moviesdemo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviesdemo.R;
import com.example.moviesdemo.ListAdapter;
import com.example.moviesdemo.models.Movie;
import com.example.moviesdemo.models.Response;
import com.example.moviesdemo.rest.ApiClient;
import com.example.moviesdemo.rest.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.novoda.merlin.Bindable;
import com.novoda.merlin.Connectable;
import com.novoda.merlin.Disconnectable;
import com.novoda.merlin.Merlin;
import com.novoda.merlin.NetworkStatus;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity implements Connectable, Disconnectable, Bindable {
    private Merlin merlin;
    private ListAdapter adapter;
    private String TAG = getClass().getCanonicalName();
    String API_KEY = "63d5d9e3339a7cd0bb24b220d0473ab5";
    String LANGUAGE = "en-US";
    String CATEGORY = "popular";
    int PAGE = 1;
    int idMRec = 551;
    int idTvRec = 90462;
    TextView txtMFav;
    TextView txtTVFav;
    RecyclerView recyclerViewMF;
    RecyclerView recyclerViewMRec;
    RecyclerView recyclerViewMRated;

    RecyclerView recyclerViewTVF;
    RecyclerView recyclerViewTVRec;
    RecyclerView recyclerViewTVRated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        merlin = new Merlin.Builder().withConnectableCallbacks().withDisconnectableCallbacks()
                .withBindableCallbacks().build(this);
        merlin.registerBindable(this);
        merlin.registerConnectable(this);
        merlin.registerDisconnectable(this);

        txtMFav = findViewById(R.id.txt1);
        txtTVFav = findViewById(R.id.txt4);

        recyclerViewMF = getRecycler(R.id.list_movie_favorite);
        recyclerViewMRec = getRecycler(R.id.list_movie_recommendation);
        recyclerViewMRated = getRecycler(R.id.list_movie_rated);

        recyclerViewTVF = getRecycler(R.id.list_tv_favorite);
        recyclerViewTVRec = getRecycler(R.id.list_tv_recommendation);
        recyclerViewTVRated = getRecycler(R.id.list_tv_rated);
    }

    private RecyclerView getRecycler(int id) {
        RecyclerView recyclerView = findViewById(id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        return recyclerView;
    }

    private void callRetrofitMovie(String nameFile, Call<Response> call, RecyclerView recyclerView) {
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                List<Movie> movieList = response.body().getResults();
                String json = new Gson().toJson(movieList);
                saveJson(json, nameFile);
                adapter = new ListAdapter(MainActivity.this, movieList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });
    }

    private void getRecommendations(String type, RecyclerView recyclerView, int id) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Response> call = apiInterface.getRecommendation(type, id, API_KEY, PAGE);
        callRetrofitMovie(type + "_recommendation.txt", call, recyclerView);
    }

    private void getRated(String type, RecyclerView recyclerView) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Response> call = apiInterface.getFromCategory(type, "top_rated", API_KEY, LANGUAGE, PAGE);
        callRetrofitMovie(type + "_rated.txt", call, recyclerView);
    }

    public void saveJson(String text, String fileName) {
        FileOutputStream fos = null;

        try {
            fos = openFileOutput(fileName, MODE_PRIVATE);
            fos.write(text.getBytes());

            /*Toast.makeText(this, "Saved to " + getFilesDir() + "/" + fileName,
                    Toast.LENGTH_LONG).show();*/
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String loadJson(String fileName) {
        FileInputStream fis = null;

        try {
            File file = new File(getFilesDir() + "/" + fileName);
            if (!file.exists()) {
                return "";
            }
            fis = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }
            return sb.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    public void getListFavorite(String nameFile, RecyclerView recyclerView, TextView textView) {
        String json = loadJson(nameFile);
        Gson gson = new Gson();
        Type type = new TypeToken<List<Movie>>() {
        }.getType();
        List<Movie> movieList = gson.fromJson(json, type);
        if (movieList != null) {
            if (nameFile.contains("movie"))
                idMRec = movieList.get(0).getId();
            else
                idTvRec = movieList.get(0).getId();
            adapter = new ListAdapter(MainActivity.this, movieList);
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        }
    }


    public void getListRecommendation(String nameFile, RecyclerView recyclerView) {
        String json = loadJson(nameFile);
        Gson gson = new Gson();
        Type type = new TypeToken<List<Movie>>() {
        }.getType();
        List<Movie> movieList = gson.fromJson(json, type);
        if (movieList != null) {
            adapter = new ListAdapter(MainActivity.this, movieList);
            recyclerView.setAdapter(adapter);
        } else
            recyclerView.setVisibility(View.GONE);

    }


    public void getListRated(String nameFile, RecyclerView recyclerView) {
        String json = loadJson(nameFile);
        Gson gson = new Gson();
        Type type = new TypeToken<List<Movie>>() {
        }.getType();
        List<Movie> movieList = gson.fromJson(json, type);
        if (movieList != null) {
            adapter = new ListAdapter(MainActivity.this, movieList);
            recyclerView.setAdapter(adapter);
        } else
            recyclerView.setVisibility(View.GONE);

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
        getListFavorite("movie_favorite.txt", recyclerViewMF, txtMFav);
        getListFavorite("tv_favorite.txt", recyclerViewTVF, txtTVFav);

        getRecommendations("movie", recyclerViewMRec, idMRec);
        getRated("movie", recyclerViewMRated);

        getRecommendations("tv", recyclerViewTVRec, idTvRec);
        getRated("tv", recyclerViewTVRated);
    }

    @Override
    public void onDisconnect() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //TODO apartado para ocupar sin conexión a internet
                Toast.makeText(getApplication(), "Desconectado a Internet :(", Toast.LENGTH_SHORT).show();
                getListFavorite("movie_favorite.txt", recyclerViewMF, txtMFav);
                getListFavorite("tv_favorite.txt", recyclerViewTVF, txtTVFav);

                getListRecommendation("movie_recommendation.txt", recyclerViewMRec);
                getListRecommendation("tv_recommendation.txt", recyclerViewTVRec);

                getListRated("movie_rated.txt", recyclerViewMRated);
                getListRated("tv_rated.txt", recyclerViewTVRated);
            }
        });
    }
}