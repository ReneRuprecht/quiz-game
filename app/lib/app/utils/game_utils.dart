import 'package:app/app/request/delete_game_request.dart';
import 'package:app/app/services/game/delete_game_service.dart';
import 'package:app/app/services/navigation_service.dart';
import 'package:http/http.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../../locator.dart';

/// utils class for deleting a game from the server
class GameUtils{
  DeleteGameService deleteGameService = locator<DeleteGameService>();

  /// sends a requests for deleting a game by the current game id inside the
  /// local storage
  Future<void> deleteGame() async {
    DeleteGameRequest deleteGameRequest = DeleteGameRequest(
        gameId: locator<SharedPreferences>().get('gameId').toString());

    Response response =
    await deleteGameService.delete(deleteGameRequest: deleteGameRequest);

    if (response.statusCode != 200) {
      print("error deleting game");
      return;
    }
    locator<NavigationService>().pushNamedAndRemoveUntil(routeName: '/home');
  }




}