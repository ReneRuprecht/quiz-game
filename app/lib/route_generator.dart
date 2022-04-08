import 'package:app/screens/connect_game/connect_game_view.dart';
import 'package:app/screens/create_game/create_game_view.dart';
import 'package:app/screens/error/error_view.dart';
import 'package:app/screens/game_play/game_play_view.dart';
import 'package:app/screens/home/home_view.dart';
import 'package:app/screens/login/login_view.dart';
import 'package:app/screens/pre_game_play/pre_gameplay_view.dart';
import 'package:app/screens/register/register_view.dart';
import 'package:app/screens/start/start_view.dart';
import 'package:app/screens/user_edit/user_edit_view.dart';
import 'package:flutter/material.dart';

import 'app/models/player.dart';

/// class for generating all the used routes
class RouteGenerator {
  static Route<dynamic> generateRoute(RouteSettings settings) {
    final args = settings.arguments;

    switch (settings.name) {
      case '/':
        return MaterialPageRoute(builder: (_) => const StartView());
      case '/register':
        return MaterialPageRoute(builder: (_) => const RegisterView());
      case '/login':
        return MaterialPageRoute(builder: (_) => const LoginView());
      case '/home':
        return MaterialPageRoute(builder: (_) => const HomeView());
      case '/edit':
        return MaterialPageRoute(builder: (_) => const UserEditView());
      case '/create':
        return MaterialPageRoute(builder: (_) => const CreateGameView());
      case '/connect':
        return MaterialPageRoute(builder: (_) => const ConnectGameView());
      case '/gameplay':
        if (args is Player) {
          return MaterialPageRoute(
            builder: (_) => GamePlayView(
              player: args,
            ),
          );
        }
        return _errorRoute();

      case '/pre-gameplay':
        return MaterialPageRoute(builder: (_) => const PreGamePlayView());
      default:
        return _errorRoute();
    }
  }

  static Route<dynamic> _errorRoute() {
    return MaterialPageRoute(builder: (_) {
      return const ErrorView();
    });
  }
}
