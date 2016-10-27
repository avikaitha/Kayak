package ark.kayak;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.gson.Gson;

import java.util.HashSet;

public class DetailActivity extends AppCompatActivity {

    private Airline airline;
    private HashSet<String> fav_set;
    private SharedPreferences favourites;
    private TextView phoneNoTextView;
    private TextView siteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        String gsonAirline = intent.getStringExtra(MainActivity.AIRLINE_SELECTED);
        favourites = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        fav_set = (HashSet<String>) favourites.getStringSet(MainActivity.FAVOURITES_KEY,null);
        Gson gS = new Gson();
        airline = gS.fromJson(gsonAirline, Airline.class);
        setTitle(airline.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        phoneNoTextView = (TextView) findViewById(R.id.phoneNo);
        siteTextView = (TextView) findViewById(R.id.site);

        if(airline.getPhone() != "") {
            phoneNoTextView.setText(airline.getPhone());
            phoneNoTextView.setTextColor(Color.BLUE);
            phoneNoTextView.setPaintFlags(phoneNoTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            phoneNoTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String phoneNo = airline.getPhone();

                    String uri = "tel:" + phoneNo.trim();
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);

                }
            });
        }

        else {
            phoneNoTextView.setText("N/A");
        }

        if(airline.getSite() != "") {
            siteTextView.setText(airline.getSite());
            siteTextView.setTextColor(Color.BLUE);
            siteTextView.setPaintFlags(phoneNoTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            siteTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "https://"+airline.getSite();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }
            });
        }
        else {
            siteTextView.setText("N/A");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        RelativeLayout fav_toggle_layout = (RelativeLayout) menu.findItem(R.id.action_fav_star_toggle).getActionView();
        MaterialFavoriteButton fav_toggle = (MaterialFavoriteButton) fav_toggle_layout.findViewById(R.id.action_fav_star);
        fav_toggle.setFavorite(airline.isFavourite(),false);
        fav_toggle.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
            @Override
            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {

                if (favorite) {
                    if(!fav_set.contains(airline.getName())) {
                        fav_set.add(airline.getName());
                        Log.d("LOG_TAG", "Added data Detail");
                    }

                } else {

                    if(fav_set.contains(airline.getName())) {
                        fav_set.remove(airline.getName());
                        Log.d("LOG_TAG", "Removed data Detail");
                    }
                }



            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext()).edit();
        editor.putStringSet(MainActivity.FAVOURITES_KEY, fav_set);
        editor.apply();
        Log.d("LOG_TAG", "WRitten data Detail "+(PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext())).getStringSet(MainActivity.FAVOURITES_KEY,null).size());
    }

}
