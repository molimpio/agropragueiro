package br.net.olimpiodev.agropragueiro.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PrevisaoTempoApi {

    @GET("weather")
    Call<ResponseBody> getPrevisaoTempo(@Query("key") String key,
                                        @Query("city_name") String cidade);
}
