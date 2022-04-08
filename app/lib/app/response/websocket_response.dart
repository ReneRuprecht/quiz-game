import 'package:app/app/models/body.dart';
import 'package:app/app/models/header.dart';
/// class for the complete websocket  response to map it from and to json format
class WebSocketResponse {
  Headers? headers;
  Body? body;
  int? statusCodeValue;
  String? statusCode;

  WebSocketResponse(
      {this.headers, this.body, this.statusCodeValue, this.statusCode});

  WebSocketResponse.fromJson(Map<String, dynamic> json) {
    headers =
        json['headers'] != null ? Headers.fromJson(json['headers']) : null;
    body = json['body'] != null ? Body.fromJson(json['body']) : null;
    statusCodeValue = json['statusCodeValue'];
    statusCode = json['statusCode'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = Map<String, dynamic>();
    if (headers != null) {
      data['headers'] = headers!.toJson();
    }
    if (body != null) {
      data['body'] = body!.toJson();
    }
    data['statusCodeValue'] = statusCodeValue;
    data['statusCode'] = statusCode;
    return data;
  }
}
