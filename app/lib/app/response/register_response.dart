import 'dart:convert';

import 'package:app/app/models/player.dart';
import 'package:http/http.dart';
/// class for the register response to map it from and to json format
class RegisterResponse {
  Player? user;
  int? statusCode;
  String? message;

  RegisterResponse.fromResponse(Response response) {
    statusCode = response.statusCode;
    if (statusCode != 200 || statusCode!= 201) {
      if(response.body ==""){
        message = "User konnte nicht erstellt werden";
      }
      return;
    }

    user = Player.fromJson(jsonDecode(response.body));
  }

  Map<String, dynamic> toJson() {
    return user!.toJson();
  }
}
