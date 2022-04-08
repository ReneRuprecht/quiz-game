import 'dart:convert';

import 'package:app/app/services/secure_storage_service.dart';
import 'package:app/app/services/user/user_info_service.dart';
import 'package:http/http.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../../constants.dart';
import '../../locator.dart';

/// service for the validation of the token at the server
class ValidateTokenService {
  SecureStorage secureStorage = locator<SecureStorage>();

  /// sends a token to the server with a get request
  /// if the token is invalid, the function will return false
  Future<bool> isTokenValid({token}) async {
    Response response =
        await locator<UserInfoService>().getUserInfo(token: token);
    if (response.statusCode == 200) {
      String username = jsonDecode(response.body)['username'];
      if (username != "") {
        final sharedPreferences = locator<SharedPreferences>();
        sharedPreferences.setString('username', username);
        String profilPicture =
            jsonDecode(response.body)['profilePicture'] ?? "";
        if (profilPicture != "") {
          sharedPreferences.setString('profile-picture', profilPicture);
        }
      }

      return true;
    } else if (response.body == tokenInvalidResponseMessage) {
      await secureStorage.storage.delete(key: 'token');
    }

    return false;
  }
}
