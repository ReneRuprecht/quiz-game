import 'dart:async';
import 'dart:convert';

import 'package:app/app/enums/message_enum.dart';
import 'package:app/app/init_state.dart';
import 'package:app/app/models/player.dart';
import 'package:app/app/request/gameid_username_ready_request.dart';
import 'package:app/app/request/gameid_username_request.dart';
import 'package:app/app/response/websocket_response.dart';
import 'package:app/app/services/game/disconnect_service.dart';
import 'package:app/app/services/game/game_connect_service.dart';
import 'package:app/app/services/game/mark_ready_service.dart';
import 'package:app/app/services/navigation_service.dart';
import 'package:app/app/services/websocket_service.dart';
import 'package:app/app/utils/websocket_message_handler.dart';
import 'package:app/app/websocket.dart';
import 'package:app/constants.dart';

import 'package:app/screens/pre_game_play/pre_gameplay_model.dart';
import 'package:flutter/cupertino.dart';
import 'package:http/http.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../../locator.dart';

/// viewmodel for the pregameplay view
class PreGamePlayViewModel extends ChangeNotifier
    implements QuizWebSocket, InitState {
  PreGamePlayModel preGamePlayModel = PreGamePlayModel();

  MarkReadyService markReadyService = locator<MarkReadyService>();
  GameConnectService gameConnectService = locator<GameConnectService>();
  DisconnectService disconnectService = locator<DisconnectService>();
  @override
  StreamSubscription? streamSubscription;
  WebSocketService webSocketService = locator<WebSocketService>();

  Player? get opponent => preGamePlayModel.opponent;

  /// initializes the inital state
  @override
  Future<void> init() async {
    preGamePlayModel = PreGamePlayModel();
    GameIdUsernameRequest gameIdUsernameRequest = GameIdUsernameRequest(
        locator<SharedPreferences>().get('username').toString(),
        locator<SharedPreferences>().get('gameId').toString());

    Response response = await gameConnectService.connect(
        gameIdUsernameRequest: gameIdUsernameRequest);

    handleResponse(response);
    handleMessages();
  }

  /// handles the response from the connection service
  /// and sets the data to the model as well as notifies the view to reload
  void handleResponse(Response response) {
    final content = jsonDecode(response.body);
    if (content['playerOne']['username'] ==
        locator<SharedPreferences>().get('username')) {
      preGamePlayModel.opponent = Player.fromJson(content['playerTwo']);
    } else {
      preGamePlayModel.opponent = Player.fromJson(content['playerOne']);
    }

    notifyListeners();
  }

  /// handles the incomming websocket messages for this class
  @override
  void handleMessages() {
    webSocketService.init(webSocketUrl,
        webSocketTopic + locator<SharedPreferences>().get('gameId').toString());

    webSocketService.activate();

    streamSubscription =
        webSocketService.socketController.stream.listen((event) {
      WebSocketResponse webSocketResponse = WebSocketResponse.fromJson(event);

      /// listens for the user_left message
      MessageEnum userLeft =
          WebSocketMessageHandler.handleLeftMessage(webSocketResponse);

      /// if someone left the game, the socket connection gets closed
      if (userLeft == MessageEnum.host || userLeft == MessageEnum.user) {
        deactivate();
        return;
      }
    });
  }

// sets the current player as ready to play
  Future<void> markAsReady(bool ready) async {
    print("ready");

    String? username = locator<SharedPreferences>().getString('username');
    if (username == null) {
      print("username is null");
      return;
    }

    GameIdUsernameReadyRequest gameIdUsernameRequest =
        GameIdUsernameReadyRequest(username,
            locator<SharedPreferences>().get('gameId').toString(), ready);

    Response response = await markReadyService.sendReady(
        gameIdUsernameReadyRequest: gameIdUsernameRequest);

    if (response.statusCode == 200 && ready) {
      deactivate();
      switchToGameScene();
    } else {
      deactivate();
    }
  }

  /// switches to the game scene
  void switchToGameScene() {
    locator<NavigationService>().pushNamedAndRemoveUntil(
        routeName: '/gameplay', args: preGamePlayModel.opponent);
  }

  /// deactivates the socket connection
  @override
  void deactivate() {
    webSocketService.deactivate();
    streamSubscription?.cancel();
  }

  /// sends a request for leaving a game lobby
  /// this will inform the server as well as the other player
  Future<void> leave() async {
    GameIdUsernameRequest gameIdUsernameRequest = GameIdUsernameRequest(
        locator<SharedPreferences>().get('username').toString(),
        locator<SharedPreferences>().get('gameId').toString());

    Response response = await disconnectService.disconnect(
        gameIdUsernameRequest: gameIdUsernameRequest);

    if (response.statusCode != 200) {
      print("error disconnect - pre game");
      return;
    }

    locator<SharedPreferences>().remove('gameId');
    locator<NavigationService>().pushNamedAndRemoveUntil(routeName: '/home');
  }
}
