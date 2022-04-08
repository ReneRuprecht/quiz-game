/// creates a request object for the ready check.
/// this is used to set the players ready state
class GameIdUsernameReadyRequest{
  String username;
  String gameId;
  bool ready;

  GameIdUsernameReadyRequest(this.username, this.gameId,this.ready);
}