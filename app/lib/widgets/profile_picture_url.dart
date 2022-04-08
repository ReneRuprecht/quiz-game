import 'package:flutter/material.dart';

import '../constants.dart';

/// contains a widget for the profile picture from an url
class ProfilePictureFromUrl extends StatefulWidget {
  ProfilePictureFromUrl({Key? key, required this.radius, required this.url})
      : super(key: key);

  final double radius;
  final String url;

  @override
  State<ProfilePictureFromUrl> createState() => _ProfilePictureFromUrlState();
}

class _ProfilePictureFromUrlState extends State<ProfilePictureFromUrl> {
  @override
  Widget build(BuildContext context) {

    if (widget.url == "") {
      print("profilbild leer");
      return Container();
    }
  print(widget.url);
    return CircleAvatar(
      backgroundImage: NetworkImage(prefix + "/" +widget.url),
      radius: widget.radius,
    );
  }
}
