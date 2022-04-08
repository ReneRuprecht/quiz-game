package groupd.quiz.game;


import com.fasterxml.jackson.annotation.JsonIgnore;
import groupd.quiz.game.response.GameStatus;
import groupd.quiz.question.Question;
import groupd.quiz.scores.Score;
import groupd.quiz.user.User;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Slf4j
@Getter
public class Game {
    private String gameId;
    private User playerOne;
    private User playerTwo;
    private String winner;
    private final Date date;
    @JsonIgnore
    private List<Question> listOfQuestions = new ArrayList<>();
    private Question currentQuestion = null;
    private GameStatus gameStatus;
    private HashMap<String, Boolean> readyStates = new HashMap<>();


    public Game() {
        this.date = new Date(System.currentTimeMillis());
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }


    public boolean isQuestionsListEmpty() {
        return this.getListOfQuestions().size() == 0;
    }

    public boolean playerTwoExists() {
        return this.getPlayerTwo() != null;
    }

    public boolean isAnswerCorrect(String answer) {
        return this.getCurrentQuestion().getCorrectAnswer().equals(answer);
    }

    public void setListOfQuestions(List<Question> listOfQuestions) {
        this.listOfQuestions = listOfQuestions;
    }


    public User getMyUser(String username) {
        User user;
        if (this.getPlayerOne().getUsername().equals(username)) {
            user = this.getPlayerOne();
        } else if (this.getPlayerTwo().getUsername().equals(username)) {
            user = this.getPlayerTwo();
        } else {
            return null;
        }
        return user;

    }


    public void createGame(User playerOne) {

        this.setGameId(UUID.randomUUID().toString());
        this.setPlayerOne(playerOne);
        this.setGameStatus(GameStatus.OPEN);

    }

    public void addPlayerTwoToGame(User playerTwo) {
        this.setPlayerTwo(playerTwo);
        this.setGameStatus(GameStatus.NOT_READY);
    }


    private void setPlayerOne(User playerOne) {
        this.playerOne = playerOne;
        this.playerOne.getScoreObject().setCurrentScoreValue(0);
        this.readyStates.put(playerOne.getUsername(), false);
    }

    private void setPlayerTwo(User playerTwo) {
        if (playerTwo == null) {
            this.readyStates.remove(this.playerTwo.getUsername());
            this.playerTwo = null;
            return;
        }
        this.playerTwo = playerTwo;
        this.playerTwo.getScoreObject().setCurrentScoreValue(0);
        this.readyStates.put(playerTwo.getUsername(), false);
    }


    public void setPlayerReady(String username,boolean ready) {
        this.readyStates.put(username, ready);

    }

    public boolean arePlayerReady() {
        boolean status = true;

        if (readyStates.isEmpty()) return false;
        if (readyStates.size() <= 1) return false;

        for (Boolean ready : readyStates.values()) {
            if (!ready) {
                status = false;
                break;
            }
        }
        if(!status){
            return false;
        }

        this.setGameStatus(GameStatus.STARTED);
        return true;
    }


    public void finalPrepare() {
        initPlayerCurrentScores();
        loadNextQuestion();
    }


    private void initPlayerCurrentScores() {
        playerOne.getScoreObject().setCurrentScoreValue(0);
        playerTwo.getScoreObject().setCurrentScoreValue(0);
    }

    public void loadNextQuestion() {

        Question question = listOfQuestions.get(0);
        if (question == null) {
            throw new IllegalStateException("Fragen sind leer");
        }
        listOfQuestions.remove(question);
        currentQuestion = question;
    }


    // game over related

    public User draw(User draw) {
        Score winnerScoreObject = draw.getScoreObject();


        winnerScoreObject.setTotalScore(
                winnerScoreObject.getTotalScore() +
                        winnerScoreObject.getCurrentScoreValue()
        );

        winnerScoreObject.setGamesPlayed(
                winnerScoreObject.getGamesPlayed() + 1
        );

        return draw;
    }


    public User won(User winner) {
        Score winnerScoreObject = winner.getScoreObject();

        // update won count +1
        winnerScoreObject.setWin(
                winnerScoreObject.getWin() + 1
        );

        winnerScoreObject.setTotalScore(
                winnerScoreObject.getTotalScore() +
                        winnerScoreObject.getCurrentScoreValue()
        );

        winnerScoreObject.setGamesPlayed(
                winnerScoreObject.getGamesPlayed() + 1
        );

        return winner;
    }

    public User lost(User looser) {
        Score looserScoreObject = looser.getScoreObject();

        // update lost count +1
        looserScoreObject.setLose(
                looserScoreObject.getLose() + 1
        );

        looserScoreObject.setTotalScore(
                looserScoreObject.getTotalScore() +
                        looserScoreObject.getCurrentScoreValue()
        );

        looserScoreObject.setGamesPlayed(
                looserScoreObject.getGamesPlayed() + 1
        );

        return looser;
    }


}
