import 'dart:convert';

import 'package:app/app/request/username_password_request.dart';
import 'package:app/constants.dart';
import 'package:http/http.dart';

/// service for the login at the server
class LoginService {
  /// sends a post request with the needed information to login the user at the server
  Future<Response> sendLogin(
      {required UsernamePasswordRequest usernamePasswordRequest}) async {
    return post(Uri.parse(apiLogin),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
        body: jsonEncode(<String, String>{
          'username': usernamePasswordRequest.username,
          'password': usernamePasswordRequest.password
        }));
  }
}
