package groupd.quiz.game.response;

public enum GamePlayResponseHeaderMessage {
    // game
    game_not_found,
    game_info,
    game_already_finished,
    game_not_ready,
    game_already_full,
    game_over,

    // answers
    answer_incorrect,
    answer_correct,

    // user
    user_not_found,
    username_invalid,
    user_left,

    // websocket
    user_joined,
    game_ready

}
