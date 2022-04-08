import 'dart:convert';

import 'package:app/app/init_state.dart';
import 'package:app/app/services/game/get_games_service.dart';
import 'package:app/app/services/navigation_service.dart';
import 'package:flutter/cupertino.dart';
import 'package:http/http.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../../locator.dart';
import 'connect_game_model.dart';

class ConnectGameViewModel extends ChangeNotifier implements InitState {
  GetGamesService gamesService = locator<GetGamesService>();
  List<ConnectGameModel> gamesList = [];

  List<ConnectGameModel> get listOfGames => gamesList;

  bool loading = true;

  /// is used to set a loading screen
  void setLoading(value) {
    loading = value;
    notifyListeners();
  }

  @override
  Future<void> init() async {
    loading = true;

    gamesList = [];
    Response response = await gamesService.getGames();

    if (response.statusCode != 200) {
      setLoading(false);
      return;
    }

    final games = jsonDecode(response.body);

    final keys = games.keys.toList();

    // loop that adds the game to the lobby list with information about the game
    for (var key in keys) {
      String username = games[key]['playerOne']['username'];
      String profilePicture = games[key]['playerOne']['profilePicture'] ?? "";
      String gameId = games[key]['gameId'];

      gamesList.add(ConnectGameModel.forList(username, gameId, profilePicture));
    }
    setLoading(false);
  }

  void switchToPreGameView(String gameid) {
    locator<SharedPreferences>().setString('gameId', gameid);
    locator<NavigationService>().pushNamed(routeName: '/pre-gameplay');
  }
}
