package de.hawhamburg.vs.restopoly.api;

import de.hawhamburg.vs.restopoly.data.dto.Roll;
import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by tim on 18.11.15.
 */
public interface DiceService {
    @GET("/dice")
    public Call<Roll> roll();
}
