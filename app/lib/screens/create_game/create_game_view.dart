import 'package:flutter/material.dart';
import 'package:provider/src/provider.dart';

import 'create_game_view_model.dart';

class CreateGameView extends StatefulWidget {
  const CreateGameView({Key? key}) : super(key: key);

  @override
  _CreateGameViewState createState() => _CreateGameViewState();
}

class _CreateGameViewState extends State<CreateGameView> {
  @override
  Widget build(BuildContext context) {
    CreateGameViewModel createGameViewModel =
        context.watch<CreateGameViewModel>();

    return WillPopScope(
      onWillPop: () async => false,
      child: Scaffold(
        appBar: AppBar(
          title: Text(createGameViewModel.appTitle),
          centerTitle: true,
          leading: _appbarButton(context,createGameViewModel),
        ),
        body: _ui(context, createGameViewModel),
      ),
    );
  }


  Widget _appbarButton(BuildContext context,
      CreateGameViewModel createGameViewModel) {
   /* if (createGameViewModel.isUserJoined && createGameViewModel.ready) {
      return Container();
    }*/

    return IconButton(
      icon: const Icon(Icons.arrow_back),
      onPressed: () {
        createGameViewModel.deleteGame();
        createGameViewModel.deactivate();
      },
    );
  }





  Widget _ui(BuildContext context, CreateGameViewModel createGameViewModel) {

    return loadingScreen(context, createGameViewModel);
  }

  Widget loadingScreen(
      BuildContext context, CreateGameViewModel createGameViewModel) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.start,
      children: <Widget>[
        const SizedBox(
          height: 50,
        ),
        Text(
          createGameViewModel.gameId == "" ? "" : "Deine Spiel ID:",
          style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
        ),
        const SizedBox(
          height: 10,
        ),
        Text(
          createGameViewModel.gameId == ""
              ? createGameViewModel.message
              : createGameViewModel.gameId,
          style: const TextStyle(fontSize: 16),
        ),
        const SizedBox(
          height: 50,
        ),
        const Center(
          child: CircularProgressIndicator(),
        ),
      ],
    );
  }

  @override
  void initState() {
    super.initState();
    context.read<CreateGameViewModel>().init();
  }
}
