import 'dart:convert';
import 'package:app/app/request/gameid_username_request.dart';
import 'package:app/constants.dart';
import 'package:http/http.dart';
import '../../../locator.dart';
import '../secure_storage_service.dart';

/// service for connecting to a  game
class GameConnectService {
  /// sends a post request with the needed information to connect to a game
  Future<Response> connect(
      {required GameIdUsernameRequest gameIdUsernameRequest}) async {
    final token = await locator<SecureStorage>().storage.read(key: 'token');

    return post(Uri.parse(apiConnectGame),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
          'Authorization': token ?? ""
        },
        body: jsonEncode(<String, String>{
          'gameId': gameIdUsernameRequest.gameId,
          'username': gameIdUsernameRequest.username
        }));
  }
}
