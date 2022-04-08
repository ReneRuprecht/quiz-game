import 'package:app/app/models/player.dart';
import 'package:app/app/models/ready_state.dart';


import 'current_question.dart';
/// class for the websocket response to map it from and to json format
class Body {
  String? gameId;
  Player? playerOne;
  Player? playerTwo;
  String? winner;
  String? date;
  CurrentQuestion? currentQuestion;
  String? gameStatus;
  ReadyStates? readyStates;
  bool? questionsListEmpty;

  Body(
      {this.gameId,
        this.playerOne,
        this.playerTwo,
        this.winner,
        this.date,
        this.currentQuestion,
        this.gameStatus,
        this.readyStates,
        this.questionsListEmpty});

  Body.fromJson(Map<String, dynamic> json) {
    gameId = json['gameId'];
    playerOne = json['playerOne'] != null
        ? Player.fromJson(json['playerOne'])
        : null;
    playerTwo = json['playerTwo'] != null
        ? Player.fromJson(json['playerTwo'])
        : null;
    winner = json['winner'];
    date = json['date'];
    currentQuestion = json['currentQuestion'] != null
        ? CurrentQuestion.fromJson(json['currentQuestion'])
        : null;
    gameStatus = json['gameStatus'];
    readyStates = json['readyStates'] != null
        ? ReadyStates.fromJson(json['readyStates'])
        : null;
    questionsListEmpty = json['questionsListEmpty'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = Map<String, dynamic>();
    data['gameId'] = gameId;
    if (playerOne != null) {
      data['playerOne'] = playerOne!.toJson();
    }
    if (playerTwo != null) {
      data['playerTwo'] = playerTwo!.toJson();
    }
    data['winner'] = winner;
    data['date'] = date;
    if (currentQuestion != null) {
      data['currentQuestion'] = currentQuestion!.toJson();
    }
    data['gameStatus'] = gameStatus;
    if (readyStates != null) {
      data['readyStates'] = readyStates!.toJson();
    }
    data['questionsListEmpty'] = questionsListEmpty;
    return data;
  }
}