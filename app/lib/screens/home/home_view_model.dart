
import 'package:app/app/init_state.dart';
import 'package:app/app/services/login/logout_service.dart';
import 'package:app/app/services/navigation_service.dart';
import 'package:app/app/services/secure_storage_service.dart';
import 'package:flutter/cupertino.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../../locator.dart';
import 'home_model.dart';

/// viewmodel for the home view
class HomeViewModel extends ChangeNotifier implements InitState {
  HomeModel homeModel = HomeModel();
  SecureStorage secureStorage = locator<SecureStorage>();

  LogoutService logoutService = locator<LogoutService>();

  String get title => homeModel.title;

  String get username => homeModel.username;

  /// navigates to the user edit screen
  void navigateToUserEdit() {
    locator<NavigationService>().pushNamed(routeName: '/edit');
  }

  /// initializes the initial state
  @override
  Future<void> init() async {
    homeModel = HomeModel();

    String username = locator<SharedPreferences>().get('username').toString();

    if (username == "") {
      print("Error: username leer");
    }

    homeModel.username = username;

  }
  /// navigates to the create game screen
  void switchToGameCreate() {
    locator<NavigationService>().pushNamed(routeName: '/create');
  }
  /// navigates to the connect screen
  void switchToConnectToGame() {
    locator<NavigationService>().pushNamed(routeName: '/connect');
  }

  void logout() {
    logoutService.logout();
  }
}
