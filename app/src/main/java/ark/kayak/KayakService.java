package ark.kayak;

/**
 * Created by avinash on 10/21/16.
 */

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by echessa on 6/18/15.
 */
public interface KayakService {

    @GET("/h/mobileapis/directory/airlines")
    public void getAirlines(Callback<List<Airline>> cb);

//    @GET("/playlist.php?fct=getfromtag&popularitymin=50&format=json")
//    public void getNeuroTracks(@Query("tag") String mood, Callback<JsonElement> musicoverycb);



}

