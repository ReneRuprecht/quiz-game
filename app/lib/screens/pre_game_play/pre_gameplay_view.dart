import 'package:app/screens/pre_game_play/pre_gameplay_view_,model.dart';
import 'package:app/widgets/profile_joined_user.dart';
import 'package:app/widgets/user_statistic.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:provider/src/provider.dart';

class PreGamePlayView extends StatefulWidget {
  const PreGamePlayView({Key? key}) : super(key: key);

  @override
  _PreGamePlayViewState createState() => _PreGamePlayViewState();
}

class _PreGamePlayViewState extends State<PreGamePlayView> {
  @override
  Widget build(BuildContext context) {
    PreGamePlayViewModel preGamePlayViewModel = context.watch<PreGamePlayViewModel>();
    return WillPopScope(
      onWillPop: () async => false,
      child: Scaffold(
        appBar: AppBar(
          title: const Text("Gegner gefunden"),
          centerTitle: true,
          leading: _appbarButton(context,preGamePlayViewModel),
        ),
        body: _ui(context, preGamePlayViewModel),
      ),
    );
  }


  Widget _appbarButton(BuildContext context,
      PreGamePlayViewModel preGamePlayViewModel) {

    return IconButton(
      icon: const Icon(Icons.arrow_back),
      onPressed: () {
        preGamePlayViewModel.leave();
      },
    );
  }

  @override
  void initState() {
    super.initState();
    context.read<PreGamePlayViewModel>().init();

  }
}

Widget _ui(BuildContext context, PreGamePlayViewModel preGamePlayViewModel){

  return userFoundScreen(context, preGamePlayViewModel);
}


Widget userFoundScreen(
    BuildContext context,PreGamePlayViewModel preGamePlayViewModel ) {

  return Padding(
      padding: const EdgeInsets.fromLTRB(20, 0, 20, 0),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.start,
        children: [
          profileJoinedPlayer(context,
              preGamePlayViewModel.opponent?.username ?? "",
              preGamePlayViewModel.opponent?.profilePicture ?? ""),
          const SizedBox(height: 20,),
          userStatistic(context, preGamePlayViewModel.opponent?.scoreObject),
          Expanded(
            child: Row(
              children: [
                Expanded(child: readyButton(context, preGamePlayViewModel)),
              ],
            ),
          ),

        ],
      ));
}



Widget readyButton(
    BuildContext context, PreGamePlayViewModel preGamePlayViewModel) {

  return ElevatedButton(
      onPressed:(){
        preGamePlayViewModel.markAsReady(true);
      },
      child: const Text(
        "Bereit",
      ));
}

