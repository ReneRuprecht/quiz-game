import 'package:app/locator.dart';
import 'package:app/route_generator.dart';
import 'package:app/screens/connect_game/connect_game_view_model.dart';
import 'package:app/screens/create_game/create_game_view_model.dart';
import 'package:app/screens/game_play/game_play_view_model.dart';
import 'package:app/screens/home/home_view_model.dart';
import 'package:app/screens/login/login_view_model.dart';
import 'package:app/screens/pre_game_play/pre_gameplay_view_,model.dart';
import 'package:app/screens/register/register_view_model.dart';
import 'package:app/screens/start/start_view_model.dart';
import 'package:app/screens/user_edit/user_edit_view_model.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';

import 'app/services/navigation_service.dart';

main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await setUpLocator();
  SharedPreferences.setMockInitialValues({});
  runApp(MyApp());
}

/// main entrypoint
/// the application starts here
class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider<StartViewModel>(create: (_) => StartViewModel()),
        ChangeNotifierProvider<RegisterViewModel>(
            create: (_) => RegisterViewModel()),
        ChangeNotifierProvider<LoginViewModel>(create: (_) => LoginViewModel()),
        ChangeNotifierProvider<HomeViewModel>(create: (_) => HomeViewModel()),
        ChangeNotifierProvider<UserEditViewModel>(
            create: (_) => UserEditViewModel()),
        ChangeNotifierProvider<CreateGameViewModel>(
            create: (_) => CreateGameViewModel()),
        ChangeNotifierProvider<ConnectGameViewModel>(
            create: (_) => ConnectGameViewModel()),
        ChangeNotifierProvider<GamePlayViewModel>(
            create: (_) => GamePlayViewModel()),
        ChangeNotifierProvider<PreGamePlayViewModel>(
            create: (_) => PreGamePlayViewModel()),
      ],
      child: MaterialApp(
        title: "",
        theme: ThemeData.dark().copyWith(
          primaryColor: Colors.white,
          scaffoldBackgroundColor: Colors.grey[900],
          appBarTheme: AppBarTheme(
            backgroundColor: Colors.grey[850],
          ),
          elevatedButtonTheme: ElevatedButtonThemeData(
            style: ElevatedButton.styleFrom(
                primary: Colors.grey[700], onPrimary: Colors.yellow),
          ),
        ),
        initialRoute: '/',
        onGenerateRoute: RouteGenerator.generateRoute,
        navigatorKey: locator<NavigationService>().navigatorKey,
      ),
    );
  }
}
