package de.hawhamburg.vs.restopoly;

import de.hawhamburg.vs.restopoly.api.BoardService;
import de.hawhamburg.vs.restopoly.api.DiceService;
import de.hawhamburg.vs.restopoly.api.GameService;
import de.hawhamburg.vs.restopoly.data.model.Player;
import de.hawhamburg.vs.restopoly.data.dto.GameCreateResponse;
import de.hawhamburg.vs.restopoly.data.dto.Roll;
import de.hawhamburg.vs.restopoly.data.dto.ThrowDTO;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientMain {

    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("[C]reate or [j]oin game? : ");
        int gameid;
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost:4567")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GameService service = retrofit.create(GameService.class);
        DiceService dice = retrofit.create(DiceService.class);
        BoardService board = retrofit.create(BoardService.class);
        String answer = reader.readLine();
        if(answer.equals("j")){
            System.out.print("Enter game id: ");
            gameid = Integer.parseInt(reader.readLine());
        } else {
            Response<GameCreateResponse> gameResponse = service.createGame().execute();
            if(gameResponse.code() != 201) {
                System.out.println(gameResponse.errorBody().string());
                return;
            } else {
                gameid = gameResponse.body().gameid;
                System.out.println("Created game " + gameid);
            }
        }

        System.out.print("Enter name: ");
        String name = reader.readLine();
        Response response = service.registerPlayer(gameid, name).execute();
        if(response.code() == 201) {
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

        board.createBoard(gameid).execute();
        response = board.place(gameid, name, new Player(name, name, null, 0, false, gameid)).execute();
        if(!response.isSuccess()) {
            System.out.println("Couldn't place on board :( " + response.code());
            System.out.println(response.errorBody().string());
            return;
        }

        while(true) {
            boolean ownTurn;
            do {
                ownTurn = service.getCurrentPlayer(gameid).execute().body().getId().equals(name);
                Thread.sleep(1000);
            } while (!ownTurn);

            response = service.acquireMutex(gameid, name, new Player(name, name, null, 0, true, gameid)).execute();
            if(response.code() != 200 && response.code() != 201) {

                System.out.println("Couldn't acquire mutex.");
                continue;
            }

            System.out.println("Acquired lock, it's your turn.");
            Roll roll1 = dice.roll().execute().body();
            Roll roll2 = dice.roll().execute().body();
            ThrowDTO thro = new ThrowDTO(roll1, roll2);
            response = board.move(gameid, name, thro).execute();
            if(response.isSuccess()) {
                System.out.println("Moved " + (roll1.number + roll2.number) + " fields");
                service.setReady(gameid, name).execute();
            } else {
                System.out.println(response.code());
                System.out.println(response.errorBody().string());
            }
        }
    }
}
