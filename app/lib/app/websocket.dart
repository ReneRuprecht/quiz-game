import 'dart:async';

/// abstract class for basic websocket functions
abstract class QuizWebSocket {
  StreamSubscription? streamSubscription;

  /// deactivate function is important, the socket needs to be closed
  void deactivate();

  /// for handling the messages
  void handleMessages();
}
