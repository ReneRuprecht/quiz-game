import 'dart:async';
import 'dart:convert';

import 'package:app/app/enums/message_enum.dart';
import 'package:app/app/enums/winner_enum.dart';
import 'package:app/app/init_state.dart';
import 'package:app/app/models/current_question.dart';
import 'package:app/app/request/gameid_username_request.dart';
import 'package:app/app/response/websocket_response.dart';
import 'package:app/app/services/game/disconnect_service.dart';
import 'package:app/app/services/navigation_service.dart';
import 'package:app/app/services/websocket_service.dart';
import 'package:app/app/utils/websocket_message_handler.dart';
import 'package:app/app/websocket.dart';
import 'package:flutter/cupertino.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:http/http.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../../locator.dart';
import 'game_play_model.dart';

///viewmodel for the gameplay
class GamePlayViewModel extends ChangeNotifier
    implements QuizWebSocket, InitState {
  GamePlayModel gamePlayModel = GamePlayModel();
  DisconnectService disconnectService = locator<DisconnectService>();
  WebSocketService webSocketService = locator<WebSocketService>();

// is used to check if the other player is ready
  bool get isReady => gamePlayModel.ready;

  CurrentQuestion? get question => gamePlayModel.question;

  int get score => gamePlayModel.score;

  bool get isGameFinished => gamePlayModel.isGameFinished;

  String get gameOverText => gamePlayModel.gameOverWinnerText;

  WebSocketResponse? get webSocketResponse => gamePlayModel.webSocketResponse;
  @override
  StreamSubscription? streamSubscription;

  /// switches the screes to the home-screen
  Future<void> switchToHome() async {
    // closes the stream
    deactivate();
    // sets up the request for disconnecting
    GameIdUsernameRequest gameIdUsernameRequest = GameIdUsernameRequest(
        locator<SharedPreferences>().get('username').toString(),
        locator<SharedPreferences>().get('gameId').toString());
    Response response = await disconnectService.disconnect(
        gameIdUsernameRequest: gameIdUsernameRequest);

    // check if there is any error
    if (response.statusCode != 200 && !gamePlayModel.isGameFinished) {
      Fluttertoast.showToast(msg: "Error: switchToHome");
    }

    // navigates to the home
    locator<NavigationService>().pushNamedAndRemoveUntil(routeName: '/home');
  }

  /// closes the streams
  @override
  void deactivate() {
    webSocketService.deactivate();
    streamSubscription?.cancel();
  }

  /// sends the answer to the server
  void sendAnswer(String answer) {
    String gameId = locator<SharedPreferences>().get('gameId').toString();
    // checks if the gameId is valid
    if (gameId == "" || gameId == "null") {
      print("error gameid ist leer");
      return;
    }

    // sets up the payload
    Map<String, String> payLoad = {
      "gameId": gameId.toString(),
      "senderUsername": locator<SharedPreferences>().get('username').toString(),
      "answer": answer
    };

    // sends the payload to the server
    webSocketService.stompClient?.send(
        destination: '/app/gameplay-' + gameId.toString(),
        body: jsonEncode(payLoad));
  }

  /// inits the view model to its initial state
  @override
  void init() {
    gamePlayModel = GamePlayModel();

    handleMessages();
  }

  /// handles the incoming messsages for this class
  @override
  void handleMessages() {
    String gameId = locator<SharedPreferences>().get('gameId').toString();
    if (gameId == "null" || gameId == "") {
      print("Game id ist leer");
      return;
    }
    webSocketService.init('ws://192.168.0.72:3000/quiz-game-websocket',
        '/topic/game-' + locator<SharedPreferences>().get('gameId').toString());

    webSocketService.activate();

    streamSubscription =
        webSocketService.socketController.stream.listen((event) {
      WebSocketResponse webSocketResponse = WebSocketResponse.fromJson(event);


      // join listener
      if (webSocketResponse.headers?.info?[0] == "game_ready") {
        gamePlayModel.ready = true;
        fillModelWithQuestionAndAnswersFromResponse(event);
        notifyListeners();
        return;
      }

      //leave listener, navigates the user back if the opponent leaves
      MessageEnum userLeft =
          WebSocketMessageHandler.handleLeftMessage(webSocketResponse);

      if (userLeft == MessageEnum.host || userLeft == MessageEnum.user) {
        deactivate();
        return;
      }

      // updates the score of the player
      updateScoreFromRequest(webSocketResponse);

      if (isGameOver(webSocketResponse)) {
        print("game over");

        isPlayerWinner(webSocketResponse);
        gamePlayModel.isGameFinished = true;
        notifyListeners();
        return;
      }
      // checks if there is a current question
      if (webSocketResponse.body?.currentQuestion != null) {
        fillModelWithQuestionAndAnswersFromResponse(event);
        notifyListeners();
        return;
      }
    });
  }

  /// checks if the player is winner out of the response
  WinnerEnum isPlayerWinner(WebSocketResponse webSocketResponse) {
    if (webSocketResponse.body?.winner == null) {
      gamePlayModel.gameOverWinnerText = "Das Spiel ist unentschieden";
      return WinnerEnum.none;
    }
    if (webSocketResponse.body?.winner ==
        locator<SharedPreferences>().get('username')) {
      gamePlayModel.gameOverWinnerText = "Du hast gewonnen";
      return WinnerEnum.you;
    }

    gamePlayModel.gameOverWinnerText = "Du hast verloren";

    return WinnerEnum.opponent;
  }

  /// checks if the game is over out of the response
  bool isGameOver(WebSocketResponse webSocketResponse) {
    if (webSocketResponse.headers?.info?[0] == "game_over") {
      return true;
    }
    return false;
  }

  /// updates the players score
  void updateScoreFromRequest(WebSocketResponse webSocketResponse) {
    gamePlayModel.score = (webSocketResponse.body?.playerOne?.username ==
            locator<SharedPreferences>().get('username')
        ? webSocketResponse.body?.playerOne?.scoreObject?.currentScoreValue!
        : webSocketResponse.body?.playerTwo?.scoreObject?.currentScoreValue!)!;
  }

  /// fills the model with the questions
  void fillModelWithQuestionAndAnswersFromResponse(Map response) {
    gamePlayModel.question =
        CurrentQuestion.fromJson(response['body']['currentQuestion']);
  }
}
