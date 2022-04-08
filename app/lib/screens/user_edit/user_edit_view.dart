import 'package:app/screens/user_edit/user_edit_view_model.dart';
import 'package:app/widgets/profile-picture.dart';

import 'package:app/widgets/user_statistic.dart';
import 'package:flutter/material.dart';
import 'package:provider/src/provider.dart';

class UserEditView extends StatefulWidget {
  const UserEditView({Key? key}) : super(key: key);

  @override
  _UserEditViewState createState() => _UserEditViewState();
}

class _UserEditViewState extends State<UserEditView> {
  @override
  Widget build(BuildContext context) {
    UserEditViewModel userEditViewModel = context.watch<UserEditViewModel>();

    return Scaffold(
      appBar: AppBar(
        title: Text(userEditViewModel.title),
        centerTitle: true,
      ),
      body: _ui(context, userEditViewModel),
    );
  }

  @override
  void initState() {
    super.initState();
    context.read<UserEditViewModel>().init();
  }
}

Widget _ui(BuildContext context, UserEditViewModel userEditViewModel) {
  return Padding(
    padding: const EdgeInsets.fromLTRB(20, 30, 20, 0),
    child: Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Center(child: ProfilePicture(radius: 50)),
        const SizedBox(
          height: 30,
        ),
        Center(
          child: ElevatedButton(
              onPressed: () {
                userEditViewModel.uploadProfilePicture();
              },
              child: const Text("foto hochladen")),
        ),
        const SizedBox(height: 50,),
        Center(child: userStatistic(context, userEditViewModel.player?.scoreObject))
      ],
    ),
  );
}
