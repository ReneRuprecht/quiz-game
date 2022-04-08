import 'dart:convert';
import 'dart:io';

import 'package:app/app/init_state.dart';
import 'package:app/app/models/player.dart';
import 'package:app/app/services/navigation_service.dart';
import 'package:app/app/services/secure_storage_service.dart';
import 'package:app/app/services/user/user_edit_profile_picture_service.dart';
import 'package:app/app/services/user/user_info_service.dart';
import 'package:app/screens/user_edit/user_edit_model.dart';

import 'package:flutter/cupertino.dart';
import 'package:http/src/response.dart';
import 'package:image_picker/image_picker.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../../locator.dart';

/// user edit view model
class UserEditViewModel extends ChangeNotifier implements InitState {
  UserEditModel userEditModel = UserEditModel();
  UserEditProfilePictureService userEditService =
      locator<UserEditProfilePictureService>();
  UserInfoService userInfoService = locator<UserInfoService>();
  final sharedPreferences = locator<SharedPreferences>();

  String get title => userEditModel.title;

  int get total => userEditModel.total;

  int get won => userEditModel.won;

  int get lost => userEditModel.lost;

  int get draw => userEditModel.draw;

  Player? get player => userEditModel.player;

  final picker = ImagePicker();

  String? get getImageFile => sharedPreferences.getString('profile-picture');

  /// uploads a profile picture to the server for the user
  Future uploadProfilePicture() async {
    final pickedFile = await picker.pickImage(source: ImageSource.gallery);

    if (pickedFile != null) {
      final image = File(pickedFile.path);
      Response response = await userEditService.uploadProfilePicture(image);

      print(response.body);

      if (response.statusCode == 200) {
        sharedPreferences.setString(
            'profile-picture', jsonDecode(response.body)['profilePicture']);
      }

      locator<NavigationService>().pushNamedAndRemoveUntil(routeName: '/home');
    } else {
      print('No image selected.');
    }
  }

  /// initializes the initial state
  @override
  Future<void> init() async {
    userEditModel = UserEditModel();
    final token = await locator<SecureStorage>().storage.read(key: 'token');

    if (token.toString() == "") {
      return;
    }
    Response response = await userInfoService.getUserInfo(token: token);

    handleResponse(response);
  }

  /// handles the response user info service,
  /// notifies the view
  void handleResponse(Response response) {
    final content = jsonDecode(response.body);
    Player player = Player.fromJson(content);

    userEditModel.player = player;

    notifyListeners();
  }
}
