# Websocket Doku

## Endpoints

- base url: http://IP:PORT/quiz-game-websocket  
IP: zb. localhost oder IP von der Anwendung (Spring boot IP) 
Port: zb. Port von der Application bzw Container (3000)
- topic: /topic/game-{gameid}
WIRD DERZEIT NICHT BENUTZT- game-info: /game-info-{gameid}
- game-play: /app/gameplay-{gameid}


## Wichtig

Nachdem beide Spieler bestätigt haben (ihren ready wert auf true gesetzt per rest api call),
wird eine Nachricht nach ca 10 Sekunden mit dem Game object an beide Spieler gesendet.


## Messages

    // game
    game_not_found,
    game_info,
    game_already_finished,
    game_not_ready,
    game_already_full,
    game_over,

    // answers
    answer_incorrect,
    answer_correct,

    // user
    user_not_found,
    username_invalid,
    user_left,

    // websocket
    user_joined,
    game_ready

## Message Header

    Info,
    Error


## Requests

### GameInfo DERZEIT NICHT GEBRAUCHT

Parameter

    {
        gameid: gameid
        username: username
    }

- gibt einen Error im Header zurück, 
sofern das Game mit der ID nicht in der Lobby ist.


    Error:  game_not_found

- wird mit einem umgültigen usernamen die game info
angefragt, so wird ein Error


    Error:  user_not_found

- sind alle Parameter richtig, so wird das Game
zurückgegeben


        {
           "headers":{
              "Info":[
                 "game_info"
              ]
           },
           "body":{
              "gameId":"2e7a6743-d16b-4759-b45f-89f3036e75b3",
              "playerOne":{
                 "scoreObject":{
                    "currentScoreValue":0,
                    "gamesPlayed":0,
                    "win":0,
                    "lose":0
                 },
                 "username":"username1",
                 "profilePicture":""
              },
              "playerTwo":{
                 "scoreObject":{
                    "currentScoreValue":0,
                    "gamesPlayed":0,
                    "win":0,
                    "lose":0
                 },
                 "username":"username2",
                 "profilePicture":""
              },
              "winner":null,
              "date":"2022-01-21",
              "currentQuestion":{
                 "question":"question1",
                 "answerOne":"answer1",
                 "answerTwo":"answer2",
                 "answerThree":"answer3",
                 "answerFour":"answer4"
              },
              "gameStatus":"IN_PROGRESS"
           },
           "statusCode":"OK",
           "statusCodeValue":200
        }

## Gameplay

Request

Parameter

    {
        gameId: gameid
        senderUsername: username
        answer: answer
    }

- falsche antwort, info ist answer_incorrect

Response

    {
        "headers": {
            "Info": [
                "answer_incorrect"
            ]
        },
        "body": {
            "gameId": "2e7a6743-d16b-4759-b45f-89f3036e75b3",
            "playerOne": {
                "scoreObject": {
                    "currentScoreValue": 0,
                    "gamesPlayed": 0,
                    "win": 0,
                    "lose": 0
                },
                "username": "username1",
                "profilePicture": ""
            },
            "playerTwo": {
                "scoreObject": {
                    "currentScoreValue": 0,
                    "gamesPlayed": 0,
                    "win": 0,
                    "lose": 0
                },
                "username": "username2",
                "profilePicture": ""
            },
            "winner": null,
            "date": "2022-01-21",
            "currentQuestion": {
                "question": "question1",
                "answerOne": "answer1",
                "answerTwo": "answer2",
                "answerThree": "answer3",
                "answerFour": "answer4"
            },
            "gameStatus": "IN_PROGRESS"
        },
        "statusCode": "OK",
        "statusCodeValue": 200
    }

- richtige antwort

Response, info answer_correct

    {
        "headers": {
            "Info": [
                "answer_correct"
            ]
        },
        "body": {
            "gameId": "060c6c4d-0169-4714-a6ba-e0276dea2069",
            "playerOne": {
                "scoreObject": {
                    "currentScoreValue": 1,
                    "gamesPlayed": 0,
                    "win": 0,
                    "lose": 0
                },
                "username": "username1",
                "profilePicture": ""
            },
            "playerTwo": {
                "scoreObject": {
                    "currentScoreValue": 0,
                    "gamesPlayed": 0,
                    "win": 0,
                    "lose": 0
                },
                "username": "username2",
                "profilePicture": ""
            },
            "winner": null,
            "date": "2022-01-22",
            "currentQuestion": {
                "question": "question2",
                "answerOne": "answer1",
                "answerTwo": "answer2",
                "answerThree": "answer3",
                "answerFour": "answer4"
            },
            "gameStatus": "IN_PROGRESS"
        },
        "statusCode": "OK",
        "statusCodeValue": 200
    }

- wenn das game zuende ist

Response, gameStatus finished und info game_over, 
winner ist gesetzt mit dem usernamen

    {
        "headers": {
            "Info": [
                "game_over"
            ]
        },
        "body": {
            "gameId": "060c6c4d-0169-4714-a6ba-e0276dea2069",
            "playerOne": {
                "scoreObject": {
                    "currentScoreValue": 3,
                    "gamesPlayed": 1,
                    "win": 1,
                    "lose": 0
                },
                "username": "username1",
                "profilePicture": ""
            },
            "playerTwo": {
                "scoreObject": {
                    "currentScoreValue": 0,
                    "gamesPlayed": 1,
                    "win": 0,
                    "lose": 1
                },
                "username": "username2",
                "profilePicture": ""
            },
            "winner": "username1",
            "date": "2022-01-22",
            "currentQuestion": {
                "question": "question3",
                "answerOne": "answer1",
                "answerTwo": "answer2",
                "answerThree": "answer3",
                "answerFour": "answer4"
            },
            "gameStatus": "FINISHED"
        },
        "statusCode": "OK",
        "statusCodeValue": 200
    }


- wenn das game unentschieden ist

Response, gameStatus finished, winner null, info game_over

    {
        "headers": {
            "Info": [
                "game_over"
            ]
        },
        "body": {
            "gameId": "371018b4-6851-4b05-b618-540970913c80",
            "playerOne": {
                "scoreObject": {
                    "currentScoreValue": 2,
                    "gamesPlayed": 1,
                    "win": 0,
                    "lose": 0
                },
                "username": "username1",
                "profilePicture": ""
            },
            "playerTwo": {
                "scoreObject": {
                    "currentScoreValue": 2,
                    "gamesPlayed": 1,
                    "win": 0,
                    "lose": 0
                },
                "username": "username2",
                "profilePicture": ""
            },
            "winner": null,
            "date": "2022-01-22",
            "currentQuestion": {
                "question": "question4",
                "answerOne": "answer1",
                "answerTwo": "answer2",
                "answerThree": "answer3",
                "answerFour": "answer4"
            },
            "gameStatus": "FINISHED"
        },
        "statusCode": "OK",
        "statusCodeValue": 200
    }


## Extras

- Wenn ein user joined, so wird im header die info user_joined inklusive der anderen infos gesendet

         "Info": [
                    "user_joined"
                ]

- Wenn ein user disconnected, so wird im header die info user_left inklusive der anderen infos gesendet
 
         "Info": [
                    "user_left"
                ]

- Wenn beide user ready sind, so wird nach ca 10 sekunden im header die info game_ready inklusive der anderen infos gesendet

         "Info": [
                    "game_ready"
                ]
