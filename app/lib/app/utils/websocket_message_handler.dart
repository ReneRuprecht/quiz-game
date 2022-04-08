import 'package:app/app/enums/message_enum.dart';
import 'package:app/app/response/websocket_response.dart';
import 'package:app/app/services/navigation_service.dart';
import 'package:fluttertoast/fluttertoast.dart';

import '../../locator.dart';

/// websockethandler class for handling messages
class WebSocketMessageHandler {
  /// handles the message if a player left the lobby/game
  /// this is used if the host leaves, the joined player will get notified
  /// or if the player leaves, the host gets back in to the create screen
  static MessageEnum handleLeftMessage(WebSocketResponse webSocketResponse) {
    if (webSocketResponse.headers?.info?[0] == "user_left") {
      if (webSocketResponse.body == null) {
        print("player one null");

        locator<NavigationService>()
            .pushNamedAndRemoveUntil(routeName: '/home');
        Fluttertoast.showToast(
            msg: "Die Lobby wurde aufgelöst, der Host hat das Spiel verlassen");
        return MessageEnum.host;
      }

      Fluttertoast.showToast(
          msg:
              "Die Lobby wurde aufgelöst, der Spieler hat das Spiel verlassen");
      locator<NavigationService>()
          .pushNamedAndRemoveUntil(routeName: '/create');

      return MessageEnum.user;
    }
    return MessageEnum.none;
  }
}
