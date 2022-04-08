import 'dart:convert';
import 'package:app/app/request/gameid_username_ready_request.dart';
import 'package:http/http.dart';
import '../../../constants.dart';
import '../../../locator.dart';
import '../secure_storage_service.dart';

/// service to set the players ready state at the server
class MarkReadyService {
  /// sends a post request with the needed information to set a players ready state
  Future<Response> sendReady(
      {required GameIdUsernameReadyRequest gameIdUsernameReadyRequest}) async {
    final token = await locator<SecureStorage>().storage.read(key: 'token');
    return post(Uri.parse(apiMarkAsReady),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
          'Authorization': token ?? "",
        },
        body: jsonEncode(<String, dynamic>{
          'username': gameIdUsernameReadyRequest.username,
          'gameId': gameIdUsernameReadyRequest.gameId,
          'ready': gameIdUsernameReadyRequest.ready
        }));
  }
}
