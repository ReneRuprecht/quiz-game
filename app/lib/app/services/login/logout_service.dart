import 'package:fluttertoast/fluttertoast.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../../../locator.dart';
import '../navigation_service.dart';
import '../secure_storage_service.dart';

/// service for the logout at the device
class LogoutService {
  /// deletes the token and the information on the device
  /// without the token, the user needs to login again with the credentials
  void logout() {
    if (locator<SharedPreferences>().getString('username') != null) {
      locator<SharedPreferences>().remove('username');
    }
    if (locator<SharedPreferences>().getString('profile-picture') != null) {
      locator<SharedPreferences>().remove('profile-picture');
    }
    final _token = locator<SecureStorage>().storage.read(key: 'token');
    if (_token.toString() != "") {
      locator<SecureStorage>().storage.delete(key: 'token');
    }
    Fluttertoast.showToast(msg: "Logout");

    locator<NavigationService>().pushNamedAndRemoveUntil(routeName: '/');
  }
}
