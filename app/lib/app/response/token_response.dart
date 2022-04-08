import 'package:http/http.dart';
/// class for the token response to map it from and to json format
class TokenResponse {
  String? token;
  int? statusCode;
  String? message;

  TokenResponse.fromResponse(Response response) {
    statusCode = response.statusCode;

    if (statusCode != 200) {
      message = "Falsche login daten";
      return;
    }
    token = response.headers['authorization'];
  }
}
