import 'package:app/app/init_state.dart';
import 'package:app/app/request/username_password_request.dart';
import 'package:app/app/response/register_response.dart';
import 'package:app/app/services/navigation_service.dart';
import 'package:app/app/services/register/register_service.dart';
import 'package:app/screens/register/register_model.dart';

import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';

import '../../locator.dart';

/// registration view model
class RegisterViewModel extends ChangeNotifier implements InitState {
  RegisterModel registerModel = RegisterModel();

  /// initializes the initial state
  @override
  void init() {
    registerModel = RegisterModel();
  }

  RegisterService registerService = locator<RegisterService>();
  NavigationService navigationService = locator<NavigationService>();

  TextEditingController usernameController = TextEditingController();
  TextEditingController passwordController = TextEditingController();

  String get errorMessage => registerModel.errorMessage;

  int get responseCode => registerModel.responseCode;

  bool get loading => registerModel.loading;

  /// sets the loading screen and informs the view to reload
  void setLoading(bool value) {
    registerModel.loading = value;
    notifyListeners();
  }

  /// sends a registration request to register a new user
  Future<void> register() async {

    if(passwordController.text == "" || usernameController.text ==""){
      Fluttertoast.showToast(msg: "Bitte überprüfen Sie die Eingaben");
      return;

    }
    setLoading(true);
    UsernamePasswordRequest usernamePasswordRequest = UsernamePasswordRequest(
        username: usernameController.text, password: passwordController.text);


    RegisterResponse registerResponse = RegisterResponse.fromResponse(
        await registerService.sendRegister(
            usernamePasswordRequest: usernamePasswordRequest));


    switch (registerResponse.statusCode) {
      case 201:
        registerModel.loading = false;
        usernameController.clear();
        passwordController.clear();
        navigationService.pushReplacementNamed(routeName: '/register');
        Fluttertoast.showToast(msg: "Erfolgreich registiert");
        break;

      default:
        registerModel.errorMessage = "Error ${registerResponse.message}";
        registerModel.responseCode = registerResponse.statusCode!;
        usernameController.clear();
        passwordController.clear();
        setLoading(false);
        break;
    }
  }
}
