import 'package:flutter/material.dart';

/// gets shown if an error occurs at the navigation
class ErrorView extends StatelessWidget {
  const ErrorView({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Fehler"),
      ),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.start,
        children: const [
          Center(
            child: Text("Error",
              style: TextStyle(
                  fontSize: 50,
                  letterSpacing: 1
              ),),
          )
        ],
      ),
    );
  }
}
