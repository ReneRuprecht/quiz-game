import 'package:app/app/models/score_object.dart';
/// class for the websocket response to map it from and to json format
class Player {
  String? username;
  String? profilePicture;
  ScoreObject? scoreObject;

  Player({this.username, this.profilePicture, this.scoreObject});

  Player.fromJson(Map<String, dynamic> json) {
    username = json['username'].toString();;
    profilePicture = json["profilePicture"];
    scoreObject = json['scoreObject'] != null
        ? ScoreObject.fromJson(json['scoreObject'])
        : null;
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['username'] = username;
    data['profilePicture'] = profilePicture;
    if (scoreObject != null) {
      data['scoreObject'] = scoreObject!.toJson();
    }
    return data;
  }
}