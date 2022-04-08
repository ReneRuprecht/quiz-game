import 'package:app/screens/start/start_model.dart';
import 'package:flutter/cupertino.dart';

/// viewmodel for the start view
class StartViewModel extends ChangeNotifier {
  StartModel startModel = StartModel();

  bool _loading = false;

  bool get loading => _loading;

  String get appTitle => startModel.title;
}
