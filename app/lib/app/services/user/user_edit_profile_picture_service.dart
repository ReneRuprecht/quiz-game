import 'dart:io';

import 'package:http/http.dart';

import '../../../constants.dart';
import '../../../locator.dart';
import '../secure_storage_service.dart';

/// service to set the profile picture
class UserEditProfilePictureService {
  /// sends a post request with the needed information to upload the profile picture
  /// for the user at the server
  Future<Response> uploadProfilePicture(File file) async {
    SecureStorage secureStorage = locator<SecureStorage>();
    var token = await secureStorage.storage.read(key: 'token');
    Map<String, String> requestHeaders = {'Authorization': token.toString()};

    Uri uri = Uri.parse(apiUserEditProfilePicture);
    MultipartRequest request = MultipartRequest("PUT", uri);
    request.headers.addAll(requestHeaders);

    MultipartFile img = await MultipartFile.fromPath('file', file.path);
    request.files.add(img);
    StreamedResponse response = await request.send();

    return Response.fromStream(response);
  }
}
