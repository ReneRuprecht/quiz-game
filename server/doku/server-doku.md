# Server Doku

## API Endpoints

## Registration

- /api/v1/registration/register

POST Request

    {
        "username": "User_POST4",
        "password": "1234"
    }

Response

    {
        "scoreObject": {
            "currentScoreValue": 0,
            "gamesPlayed": 0,
            "win": 0,
            "lose": 0
        },
        "username": "User_POST4",
        "profilePicture": ""
    }

## Login

- /api/v1/login

POST Request

    {
        "username":"User_POST4",
        "password":"1234"
    }

Response

    status 200 mit Authorization im Header und token

## User

- /api/v1/user/get

GET Request

Response

    {
        "username": "username1",
        "profilePicture": "",
        "scoreObject": {
            "currentScoreValue": 0,
            "gamesPlayed": 0,
            "win": 0,
            "lose": 0
        }
    }

- /api/v1/user/update

bisher kann nur das Passwort geändert werden

POST Request

    key: password
    value: "HIER NEUES PASSWORT"

Response

    {
        "username": "username1",
        "profilePicture": "",
        "scoreObject": {
            "currentScoreValue": 0,
            "gamesPlayed": 0,
            "win": 0,
            "lose": 0
        }
    }

### Profilbild

- /api/v1/user/upload-profile-picture

PUT Request

    File: Datei.jpg

Response

    {
        "profilePicture": "user-photos/username1/5bHWsa.jpg"
    }

- /api/v1/user/get-user-with-profile-picture

GET Request, braucht einen token

        username=username1

Response

    {
    "user": {
        "username": "username2",
        "profilePicture": "user-photos/username2/5bHWsa.jpg",
        "scoreObject": {
            "currentScoreValue": 0,
            "gamesPlayed": 0,
            "win": 0,
            "lose": 0
        }
    },
    "profilePictureBase64": "" 
    }

profilePictureBase64 ist ein Base64 encoded String

### Profilbild statisch einsehbar

- Über Url direkt einsehbar http://localhost:3000/user-photos/{username}/{fileName}
  der pfad steht auch im User objekt unter 'profilePicture'

## Question

- /api/v1/question/create

POST Request

    {
        "question":"Frage 1",
        "answerOne":"Antwort_1",
        "answerTwo":"Antwort_2",
        "answerThree":"Antwort_3",
        "answerFour":"Antwort_4",
        "answerCorrect":"Antwort_Correct"
    }

Response

    {
        "question": "Frage 1",
        "answerOne": "Antwort_1",
        "answerTwo": "Antwort_2",
        "answerThree": "Antwort_3",
        "answerFour": "Antwort_4"
    }

## Game

- /api/v1/game/getGames

GET Request

Response Status code 200

    {
        "946231cc-227d-44c9-b19a-d12cd448cb07": {
            "gameId": "946231cc-227d-44c9-b19a-d12cd448cb07",
            "playerOne": {
                "username": "username1",
                "profilePicture": "",
                "scoreObject": {
                    "currentScoreValue": 0,
                    "gamesPlayed": 0,
                    "win": 0,
                    "lose": 0
                }
            },
            "playerTwo": {
                "username": "username2",
                "profilePicture": "",
                "scoreObject": {
                    "currentScoreValue": 0,
                    "gamesPlayed": 0,
                    "win": 0,
                    "lose": 0
                }
            },
            "winner": null,
            "date": "2022-01-27",
            "currentQuestion": {
                "question": "question1",
                "answerOne": "answer1",
                "answerTwo": "answer2",
                "answerThree": "answer3",
                "answerFour": "answer4"
            },
            "gameStatus": "NOT_READY",
            "readyStates": {
                "username2": false,
                "username1": false
            },
            "questionsListEmpty": false
        }
    }

- /api/v1/game/create

POST Request

    {
        "username":"username2",
    }

Response Status code 200

    {
        "gameId": "78c66c33-861e-4d9c-af1c-d9fbc8f184a0",
        "playerOne": {
            "username": "username2",
            "profilePicture": "user-photos/username2/5bHWsa.jpg",
            "scoreObject": {
                "currentScoreValue": 0,
                "gamesPlayed": 0,
                "win": 0,
                "lose": 0
            }
        },
        "playerTwo": null,
        "winner": null,
        "date": "2022-01-29",
        "currentQuestion": null,
        "gameStatus": "OPEN",
        "readyStates": {
            "username2": false
        },
        "questionsListEmpty": true
    }

- /api/v1/game/connect

POST Request

    {
        "username":"username1",
        "gameId":"78c66c33-861e-4d9c-af1c-d9fbc8f184a0"
    }

Response Status code 200

    {
        "gameId": "78c66c33-861e-4d9c-af1c-d9fbc8f184a0",
        "playerOne": {
            "username": "username2",
            "profilePicture": "user-photos/username2/5bHWsa.jpg",
            "scoreObject": {
                "currentScoreValue": 0,
                "gamesPlayed": 0,
                "win": 0,
                "lose": 0
            }
        },
        "playerTwo": {
            "username": "username1",
            "profilePicture": "",
            "scoreObject": {
                "currentScoreValue": 0,
                "gamesPlayed": 0,
                "win": 0,
                "lose": 0
            }
        },
        "winner": null,
        "date": "2022-01-29",
        "currentQuestion": {
            "question": "question1",
            "answerOne": "answer1",
            "answerTwo": "answer2",
            "answerThree": "answer3",
            "answerFour": "answer4"
        },
        "gameStatus": "NOT_READY",
        "readyStates": {
            "username2": false,
            "username1": false
        },
        "questionsListEmpty": false
    }

- /api/v1/game/ready

POST Request

    {
        "username":"username1",
        "gameId":"78c66c33-861e-4d9c-af1c-d9fbc8f184a0",
        "ready":true
    }

Response Status code 200

    True



- /api/v1/game/disconnect

POST Request

    {
        "username":"username1",
        "gameId":"78c66c33-861e-4d9c-af1c-d9fbc8f184a0"
    }

Response 200, wenn player2 disconnected, dann wird folgendes zurückgegeben

    {
        "gameId": "78c66c33-861e-4d9c-af1c-d9fbc8f184a0",
        "playerOne": {
            "username": "username2",
            "profilePicture": "user-photos/username2/5bHWsa.jpg",
            "scoreObject": {
                "currentScoreValue": 0,
                "gamesPlayed": 0,
                "win": 0,
                "lose": 0
            }
        },
        "playerTwo": null,
        "winner": null,
        "date": "2022-01-29",
        "currentQuestion": {
            "question": "question1",
            "answerOne": "answer1",
            "answerTwo": "answer2",
            "answerThree": "answer3",
            "answerFour": "answer4"
        },
        "gameStatus": "NOT_READY",
        "readyStates": {
            "username2": false
        },
        "questionsListEmpty": false
    }

Response 200, wenn player1 disconnected, dann wird das game gelöscht



- /api/v1/game/delete
  

    is used internally

### GameStatus

    GameStatus {
        OPEN,
        NOT_READY,
        STARTED,
        FINISHED
    
    }

Game Status ist STARTED wenn beide Spieler bereit sind


