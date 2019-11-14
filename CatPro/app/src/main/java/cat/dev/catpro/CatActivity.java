package cat.dev.catpro;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Arrays;
import java.util.List;

public class CatActivity extends AppCompatActivity {

    public static String intent_id= "id";
    public static String intent_name= "name";
    private String id;
    private TextView tvLevle;
    private TextView tvLink;
    private TextView tvName;
    private TextView tvTemperament;
    private TextView tvWeight;
    private TextView tvOrigin;
    private TextView tvLifeSpan;
    private ImageView ivBreed;
    private TextView tvDescription;
    private ProgressDialog dialog;
    private Button goFavorites;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat);
        Intent intent = getIntent();
        id = intent.getStringExtra(intent_id);
         name = intent.getStringExtra(intent_name);
        dialog = new ProgressDialog(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle(name);
        tvLevle = findViewById(R.id.tv_level);
        tvLink = findViewById(R.id.tv_link);
        tvName = findViewById(R.id.tv_name);
        tvTemperament = findViewById(R.id.tv_Temperament);
        tvWeight = findViewById(R.id.tv_weight);
        tvOrigin = findViewById(R.id.tv_Origin);
        tvLifeSpan = findViewById(R.id.tv_life_span);
        ivBreed = findViewById(R.id.breediv);
        tvDescription = findViewById(R.id.tv_Description);
        goFavorites = findViewById(R.id.go_favorites);
        requestCatById();
        goFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favorites();
            }
        });
    }

    private void favorites() {
        FavoritesHelper databaseHelper = new FavoritesHelper(this,"favorite",null,1);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("id",id);
        contentValues.put("name",name);
        long result = db.insert("favorites", null, contentValues);
        Log.e("result",result+"");
        if(result!=-1){
            Toast.makeText(this,"favorite",Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(CatDetail.Breeds breeds) {
        tvLevle.setText(breeds.getDog_friendly()+"");
        tvLink.setText(breeds.getWikipedia_url());
        tvName.setText(breeds.getName());
        tvTemperament.setText(breeds.getTemperament());
        tvWeight.setText(breeds.getWeight().getMetric());
        tvOrigin.setText(breeds.getOrigin());
        tvLifeSpan.setText(breeds.getLife_span());
        tvDescription.setText(breeds.getDescription());
    }
    private void requestCatById() {
        dialog.show();
        String url =Url.search+id;
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("The request failed.");
                dialog.dismiss();
            } };
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // response = JSON string, so you can do parsing with Gson here
                //o ThusyouneedtoconverttheresponseintoanArrayorList<>usingGson.
                Log.e("CatDetail",response);
                Gson gson = new Gson();
                CatDetail[] catArray = gson.fromJson(response, CatDetail[].class);
                List<CatDetail> cats = Arrays.asList(catArray);
                CatDetail catDetail = cats.get(0);
                Glide.with(CatActivity.this).load(catDetail.getUrl()).into(ivBreed);
                dialog.dismiss();
                updateUI(catDetail.getBreeds().get(0));
            } };

        StringRequest stringRequest =
                new StringRequest(Request.Method.GET, url, responseListener, errorListener);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
