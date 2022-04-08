class LoginModel {
  String title = "Login-Screen";

  bool loading = false;

  LoginModel(){
    loading=false;
    errorMessage="";
  }
  String token="";
  String errorMessage="";
}
