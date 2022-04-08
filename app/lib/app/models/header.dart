/// class for the websocket response to map it from and to json format
class Headers {
  List<String>? info;
  List<String>? error;

  Headers({this.info});

  Headers.fromJson(Map<String, dynamic> json) {
    if (json['Info'] != null) {
      info = json['Info'].cast<String>() ?? "";
    }

    if (json['Error'] != null) {
      error = json['Error'].cast<String>() ?? "";
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = Map<String, dynamic>();
    if (info != null) {
      data['Info'] = info;
    }
    if (error != null) {
      data['Error'] = error;
    }

    return data;
  }
}
