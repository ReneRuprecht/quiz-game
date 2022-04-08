import 'dart:convert';
import 'package:app/app/request/game_info_request.dart';
import 'package:app/app/services/secure_storage_service.dart';
import 'package:app/constants.dart';
import 'package:http/http.dart';
import '../../../locator.dart';

/// service for getting information from a game
class GameInfoService {
  /// sends a post request with the needed information to get information from a game
  Future<Response> getGameInfo(
      {required GameInfoRequest gameInfoRequest}) async {
    final token = await locator<SecureStorage>().storage.read(key: 'token');

    return post(Uri.parse(apiGetGameInfo),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
          'Authorization': token ?? ""
        },
        body: jsonEncode(<String, String>{'gameId': gameInfoRequest.gameId}));
  }
}
