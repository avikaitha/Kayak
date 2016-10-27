package ark.kayak;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private AirlineAdapter mAirlineAdapter;
    private List<Airline> mFavAirlines;
    private List<Airline> mAllAirline;
    private boolean isFavToggle = false;
    private HashSet<String> fav_set;

    public static final String AIRLINE_SELECTED = "ark.kayak.airline";
    public static final String POSITION_SELECTED = "ark.kayak.position";
    public static final String FAVOURITES_KEY = "ark.kayak.favourites";
    private SharedPreferences favourites;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mListView = (ListView) findViewById(R.id.airline_list);
        favourites = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        fav_set = (HashSet<String>) favourites.getStringSet(FAVOURITES_KEY,null);


        KayakService mKayakService = Kayak.getService();
        mKayakService.getAirlines(new Callback<List<Airline>>() {
            @Override
            public void success(final List<Airline> airlines, Response response) {
//                Log.d("LOG_TAG",(airlines.get(0).getName()));
                mAllAirline = airlines;

                if(fav_set != null) {
                    Log.d("LOG_TAG", "Test: "+fav_set.size());
                    for (int i = 0; i < mAllAirline.size(); i++) {
                        Airline airline = mAllAirline.get(i);
                        if(fav_set.contains(airline.getName())) {
                            airline.setFavourite(true);
                        }

                        else {
                            airline.setFavourite(false);
                        }
                    }
                }

                else {
                    Log.d("LOG_TAG", "Null");
                    fav_set = new HashSet<>();
                }
                mAirlineAdapter = new AirlineAdapter(MainActivity.this,mAllAirline);
                mListView.setAdapter(mAirlineAdapter);

                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                        Gson gS = new Gson();
                        Airline airline;
                        if(isFavToggle) {
                            airline = mFavAirlines.get(position);
                        }
                        else {
                            airline = mAllAirline.get(position);
                        }

                        String airlineGson = gS.toJson(airline);
                        intent.putExtra(AIRLINE_SELECTED,airlineGson);
                        intent.putExtra(POSITION_SELECTED,position);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("LOG_TAG Error",error.toString());

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        RelativeLayout fav_toggle_layout = (RelativeLayout) menu.findItem(R.id.action_fav_toggle).getActionView();
        MaterialFavoriteButton fav_toggle = (MaterialFavoriteButton) fav_toggle_layout.findViewById(R.id.action_fav);
        fav_toggle.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
            @Override
            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                if (favorite) {
                    isFavToggle = true;
                    mFavAirlines = new ArrayList<>();
                    for (int i = 0; i < mAllAirline.size(); i++) {
                        if (mAllAirline.get(i).isFavourite()) {
                            mFavAirlines.add(mAllAirline.get(i));
                        }

                    }
                    mAirlineAdapter = new AirlineAdapter(MainActivity.this, mFavAirlines);
//                    Toast.makeText(MainActivity.this, "Test", Toast.LENGTH_SHORT).show();
                } else {
                    isFavToggle = false;
                    mAirlineAdapter = new AirlineAdapter(MainActivity.this, mAllAirline);

                }
                mListView.setAdapter(mAirlineAdapter);

            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();

//        Log.d("LOG_TAG", "Old data Detail " + (PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext())).getStringSet(FAVOURITES_KEY, null).size());

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext()).edit();
        fav_set = new HashSet<>();
        for (int i = 0; i < mAllAirline.size(); i++) {
            Airline airline = mAllAirline.get(i);
            if(airline.isFavourite()) {
                fav_set.add(airline.getName());
            }

//            Log.d("LOG_TAG",""+airline.isFavourite()+": "+mAllAirline.get(i).isFavourite());

        }

        editor.putStringSet(FAVOURITES_KEY, fav_set);
        editor.apply();

        SharedPreferences temp = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());

        Log.d("LOG_TAG", "Written data "+(temp.getStringSet(FAVOURITES_KEY,null)).size());
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mAirlineAdapter != null) {
            favourites = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
            fav_set = (HashSet<String>) favourites.getStringSet(FAVOURITES_KEY,null);
            for (int i = 0; i < mAllAirline.size(); i++) {
                Airline airline = mAllAirline.get(i);
                if(fav_set.contains(airline.getName())) {
                    airline.setFavourite(true);
                }

                else {
                    airline.setFavourite(false);
                }
//                Log.d("LOG_TAG",""+airline.isFavourite()+": "+mAllAirline.get(i).isFavourite());
            }
            Log.d("LOG_TAG", "ON Resume");
            mAirlineAdapter.notifyDataSetChanged();
        }

    }
}
