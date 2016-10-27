package ark.kayak;

import retrofit.RestAdapter;

/**
 * Created by avinash on 10/22/16.
 */
public class Kayak {

    private static final RestAdapter REST_ADAPTER = new RestAdapter.Builder().setEndpoint("https://www.kayak.com/").build();
    private static final KayakService SERVICE = REST_ADAPTER.create(KayakService.class);

    public static KayakService getService() {
        return SERVICE;
    }
}
