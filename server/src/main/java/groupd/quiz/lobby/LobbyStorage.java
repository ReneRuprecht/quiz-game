package groupd.quiz.lobby;

import groupd.quiz.game.Game;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains all the created and running games
 */
public class LobbyStorage {
    private static Map<String, Game> games;
    private static LobbyStorage instance;


    public LobbyStorage() {
        games = new HashMap<>();
    }

    public static synchronized LobbyStorage getInstance() {
        if (instance == null) {
            instance = new LobbyStorage();
        }
        return instance;
    }

    public Map<String, Game> getGames() {
        return games;
    }

    public void setGame(Game game) {
        games.put(game.getGameId(), game);
    }

    public Map<String, Game> deleteGame(String gameId) {
        getGames().remove(gameId);
        return getGames();
    }
}
