import 'dart:convert';

import 'package:app/app/models/player.dart';
import 'package:http/http.dart';
/// class for the user score response to map it from and to json format
class UserScoreResponse {
  Player? user;
  int? statusCode;
  String? message;

  UserScoreResponse.fromResponse(Response response) {
    statusCode = response.statusCode;
    if (statusCode != 200) {
      message = jsonDecode(response.body)['message'];
      return;
    }

    user = Player.fromJson(jsonDecode(response.body));
  }

  Map<String, dynamic> toJson() {
    return user!.toJson();
  }
}
