class RegisterModel {
  int responseCode = 0;
  String responseBody = "";
  String errorMessage = "";

  bool loading = false;

  RegisterModel() {
    responseCode = 0;
    responseBody = "";
    errorMessage = "";
    loading = false;
  }
}
