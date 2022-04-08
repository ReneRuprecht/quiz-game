/// class for the websocket response to map it from and to json format
class ReadyStates {
  bool? username2;
  bool? username1;

  ReadyStates({this.username2, this.username1});

  ReadyStates.fromJson(Map<String, dynamic> json) {
    username2 = json['username2'];
    username1 = json['username1'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = Map<String, dynamic>();
    data['username2'] = username2;
    data['username1'] = username1;
    return data;
  }
}