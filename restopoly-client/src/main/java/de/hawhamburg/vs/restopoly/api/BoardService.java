package de.hawhamburg.vs.restopoly.api;

import com.squareup.okhttp.ResponseBody;
import de.hawhamburg.vs.restopoly.data.model.Player;
import de.hawhamburg.vs.restopoly.data.responses.ThrowDTO;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface BoardService {
    @POST("/boards/{gameid}")
    Call<ResponseBody> createBoard(@Path("gameid") int gameId);

    @POST("/boards/{gameid}/players/{playerid}/roll")
    Call<ResponseBody> move(@Path("gameid") int gameId, @Path("playerid") String player, @Body ThrowDTO thro);

    @PUT("/boards/{gameid}/players/{playerid}")
    Call<ResponseBody> place(@Path("gameid") int gameId, @Path("playerid") String playerName, @Body Player player);
}
