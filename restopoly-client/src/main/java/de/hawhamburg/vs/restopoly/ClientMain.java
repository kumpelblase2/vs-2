package de.hawhamburg.vs.restopoly;

import de.hawhamburg.vs.restopoly.api.GameService;
import de.hawhamburg.vs.restopoly.responses.GameCreateResponse;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

import java.io.IOException;

public class ClientMain {
    public static void main(String[] args) throws IOException {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GameService service = retrofit.create(GameService.class);
        GameCreateResponse response = service.createGame().execute().body();
        System.out.println(response.gameid);
        System.out.println(service.getGames().execute().body().size());
    }
}
