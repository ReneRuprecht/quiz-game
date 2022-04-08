/// class for the websocket response to map it from and to json format
class CurrentQuestion {
  String? question;
  String? answerOne;
  String? answerTwo;
  String? answerThree;
  String? answerFour;

  CurrentQuestion(
      {this.question,
        this.answerOne,
        this.answerTwo,
        this.answerThree,
        this.answerFour});

  CurrentQuestion.fromJson(Map<String, dynamic> json) {
    question = json['question'];
    answerOne = json['answerOne'];
    answerTwo = json['answerTwo'];
    answerThree = json['answerThree'];
    answerFour = json['answerFour'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = Map<String, dynamic>();
    data['question'] = question;
    data['answerOne'] = answerOne;
    data['answerTwo'] = answerTwo;
    data['answerThree'] = answerThree;
    data['answerFour'] = answerFour;
    return data;
  }
}