import 'package:app/app/services/secure_storage_service.dart';
import 'package:app/constants.dart';
import 'package:http/http.dart';
import '../../../locator.dart';

/// service for getting all the open games from the server
class GetGamesService {
  /// sends a get request with the needed information to get the games from the server
  Future<Response> getGames() async {
    final token = await locator<SecureStorage>().storage.read(key: 'token');
    return get(
      Uri.parse(apiGetGames),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
        'Authorization': token ?? "",
      },
    );
  }
}
