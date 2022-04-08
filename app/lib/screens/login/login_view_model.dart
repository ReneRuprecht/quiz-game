import 'dart:convert';

import 'package:app/app/init_state.dart';
import 'package:app/app/request/username_password_request.dart';
import 'package:app/app/response/token_response.dart';
import 'package:app/app/services/login/login_service.dart';
import 'package:app/app/services/navigation_service.dart';
import 'package:app/app/services/secure_storage_service.dart';
import 'package:app/app/services/user/user_info_service.dart';
import 'package:app/app/services/validate_token_service.dart';
import 'package:flutter/cupertino.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:http/http.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../../locator.dart';
import 'login_model.dart';

/// viewmodel for the login view
class LoginViewModel extends ChangeNotifier implements InitState {
  LoginModel loginModel = LoginModel();
  TextEditingController usernameController = TextEditingController();
  TextEditingController passwordController = TextEditingController();
  LoginService loginService = locator<LoginService>();
  SecureStorage secureStorage = locator<SecureStorage>();
  UserInfoService userService = locator<UserInfoService>();
  ValidateTokenService validateTokenService = locator<ValidateTokenService>();
  SharedPreferences sharedPreferences = locator<SharedPreferences>();

  bool get isLoading => loginModel.loading;

  String get errorMessage => loginModel.errorMessage;

  String get appTitle => loginModel.title;

  /// initalizes the inital state
  @override
  Future<void> init() async {
    loginModel = LoginModel();
    usernameController.clear();
    passwordController.clear();

    String? tokenFromStorage = await secureStorage.storage.read(key: 'token');
    if (tokenFromStorage != null && tokenFromStorage != "") {
      setLoading(true);
      if (await validateTokenService.isTokenValid(token: tokenFromStorage)) {
        locator<NavigationService>()
            .pushNamedAndRemoveUntil(routeName: '/home');
        return;
      } else {}
    }
  }

  /// sets the loading screen and notifies the view
  void setLoading(value) {
    loginModel.loading = value;
    notifyListeners();
  }

  /// does the login request and saves the information inside the
  /// shared preferences as well as the token to the secured storage
  Future<void> login() async {

    if(usernameController.text =="" || passwordController.text ==""){
      Fluttertoast.showToast(msg: "Logindaten unvollständig");
      return;
    }

    setLoading(true);

    UsernamePasswordRequest usernamePasswordRequest = UsernamePasswordRequest(
        username: usernameController.text, password: passwordController.text);

    TokenResponse tokenResponse = TokenResponse.fromResponse(await loginService
        .sendLogin(usernamePasswordRequest: usernamePasswordRequest));

    if (tokenResponse.statusCode != 200) {
      loginModel.errorMessage = tokenResponse.message!;
      setLoading(false);
      return;
    }

    loginModel.token = tokenResponse.token!;

    await secureStorage.storage.write(key: "token", value: loginModel.token);

    // gets the user info
    Response response = await locator<UserInfoService>().getUserInfo(token: loginModel.token);
    String profilPicture =
        jsonDecode(response.body)['profilePicture'] ?? "";
    if (profilPicture != "") {
      sharedPreferences.setString('profile-picture', profilPicture);
    }

    sharedPreferences.setString('username', usernameController.text);

    Fluttertoast.showToast(msg: "Login erfolgreich");

    locator<NavigationService>().pushReplacementNamed(routeName: '/home');
  }
}
