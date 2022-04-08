import 'package:app/app/services/game/create_game_service.dart';
import 'package:app/app/services/register/register_service.dart';
import 'package:get_it/get_it.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'app/services/game/delete_game_service.dart';
import 'app/services/game/disconnect_service.dart';
import 'app/services/game/game_connect_service.dart';
import 'app/services/game/mark_ready_service.dart';
import 'app/services/game/game_info_service.dart';
import 'app/services/game/get_games_service.dart';
import 'app/services/login/login_service.dart';
import 'app/services/login/logout_service.dart';
import 'app/services/navigation_service.dart';
import 'app/services/secure_storage_service.dart';
import 'app/services/user/user_edit_profile_picture_service.dart';
import 'app/services/user/user_info_service.dart';
import 'app/services/validate_token_service.dart';
import 'app/services/websocket_service.dart';

GetIt locator = GetIt.instance;

/// loads all the services
Future<void> setUpLocator() async {
  locator.registerLazySingleton(() => NavigationService());

  locator.registerLazySingleton(() => SecureStorage());

  // login
  locator.registerLazySingleton(() => LoginService());

  locator.registerLazySingleton(() => RegisterService());

  locator.registerLazySingleton(() => ValidateTokenService());

  // user
  locator.registerLazySingleton(() => UserInfoService());
  locator.registerLazySingleton(() => UserEditProfilePictureService());

  //game
  locator.registerLazySingleton(() => CreateGameService());
  locator.registerLazySingleton(() => GetGamesService());
  locator.registerLazySingleton(() => MarkReadyService());
  locator.registerLazySingleton(() => DisconnectService());
  locator.registerLazySingleton(() => LogoutService());
  locator.registerLazySingleton(() => DeleteGameService());
  locator.registerLazySingleton(() => GameInfoService());
  locator.registerLazySingleton(() => GameConnectService());

  locator.registerLazySingleton(() => WebSocketService());

  final sharedPreferences = await SharedPreferences.getInstance();
  locator.registerLazySingleton<SharedPreferences>(() => sharedPreferences);
}
