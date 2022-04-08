import 'package:app/widgets/profile-picture.dart';
import 'package:flutter/material.dart';
import 'package:provider/src/provider.dart';

import 'home_view_model.dart';

class HomeView extends StatefulWidget {
  const HomeView({Key? key}) : super(key: key);

  @override
  State<HomeView> createState() => _HomeViewState();
}

class _HomeViewState extends State<HomeView> {
  @override
  Widget build(BuildContext context) {
    HomeViewModel homeViewModel = context.watch<HomeViewModel>();

    return WillPopScope(
        onWillPop: () async => false,
        child: Scaffold(
          appBar: AppBar(
            title: Text(homeViewModel.title),
            centerTitle: true,
            leading: IconButton(
              icon: const Icon(Icons.logout),
              onPressed: () {
                print("logout");
                homeViewModel.logout();
              },
            ),
          ),
          body: _ui(context, homeViewModel),
        ));
  }

  @override
  void initState() {
    super.initState();
    context.read<HomeViewModel>().init();
  }
}

Widget _ui(BuildContext context, HomeViewModel homeViewModel) {
  return Padding(
    padding: const EdgeInsets.fromLTRB(20, 30, 20, 0),
    child: Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Center(
            child: Text(
          "Hallo, " + homeViewModel.username,
          style: const TextStyle(fontSize: 25, letterSpacing: 1),
        )),
        const SizedBox(
          height: 20,
        ),
        Center(
            child: ProfilePicture(
          radius: 50,
        )),
        const Divider(
          height: 40,
          thickness: 3,
          color: Colors.yellow,
        ),
        const SizedBox(
          height: 20,
        ),
        Row(
          children: [
            Expanded(
                child: ElevatedButton(
              onPressed: () {
                print("Spiel erstellen");
               homeViewModel.switchToGameCreate();
              },
              child: const Text("Spiel erstellen", style: TextStyle(fontSize: 15)),
            ))
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
                    print("Spiel beitreten");
                    homeViewModel.switchToConnectToGame();
                  },
                  child:
                      const Text("Spiel beitreten", style: TextStyle(fontSize: 15))),
            )
          ],
        ),
        const Spacer(),
        Align(
          child: FloatingActionButton(
            onPressed: () {
              print("Bearbeiten");
              homeViewModel.navigateToUserEdit();
            },
            child: const Icon(
              Icons.person,
            ),
            backgroundColor: Colors.yellow,
          ),
          alignment: FractionalOffset.bottomRight,
        ),
        const SizedBox(
          height: 20,
        )
      ],
    ),
  );
}
