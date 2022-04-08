import 'dart:convert';
import 'package:app/app/request/create_game_request.dart';
import 'package:app/app/services/secure_storage_service.dart';
import 'package:app/constants.dart';
import 'package:http/http.dart';
import '../../../locator.dart';

/// service for creating a new game
class CreateGameService {
  /// sends a post request with the needed information to create a game
  Future<Response> create(
      {required CreateGameRequest createGameRequest}) async {
    final token = await locator<SecureStorage>().storage.read(key: 'token');

    return post(Uri.parse(apiCreateGame),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
          'Authorization': token ?? ""
        },
        body: jsonEncode(
            <String, String>{'username': createGameRequest.username}));
  }
}
