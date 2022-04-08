import 'package:app/app/services/navigation_service.dart';
import 'package:app/constants.dart';

import 'package:flutter/material.dart';
import 'package:provider/src/provider.dart';

import '../../locator.dart';

import 'connect_game_view_model.dart';

class ConnectGameView extends StatefulWidget {
  const ConnectGameView({Key? key}) : super(key: key);

  @override
  State<ConnectGameView> createState() => _ConnectGameViewState();
}

class _ConnectGameViewState extends State<ConnectGameView> {
  @override
  Widget build(BuildContext context) {
    ConnectGameViewModel connectGameViewModel =
        context.watch<ConnectGameViewModel>();

    return WillPopScope(
        onWillPop: () async => false,
        child: Scaffold(
          appBar: AppBar(
            title: const Text("Spiel suchen"),
            centerTitle: true,
            leading: IconButton(
              icon: const Icon(Icons.arrow_back),
              onPressed: () {
                locator<NavigationService>().goBack();
              },
            ),
          ),
          body: _ui(context, connectGameViewModel),
        ));
  }

  @override
  void initState() {
    super.initState();
    context.read<ConnectGameViewModel>().init();
  }
}

Widget _ui(BuildContext context, ConnectGameViewModel connectGameViewModel) {
  if (connectGameViewModel.loading) {
    return const Center(
      child: CircularProgressIndicator(),
    );
  }
  return Padding(
    padding: const EdgeInsets.fromLTRB(20, 30, 20, 0),
    child: Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Container(
          color: Colors.grey[850],
          child: ListView.builder(
            shrinkWrap: true,
            itemCount: connectGameViewModel.listOfGames.length,
            itemBuilder: (context, index) {
              final item = connectGameViewModel.listOfGames[index];

              return ListTile(
                title: Text(item.gameId!),
                subtitle: Text(item.playerOneUsername!),
                leading: item.profilePicture == ""
                    ? const CircleAvatar(
                        backgroundColor: Colors.black,
                      )
                    : Image.network(prefix + "/" + item.profilePicture!),
                trailing: const Icon(
                  Icons.play_arrow,
                ),
                onTap: () {
                  connectGameViewModel
                      .switchToPreGameView(item.gameId.toString());
                },
              );
            },
          ),
        ),
        const Spacer(),
        Align(
          child: FloatingActionButton(
            onPressed: connectGameViewModel.init,
            child: const Icon(Icons.refresh),
            backgroundColor: Colors.yellow,
          ),
          alignment: AlignmentDirectional.bottomEnd,
        ),
        const SizedBox(
          height: 20,
        ),
      ],
    ),
  );
}
