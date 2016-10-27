package ark.kayak;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by avinash on 10/22/16.
 */
public class AirlineAdapter extends BaseAdapter {


    private Context mContext;
    private List<Airline> mAirlines;


    public AirlineAdapter(Context context, List<Airline> airlines) {
        mContext = context;
        mAirlines = airlines;
    }

    @Override
    public int getCount() {
        return mAirlines.size();
    }

    @Override
    public Airline getItem(int position) {
        return mAirlines.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Airline airline = getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.airline_list_row, parent, false);
            holder = new ViewHolder();
            holder.airlineImageView = (ImageView) convertView.findViewById(R.id.airline_image);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.airline_title);
            holder.favoriteButton = (MaterialFavoriteButton) convertView.findViewById(R.id.fav_toggle);

            convertView.setTag(holder);
            holder.favoriteButton.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.titleTextView.setText(airline.getName());

        // Trigger the download of the URL asynchronously into the image view.
        Picasso.with(mContext)
                .load("http://www.kayak.com"+airline.getLogoUrl())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.airlineImageView);

        holder.favoriteButton.setFavorite(airline.isFavourite(), false);

        holder.favoriteButton.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
            @Override
            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {

                if (buttonView.isShown()) {
                    Log.d("LOG_TAG", "Button Tag: " + position);
                        airline.setFavourite(!airline.isFavourite());
                        Log.d("LOG_TAG", "Position: " + position + " Favourite: " + airline.isFavourite());
                }

            }
        });


        return convertView;
    }


    static class ViewHolder {
        ImageView airlineImageView;
        TextView titleTextView;
        MaterialFavoriteButton favoriteButton;
    }

}
