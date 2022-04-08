const String _ip = "192.168.0.72";
const String _port = "3000";
const String prefix = "http://" + _ip + ":" + _port;

const String apiLogin = prefix + "/api/v1/login";

const String apiRegister = prefix + "/api/v1/registration/register";

// user
const String _apiUser = prefix + "/api/v1/user";
const String apiUserEditProfilePicture = _apiUser + "/upload-profile-picture";
const String apiUserInfo = _apiUser + "/get";

// game
const String _apiGame = prefix + "/api/v1/game";
const String apiCreateGame = _apiGame + "/create";
const String apiDeleteGame = _apiGame + "/delete";
const String apiGetGameInfo = _apiGame + "/getGame";
const String apiGetGames = _apiGame + "/getGames";
const String apiConnectGame = _apiGame + "/connect";
const String apiDisconnectGame = _apiGame + "/disconnect";
const String apiMarkAsReady = _apiGame + "/ready";

const String tokenInvalidResponseMessage = "Token ist nicht valid";


// websocket
const String webSocketUrl = "ws://192.168.0.72:3000/quiz-game-websocket";
const String webSocketTopic ="/topic/game-";