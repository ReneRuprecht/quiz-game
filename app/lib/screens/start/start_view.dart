import 'package:app/app/services/navigation_service.dart';
import 'package:app/screens/start/start_view_model.dart';

import 'package:flutter/material.dart';

// ignore: implementation_imports
import 'package:provider/src/provider.dart';

import '../../locator.dart';



class StartView extends StatefulWidget {
  const StartView({Key? key}) : super(key: key);

  @override
  _StartViewState createState() => _StartViewState();
}

class _StartViewState extends State<StartView> {
  @override
  Widget build(BuildContext context) {
    StartViewModel startViewModel = context.watch<StartViewModel>();

    return Scaffold(
      appBar: AppBar(
        title: Text(startViewModel.appTitle.toString()),
        centerTitle: true,
      ),
      body: _ui(startViewModel),
    );
  }

  _ui(StartViewModel startViewModel) {
    if (startViewModel.loading) {
      return Container(
        color: Colors.blue[800],
      );
    }
    return Container(
        margin: const EdgeInsets.fromLTRB(20, 0, 20, 0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Row(
              children: [
                Expanded(
                  child: ElevatedButton(
                    onPressed: () {
                      print("login clicked");
                      locator<NavigationService>().pushNamed(routeName: '/login');
                    },
                    child: const Text(
                      "Login-page",
                      style: TextStyle(fontSize: 15),
                    ),
                  ),
                )
              ],
            ),
            const SizedBox(
              height: 10,
            ),
            Row(
              children: [
                Expanded(
                  child: ElevatedButton(
                    onPressed: () {
                      print("register clicked");
                      locator<NavigationService>().pushNamed(routeName: '/register');
                    },
                    child: const Text(
                      "Register-page",
                      style: TextStyle(fontSize: 15),
                    ),
                  ),
                )
              ],
            )
          ],
        ));
  }
}
