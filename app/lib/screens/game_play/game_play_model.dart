import 'package:app/app/models/current_question.dart';
import 'package:app/app/response/websocket_response.dart';

class GamePlayModel {
  bool ready = false;
  WebSocketResponse? webSocketResponse;

  CurrentQuestion? question;

  int score = 0;

  bool isGameFinished = false;

  String gameOverWinnerText = "";
}
