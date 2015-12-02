package de.hawhamburg.vs.restopoly.api;

import com.squareup.okhttp.ResponseBody;
import de.hawhamburg.vs.restopoly.data.model.Game;
import de.hawhamburg.vs.restopoly.data.model.Player;
import de.hawhamburg.vs.restopoly.data.responses.GameCreateResponse;
import retrofit.Call;
import retrofit.http.*;

import java.util.List;

public interface GameService {
    @GET("/games")
    Call<List<Game>> getGames();

    @POST("/game")
    Call<GameCreateResponse> createGame();

    @PUT("/games/{gameid}/players/{playerid}")
    Call<ResponseBody> registerPlayer(@Path("gameid") int gameId, @Path("playerid") String player);

    @PUT("/games/{gameid}/players/{playerid}/ready")
    Call<ResponseBody> setReady(@Path("gameid") int gameId, @Path("playerid") String player);

    @GET("/games/{gameid}/players/{playerid}/ready")
    boolean checkPlayerReady(@Path("gameid") int gameId, @Path("playerid") String player);

    @GET("/games/{gameid}/players")
    Call<List<Player>> getPlayerOfGame(@Path("gameid") int gameId);

    @GET("/games/{gameid}/players/current")
    Call<Player> getCurrentPlayer(@Path("gameid") int gameId);

    @PUT("/games/{gameid}/players/turn")
    Call<ResponseBody> acquireMutex(@Path("gameid") int gameId, @Query("player") String playerName, @Body Player player);

    @DELETE("/games/{gameid}/players/turn")
    Call<ResponseBody> releaseMutex(@Path("gameid") int gameId, @Query("player") String player);

    @GET("/games/{gameid}/players/turn")
    Call<Player> getPlayerHoldingMutex(@Path("gameid") int gameId);
}
