package de.hawhamburg.vs.restopoly;

import de.hawhamburg.vs.restopoly.api.GameService;
import de.hawhamburg.vs.restopoly.model.Player;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientMain {
    public static void main(String[] args) throws IOException, InterruptedException {
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
            System.out.println("Ready?");
            reader.readLine();
            response = service.setReady(gameid, name).execute();
            if(response.code() == 200) {
                System.out.println("Set ready");
            } else {
                return;
            }
        } else {
            System.out.println("Error. Code: " + response.code());
            return;
        }

        while(true) {
            boolean ownTurn;
            do {
                ownTurn = service.getCurrentPlayer(gameid).execute().body().getId().equals(name);
                Thread.sleep(1000);
            } while (!ownTurn);

            response = service.acquireMutex(gameid, new Player(name, name, null, 0, true, gameid)).execute();
            if(!response.isSuccess()) {
                System.out.println("Couldn't acquire mutex.");
                continue;
            }

            System.out.println("Acquired lock, it's your turn.");
        }
    }
}
