import 'package:app/app/response/websocket_response.dart';

class CreateGameModel {
  String? gameId;
  String? message;
  bool init = false;
  bool userJoined = false;
  WebSocketResponse? webSocketResponse;
  String appTitle = "Warte auf Spieler";
  bool ready = false;
}
