package de.hawhamburg.vs.restopoly;

import de.hawhamburg.vs.restopoly.api.GameService;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientMain {
    public static void main(String[] args) throws IOException {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GameService service = retrofit.create(GameService.class);

        System.out.print("[C]reate or [j]oin game? : ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String answer = reader.readLine();
        int gameid;
        if(answer.equals("j")){
            System.out.print("Enter game id: ");
            gameid = Integer.parseInt(reader.readLine());
        } else {
            gameid = service.createGame().execute().body().gameid;
            System.out.println("Created game " + gameid);
        }

        System.out.print("Enter name: ");
        String name = reader.readLine();
        Response response = service.registerPlayer(gameid, name).execute();
        if(response.code() == 200) {
            System.out.println("register successful");
        } else {
            System.out.println("Error. Code: " + response.code());
        }
    }
}
