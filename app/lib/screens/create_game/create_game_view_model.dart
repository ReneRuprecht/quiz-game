import 'dart:async';
import 'dart:convert';

import 'package:app/app/init_state.dart';
import 'package:app/app/request/create_game_request.dart';
import 'package:app/app/request/gameid_username_ready_request.dart';
import 'package:app/app/request/gameid_username_request.dart';
import 'package:app/app/response/websocket_response.dart';
import 'package:app/app/services/game/create_game_service.dart';
import 'package:app/app/services/game/delete_game_service.dart';
import 'package:app/app/services/game/disconnect_service.dart';
import 'package:app/app/services/game/mark_ready_service.dart';
import 'package:app/app/services/navigation_service.dart';
import 'package:app/app/services/websocket_service.dart';
import 'package:app/app/utils/game_utils.dart';
import 'package:app/app/websocket.dart';
import 'package:flutter/cupertino.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:http/http.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../../constants.dart';
import '../../locator.dart';

import 'create_game_model.dart';

/// viewmodel for the create game view
class CreateGameViewModel extends ChangeNotifier
    implements QuizWebSocket, InitState {
  CreateGameModel createGameModel = CreateGameModel();
  CreateGameService createGameService = locator<CreateGameService>();
  DeleteGameService deleteGameService = locator<DeleteGameService>();
  WebSocketService webSocketService = locator<WebSocketService>();
  MarkReadyService markReadyService = locator<MarkReadyService>();
  NavigationService navigationService = locator<NavigationService>();
  DisconnectService disconnectService = locator<DisconnectService>();
  GameUtils gameUtils = GameUtils();

  String get message => createGameModel.message ?? "";

  String get gameId => createGameModel.gameId ?? "";

  bool get isLoaded => createGameModel.init;

  bool get isUserJoined => createGameModel.userJoined;

  String get appTitle => createGameModel.appTitle;

  WebSocketResponse? get webSocketResponse => createGameModel.webSocketResponse;
  @override
  StreamSubscription? streamSubscription;

  bool get ready => createGameModel.ready;



  /// sends a request for creating a game
  /// sets the game id to the shared preferences storage
  Future<void> createGame() async {
    CreateGameRequest createGameRequest = CreateGameRequest(
        username: locator<SharedPreferences>().getString('username') ?? "");
    if (createGameRequest.username == "") {
      print("username leer");
      return;
    }

    Response response =
        await createGameService.create(createGameRequest: createGameRequest);

    if (response.statusCode != 200) {
      createGameModel.message = "Error";
      notifyListeners();
      return;
    }

    await locator<SharedPreferences>()
        .setString('gameId', jsonDecode(response.body)['gameId']);

    createGameModel.gameId =
        locator<SharedPreferences>().get('gameId').toString();
    print(createGameModel.gameId);

    GameIdUsernameReadyRequest gameIdUsernameReadyRequest =
        GameIdUsernameReadyRequest(
            locator<SharedPreferences>().get('username').toString(),
            locator<SharedPreferences>().get('gameId').toString(),
            false);
    Response readyResponse = await markReadyService.sendReady(
        gameIdUsernameReadyRequest: gameIdUsernameReadyRequest);

    if (readyResponse.statusCode != 200) {
      print("Error ready status");
      return;
    }

    handleMessages();

    createGameModel.init = true;
    notifyListeners();
  }

  /// initializes the inital state
  @override
  void init() {
    createGameModel = CreateGameModel();
    createGame();
  }

  /// handles all the incoming websocket messages for this class
  @override
  void handleMessages() {
    webSocketService.init(webSocketUrl,
        webSocketTopic + createGameModel.gameId.toString());

    webSocketService.activate();
    streamSubscription =
        webSocketService.socketController.stream.listen((event) {
      WebSocketResponse webSocketResponse = WebSocketResponse.fromJson(event);

      /// listens for joining event
      if (webSocketResponse.headers?.info?[0] == "user_joined") {
        switchToPreGamePlay();

        return;
      }

      // leave listener
      if (webSocketResponse.headers?.info?[0] == "user_left") {
        Fluttertoast.showToast(msg: "Die Lobby wurde aufgelöst");
        deleteGame();
        deactivate();

        return;
      }
    });
  }

  /// deletes the current game
  void deleteGame(){
    gameUtils.deleteGame();
  }

  // backbutton on appbar calls this function to leave the lobby and delete the game
  Future<void> exitLobby() async {
    GameIdUsernameRequest gameIdUsernameRequest = GameIdUsernameRequest(
        locator<SharedPreferences>().getString('username') ?? "", gameId);
    Response response = await disconnectService.disconnect(
        gameIdUsernameRequest: gameIdUsernameRequest);

    if (response.headers['info'] == "user_left") {
      deleteGame();
      deactivate();
    }
  }

  // prepares the model with informations
  void switchToPreGamePlay() {
    deactivate();

    locator<NavigationService>().pushNamed(routeName: '/pre-gameplay');
  }

  // deactivates the socket
  @override
  void deactivate() {
    webSocketService.deactivate();
    streamSubscription?.cancel();
  }
}
