import 'dart:convert';

import 'package:app/app/request/username_password_request.dart';

import 'package:http/http.dart';

import '../../../constants.dart';
/// service for the registration at the server
class RegisterService {
  /// sends a post request with the needed information to register the user at the server
  Future<Response> sendRegister(
      {required UsernamePasswordRequest usernamePasswordRequest}) async {
    return post(
        Uri.parse(apiRegister),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
        body: jsonEncode(<String, String>{
          'username': usernamePasswordRequest.username,
          'password': usernamePasswordRequest.password
        }));
  }
}
