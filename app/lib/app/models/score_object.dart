/// class for the websocket response to map it from and to json format
class ScoreObject {
  int? currentScoreValue;
  int? gamesPlayed;
  int? win;
  int? lose;

  ScoreObject({this.currentScoreValue, this.gamesPlayed, this.win, this.lose});

  ScoreObject.fromJson(Map<String, dynamic> json) {
    currentScoreValue = json['currentScoreValue'];
    gamesPlayed = json['gamesPlayed'];
    win = json['win'];
    lose = json['lose'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = Map<String, dynamic>();
    data['currentScoreValue'] = currentScoreValue;
    data['gamesPlayed'] = gamesPlayed;
    data['win'] = win;
    data['lose'] = lose;
    return data;
  }
}
