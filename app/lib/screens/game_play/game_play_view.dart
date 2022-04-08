import 'package:app/app/models/player.dart';

import 'package:app/widgets/profile_joined_user.dart';
import 'package:app/widgets/user_statistic.dart';
import 'package:flutter/material.dart';
import 'package:provider/src/provider.dart';

import 'game_play_view_model.dart';

class GamePlayView extends StatefulWidget {
  const GamePlayView({Key? key, this.player}) : super(key: key);

  final Player? player;

  @override
  _GamePlayViewState createState() => _GamePlayViewState();
}

class _GamePlayViewState extends State<GamePlayView> {
  @override
  Widget build(BuildContext context) {
    GamePlayViewModel gamePlayViewModel = context.watch<GamePlayViewModel>();

    return WillPopScope(
      onWillPop: () async => false,
      child: Scaffold(
        appBar: AppBar(
          title: const Text("Quiz-Game"),
          centerTitle: true,
          leading: _appbarButton(context, gamePlayViewModel),
        ),
        body: _ui(context, gamePlayViewModel, widget.player),
      ),
    );
  }

  @override
  void initState() {
    super.initState();
    context.read<GamePlayViewModel>().init();
  }
}

Widget _ui(
    BuildContext context, GamePlayViewModel gamePlayViewModel, Player? player) {
  return Padding(
    padding: const EdgeInsets.fromLTRB(20, 0, 20, 0),
    child: Column(
      mainAxisAlignment: MainAxisAlignment.start,
      children: [
        _uiScreens(context, gamePlayViewModel, player),
      ],
    ),
  );
}

Widget _uiScreens(
    BuildContext context, GamePlayViewModel gamePlayViewModel, Player? player) {
  if (!gamePlayViewModel.isReady) {
    return waitingScreen(context, gamePlayViewModel, player);
  }
  if (gamePlayViewModel.isGameFinished) {
    return gameEndingScreen(context, gamePlayViewModel);
  }

  return _gameUi(context, gamePlayViewModel);
}

Widget waitingScreen(
    BuildContext context, GamePlayViewModel gamePlayViewModel, Player? player) {
  return Padding(
    padding: const EdgeInsets.fromLTRB(20, 0, 20, 0),
    child: Column(
      children: [
        const SizedBox(
          height: 20,
        ),
        const Center(
          child: Text(
            "Es geht in kürze los",
            style: TextStyle(
              fontSize: 20,
            ),
          ),
        ),
        const SizedBox(
          height: 50,
        ),
        profileJoinedPlayer(
            context, player?.username ?? "", player?.profilePicture ?? ""),
        const SizedBox(
          height: 20,
        ),
        userStatistic(context, player?.scoreObject),
      ],
    ),
  );
}

Widget _appbarButton(
    BuildContext context, GamePlayViewModel gamePlayViewModel) {

  return IconButton(
    icon: const Icon(Icons.arrow_back),
    onPressed: gamePlayViewModel.switchToHome,
  );
}

Widget _gameUi(BuildContext context, GamePlayViewModel gamePlayViewModel) {
  return Padding(
    padding: const EdgeInsets.fromLTRB(20, 20, 20, 0),
    child: Column(
      mainAxisAlignment: MainAxisAlignment.start,
      children: [
        Text("Aktueller Score: " + gamePlayViewModel.score.toString(),
        style: const TextStyle(
          fontSize: 20
        ),),
        const SizedBox(
          height: 40,
        ),
        Text(
          gamePlayViewModel.question?.question ?? "ERROR: keine Frage gefunden",
          style: const TextStyle(
            fontSize: 20,
            letterSpacing: 1,
          ),
        ),
        const SizedBox(
          height: 80,
        ),
        answerButtons(context, gamePlayViewModel),
        const SizedBox(
          height: 20,
        )
      ],
    ),
  );
}

Widget answerButtons(
    BuildContext context, GamePlayViewModel gamePlayViewModel) {
  return Column(
    mainAxisAlignment: MainAxisAlignment.start,
    children: [
      Row(
        children: [
          Expanded(
              child: ElevatedButton(
                  onPressed: () {
                    gamePlayViewModel
                        .sendAnswer(gamePlayViewModel.question?.answerOne ?? "");
                  },
                  child: Text(gamePlayViewModel.question?.answerOne ??
                      "ERROR: keine Antwort gefunden")))
        ],
      ),
      const SizedBox(
        height: 20,
      ),
      Row(
        children: [
          Expanded(
              child: ElevatedButton(
                  onPressed: () {
                    gamePlayViewModel
                        .sendAnswer(gamePlayViewModel.question?.answerTwo ?? "");
                  },
                  child: Text(gamePlayViewModel.question?.answerTwo ??
                      "ERROR: keine Antwort gefunden")))
        ],
      ),
      const SizedBox(
        height: 20,
      ),
      Row(
        children: [
          Expanded(
              child: ElevatedButton(
                  onPressed: () {
                    gamePlayViewModel
                        .sendAnswer(gamePlayViewModel.question?.answerThree ?? "");
                  },
                  child: Text(gamePlayViewModel.question?.answerThree ??
                      "ERROR: keine Antwort gefunden")))
        ],
      ),
      const SizedBox(
        height: 20,
      ),
      Row(
        children: [
          Expanded(
              child: ElevatedButton(
                  onPressed: () {
                    gamePlayViewModel
                        .sendAnswer(gamePlayViewModel.question?.answerFour ?? "");
                  },
                  child: Text(gamePlayViewModel.question?.answerFour ??
                      "ERROR: keine Antwort gefunden")))
        ],
      ),
      const SizedBox(
        height: 20,
      )
    ],
  );
}

Widget gameEndingScreen(
    BuildContext context, GamePlayViewModel gamePlayViewModel) {
  return Padding(
    padding: const EdgeInsets.fromLTRB(20, 20, 20, 0),
    child: Column(
      mainAxisAlignment: MainAxisAlignment.start,
      children: [
        Row(
          children: [
            Expanded(
              child: Center(
                child: Text(
                  gamePlayViewModel.gameOverText,
                  style: const TextStyle(fontSize: 20),
                ),
              ),
            ),
          ],
        ),
        const SizedBox(
          height: 20,
        ),
        Row(
          children: [
            Expanded(
              child: Center(
                child: Text(
                  "Du hast: " +
                      gamePlayViewModel.score.toString() +
                      " Punkte erreicht!",
                  style: const TextStyle(fontSize: 20),
                ),
              ),
            )
          ],
        ),
      ],
    ),
  );
}
