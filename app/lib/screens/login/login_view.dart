import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';

// ignore: implementation_imports
import 'package:provider/src/provider.dart';

import 'login_view_model.dart';

class LoginView extends StatefulWidget {
  const LoginView({Key? key}) : super(key: key);

  @override
  _LoginViewState createState() => _LoginViewState();
}

class _LoginViewState extends State<LoginView> {
  @override
  Widget build(BuildContext context) {
    LoginViewModel loginViewModel = context.watch<LoginViewModel>();

    return Scaffold(
      backgroundColor: Colors.grey[850],
      appBar: AppBar(
        title: Text(loginViewModel.appTitle),
        centerTitle: true,
        backgroundColor: Colors.grey[900],
      ),
      body: _ui(context,loginViewModel),
    );
  }

  @override
  void initState() {
    super.initState();
    context.read<LoginViewModel>().init();
  }
}

Widget _ui(BuildContext context, LoginViewModel loginViewModel) {

  if(loginViewModel.isLoading){
    return const Center(
      child: CircularProgressIndicator(),
    );
  }

  if (loginViewModel.errorMessage != "") {
    Fluttertoast.showToast(
      msg: loginViewModel.errorMessage,
      toastLength: Toast.LENGTH_SHORT,
    );
  }

  return Padding(
    padding: const EdgeInsets.fromLTRB(20, 30, 20, 0),
    child: Column(
      children: <Widget>[
        TextField(
          controller: loginViewModel.usernameController,
          decoration: const InputDecoration(
              border: OutlineInputBorder(), hintText: "Username"),
        ),
        const SizedBox(
          height: 10,
        ),
        TextField(
          controller: loginViewModel.passwordController,
          decoration: const InputDecoration(
              border: OutlineInputBorder(), hintText: "Password"),
        ),
        ElevatedButton(
            onPressed: ()  {
              print("login");
              loginViewModel.login();
            },
            child: const Text("Login")),
      ],
    ),
  );
}
