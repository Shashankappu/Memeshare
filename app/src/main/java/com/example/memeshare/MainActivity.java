package com.example.memeshare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    ImageView imageView ;
    Button next,share;
    ProgressBar progressBar;
    String currentUrl;
    ImageButton like;
    boolean t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        share = findViewById(R.id.sharebtn);
        next = findViewById(R.id.nextbtn);
        imageView = findViewById(R.id.imageview);
        progressBar = findViewById(R.id.progressbar);
        like = findViewById(R.id.likebtn);
        loadMeme();
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                 intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,"Hey Checkout this Meme "+currentUrl);
                Intent chooser = Intent.createChooser(intent,"Share this Using");
                startActivity(chooser);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // next meme
                like.setImageResource(R.mipmap.whiteheart);
                loadMeme();

            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!t){
                like.setImageResource(R.mipmap.redheart);
                t=true;
                } else {
                    like.setImageResource(R.mipmap.whiteheart);
                    t = false;
                }
            }
        });
    }
    private void loadMeme(){
        progressBar.setVisibility(imageView.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://meme-api.herokuapp.com/gimme";

// Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String url =null;
                        try {
                            currentUrl = response.getString("url");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Display the first 500 characters of the response string.
                            Glide.with(MainActivity.this).load(currentUrl).listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                   progressBar.setVisibility(imageView.VISIBLE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    progressBar.setVisibility(imageView.INVISIBLE);
                                    return false;
                                }
                            }).into(imageView);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
    }

}