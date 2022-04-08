import 'package:app/screens/register/register_view_model.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';

// ignore: implementation_imports
import 'package:provider/src/provider.dart';

class RegisterView extends StatefulWidget {
  const RegisterView({Key? key}) : super(key: key);

  @override
  _RegisterViewState createState() => _RegisterViewState();
}

class _RegisterViewState extends State<RegisterView> {
  @override
  Widget build(BuildContext context) {
    RegisterViewModel registerViewModel = context.watch<RegisterViewModel>();
    return Scaffold(
        appBar: AppBar(
          title: const Text("Register-Screen"),
        ),
        body: _ui(context, registerViewModel));
  }

  @override
  void initState() {
    super.initState();
    context.read<RegisterViewModel>().init();
  }
}

Widget _ui(BuildContext context, RegisterViewModel registerViewModel) {
  if (registerViewModel.loading) {
    return const Center(
      child: CircularProgressIndicator(),
    );
  }

  if (registerViewModel.errorMessage != "") {
    Fluttertoast.showToast(
      msg: registerViewModel.errorMessage,
      toastLength: Toast.LENGTH_SHORT,
    );
  }

  return Padding(
    padding: const EdgeInsets.fromLTRB(20, 30, 20, 0),
    child: Column(
      children: <Widget>[
        TextField(
          controller: registerViewModel.usernameController,
          decoration: const InputDecoration(
              border: OutlineInputBorder(), hintText: "Username"),
        ),
        const SizedBox(
          height: 10,
        ),
        TextField(
          controller: registerViewModel.passwordController,
          decoration: const InputDecoration(
              border: OutlineInputBorder(), hintText: "Password"),
        ),
        ElevatedButton(
            onPressed: () async {
              registerViewModel.register();
            },
            child: const Text("Register")),
      ],
    ),
  );
}
