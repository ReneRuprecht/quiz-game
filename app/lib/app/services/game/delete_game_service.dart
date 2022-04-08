import 'dart:convert';
import 'package:app/app/request/delete_game_request.dart';
import 'package:app/constants.dart';
import 'package:http/http.dart';
import '../../../locator.dart';
import '../secure_storage_service.dart';

/// service for deleting a game
class DeleteGameService {

  /// sends a post request with the needed information to delete a game
  Future<Response> delete(
      {required DeleteGameRequest deleteGameRequest}) async {
    final token = await locator<SecureStorage>().storage.read(key: 'token');

    return post(Uri.parse(apiDeleteGame),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
          'Authorization': token ?? ""
        },
        body: jsonEncode(<String, String>{'gameId': deleteGameRequest.gameId}));
  }
}
