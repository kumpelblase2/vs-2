package de.hawhamburg.vs.restopoly.data.dto;

import de.hawhamburg.vs.restopoly.data.model.Components;
import de.hawhamburg.vs.restopoly.data.model.Game;

public class GameDTO {
    private int gameid;
    private Components components;
    private String players;
    private boolean started = false;

    public GameDTO(int gameid, Components components, String players, boolean started) {
        this.gameid = gameid;
        this.components = components;
        this.players = players;
        this.started = started;
    }

    public GameDTO() {
    }

    public GameDTO(Game current) {
        this.gameid = current.getGameid();
        this.components = current.getComponents();
        this.players = "/games/" + this.gameid + "/players";
        this.started = current.isStarted();
    }

    public int getGameid() {
        return gameid;
    }

    public void setGameid(int gameid) {
        this.gameid = gameid;
    }

    public Components getComponents() {
        return components;
    }

    public void setComponents(Components components) {
        this.components = components;
    }

    public String getPlayers() {
        return players;
    }

    public void setPlayers(String players) {
        this.players = players;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameDTO gameDTO = (GameDTO) o;

        if (gameid != gameDTO.gameid) return false;
        if (started != gameDTO.started) return false;
        if (components != null ? !components.equals(gameDTO.components) : gameDTO.components != null) return false;
        return players != null ? players.equals(gameDTO.players) : gameDTO.players == null;

    }

    @Override
    public int hashCode() {
        int result = gameid;
        result = 31 * result + (components != null ? components.hashCode() : 0);
        result = 31 * result + (players != null ? players.hashCode() : 0);
        result = 31 * result + (started ? 1 : 0);
        return result;
    }

    public static class PlayerDTO {
        private String id;
        private String name;
        private String uri;
        private String ready;

        public PlayerDTO(String id, String name, String uri, String ready) {
            this.id = id;
            this.name = name;
            this.uri = uri;
            this.ready = ready;
        }

        public PlayerDTO() {
        }

        public PlayerDTO(int gameId, Game.Player current) {
            this.id = current.getId();
            this.name = current.getName();
            this.uri = current.getUri();
            this.ready = "/games/" + gameId + "/players/" + this.id + "/ready";
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getReady() {
            return ready;
        }

        public void setReady(String ready) {
            this.ready = ready;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PlayerDTO playerDTO = (PlayerDTO) o;

            if (id != null ? !id.equals(playerDTO.id) : playerDTO.id != null) return false;
            if (name != null ? !name.equals(playerDTO.name) : playerDTO.name != null) return false;
            if (uri != null ? !uri.equals(playerDTO.uri) : playerDTO.uri != null) return false;
            return ready != null ? ready.equals(playerDTO.ready) : playerDTO.ready == null;

        }

        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            result = 31 * result + (uri != null ? uri.hashCode() : 0);
            result = 31 * result + (ready != null ? ready.hashCode() : 0);
            return result;
        }
    }
}
