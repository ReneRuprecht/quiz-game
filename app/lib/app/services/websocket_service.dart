//'ws://192.168.0.72:3000/quiz-game-models'
import 'dart:async';
import 'dart:convert';

import 'package:stomp_dart_client/stomp.dart';
import 'package:stomp_dart_client/stomp_config.dart';
import 'package:stomp_dart_client/stomp_frame.dart';

/// service for the websocket
class WebSocketService {
  StompClient? stompClient;

  /// sets a stream controller, this is used to send information
  /// outside of the class
  StreamController<Map<String, dynamic>> _messageController =
      StreamController<Map<String, dynamic>>();

  StreamController get socketController => _messageController;

  /// activates the stomp connection
  void activate() {
    if (stompClient == null) return;
    stompClient?.activate();
  }

  ///deactivates the connection and closes all the streams
  void deactivate() {
    if (stompClient == null) return;

    _messageController.sink.close();
    _messageController.close();
    stompClient?.deactivate();

    _messageController = StreamController<Map<String, dynamic>>();
  }

  /// set up the stomp client with the url and destination to listen on
  void init(String url, String destination) {
    stompClient = StompClient(
        config: StompConfig(
      url: url,
      onConnect: (StompFrame frame) {
        stompClient?.subscribe(
            destination: destination, callback: onConnectCallback);
      },
      onWebSocketError: onWebSocketError,
    ));
  }

  // gets called if a new message is received
  void onConnectCallback(StompFrame stompFrame) {
    if (stompFrame.body != null) {
      Map<String, dynamic> result = json.decode(stompFrame.body.toString());

      _messageController.sink.add(result);
    }
  }

  // prints on the console of there was an error (debug purpose)
  void onWebSocketError(dynamic error) {
    print("error: " + error.toString());
  }
}
