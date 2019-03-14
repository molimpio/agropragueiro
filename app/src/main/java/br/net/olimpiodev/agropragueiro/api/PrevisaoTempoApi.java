package br.net.olimpiodev.agropragueiro.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PrevisaoTempoApi {

    //"https://api.hgbrasil.com/weather?key=6ff81ae8&city_name=Campinas,SP"
    @GET("weather")
    Call<ResponseBody> getPrevisaoTempo(@Query("key") String key,
                                        @Query("city_name") String cidade);
}
