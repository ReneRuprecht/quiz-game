import 'package:app/constants.dart';
import 'package:http/http.dart';

/// service for getting the information from a user
class UserInfoService {
  /// sends a get request with the needed information to get the information
  Future<Response> getUserInfo({token}) async {
    return get(
      Uri.parse(apiUserInfo),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
        'Authorization': token,
      },
    );
  }
}
