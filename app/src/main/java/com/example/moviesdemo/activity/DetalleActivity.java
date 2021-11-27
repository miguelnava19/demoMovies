package com.example.moviesdemo.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moviesdemo.R;
import com.example.moviesdemo.models.Movie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DetalleActivity extends AppCompatActivity {
    Bundle bundle;
    ImageView image;
    TextView txtTitle;
    EditText editText;
    Button btnFavorite;
    private String TAG = getClass().getCanonicalName();
    Movie movie;
    String json = "";
    List<Movie> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        bundle = getIntent().getExtras();
        image = findViewById(R.id.image_movie);
        txtTitle = findViewById(R.id.txt_title);
        editText = findViewById(R.id.overview);
        btnFavorite = findViewById(R.id.btnFavorite);
        movie = (Movie) bundle.getSerializable("movie");
        Picasso.get().load("https://image.tmdb.org/t/p/w500" + movie.getPosterPath()).into(image);
        txtTitle.setText(movie.getOriginalTitle() != null ? movie.getOriginalTitle() : movie.getOriginalName());
        editText.setText(movie.getOverview());
        String nameFile = movie.getOriginalTitle() != null ? "movie_favorite.txt" : "tv_favorite.txt";
        json = loadJson(nameFile);
        Log.i(TAG, "nameFile " + nameFile);
        Log.i(TAG, "json favorite " + json);
        if (!json.equals("")) {
            /*JsonParser jsonParser = new JsonParser();
            movieList = (List<Movie>) jsonParser.parse(json);*/
            Gson gson = new Gson();
            Type type = new TypeToken<List<Movie>>(){}.getType();
            movieList = gson.fromJson(json, type);

        } else
            movieList = new ArrayList<Movie>();

        Log.i(TAG, "findMovie " + findMovie(movie.getId(), movieList));

        if (!json.equals("") && findMovie(movie.getId(), movieList) != null) {
            btnFavorite.setVisibility(View.GONE);
        }
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "clicked btn favoritos");
                btnFavorite.setVisibility(View.GONE);
                movieList.add(movie);
                String json = new Gson().toJson(movieList);
                saveJson(json, nameFile);
                Toast.makeText(getApplication(), "Added Favorite", Toast.LENGTH_SHORT).show();
            }
        });
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
            File file = new File( getFilesDir() + "/" + fileName);
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

    public Movie findMovie(Integer id, List<Movie> moviesList) {

        for (Movie movie : moviesList) {
            if (movie.getId().equals(id)) {
                return movie;
            }
        }
        return null;
    }
}
