import 'dart:convert';
import 'package:app/app/request/gameid_username_request.dart';
import 'package:app/app/services/secure_storage_service.dart';
import 'package:http/http.dart';
import '../../../constants.dart';
import '../../../locator.dart';

/// service for disconnecting from a game
class DisconnectService {
  /// sends a post request with the needed information to disconnect from a game
  Future<Response> disconnect(
      {required GameIdUsernameRequest gameIdUsernameRequest}) async {
    final token = await locator<SecureStorage>().storage.read(key: 'token');

    return post(Uri.parse(apiDisconnectGame),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
          'Authorization': token ?? ""
        },
        body: jsonEncode(<String, String>{
          'username': gameIdUsernameRequest.username,
          'gameId': gameIdUsernameRequest.gameId
        }));
  }
}
