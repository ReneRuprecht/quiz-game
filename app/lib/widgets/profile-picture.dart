import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../constants.dart';
import '../locator.dart';

/// contains a widget for the profile picture
class ProfilePicture extends StatefulWidget {
  ProfilePicture({Key? key, required this.radius}) : super(key: key);

  final double radius;

  @override
  State<ProfilePicture> createState() => _ProfilePictureState();
}

class _ProfilePictureState extends State<ProfilePicture> {

  @override
  Widget build(BuildContext context) {
    final sharedPreferences = locator<SharedPreferences>();
    if (sharedPreferences.getString('profile-picture') == null) {
      return Container();
    } else {
      return CircleAvatar(
        backgroundImage: NetworkImage(
            prefix + "/" + sharedPreferences.getString('profile-picture')!),
        radius: widget.radius,
      );
    }
  }
}
