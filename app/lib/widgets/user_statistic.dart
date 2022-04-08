import 'package:app/app/models/score_object.dart';
import 'package:flutter/cupertino.dart';

/// contains a widget for the user statistics
Widget userStatistic(
    BuildContext context, ScoreObject? scoreObject) {

  int total = scoreObject?.gamesPlayed ?? 0;
  int won = scoreObject?.win ?? 0;
  int lose = scoreObject?.lose ?? 0;
  int draw = total - won - lose;

  return Column(
    mainAxisAlignment: MainAxisAlignment.start,
    children: [
      Text(
        "Insgesamt gespielt: " + total.toString(),
        style: TextStyle(
          fontSize: 20,
        ),
      ),
      SizedBox(
        height: 10,
      ),
      Text("Gewonnen: " + won.toString(),
          style: TextStyle(
            fontSize: 20,
          )),
      SizedBox(
        height: 10,
      ),
      Text("Verloren: " + lose.toString(),
          style: TextStyle(
            fontSize: 20,
          )),
      SizedBox(
        height: 10,
      ),
      Text("Unentschieden: " + draw.toString(),
          style: TextStyle(
            fontSize: 20,
          )),
    ],
  );
}
