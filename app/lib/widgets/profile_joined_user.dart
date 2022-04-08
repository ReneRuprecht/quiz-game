import 'package:flutter/material.dart';

import '../constants.dart';

/// contains a widget for the joined user
Widget profileJoinedPlayer(
    BuildContext context, String username, String profilePictureUrl) {
  if (username == "") {
    return Container();
  }

  return Row(
    mainAxisAlignment: MainAxisAlignment.start,
    children: <Widget>[
      Expanded(
        child: Column(
          children: [
            const SizedBox(
              height: 20,
            ),
            Text(
              username,
              style: TextStyle(fontSize: 25, letterSpacing: 1),
            ),
            const SizedBox(
              height: 20,
            ),
            ProfilePicture(profilePictureUrl),
          ],
        ),
      )
    ],
  );
}

Widget ProfilePicture(String url) {
  if (url == "") {
    return Container();
  }
  return CircleAvatar(
    backgroundImage: NetworkImage(prefix + "/" + url),
    radius: 50,
  );
}
