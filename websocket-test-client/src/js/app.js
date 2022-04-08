let stompClient;
let gameid;

const connectBtn = document.getElementById("connectBtn");
const disconenctBtn = document.getElementById("disconnectBtn");
const getGameInfoBtn = document.getElementById("getGameInfoBtn");

const sendPayloadBtn = document.getElementById("sendPayloadBtn");

const leaveBtn = document.getElementById("leaveBtn");

function init() {
    disconenctBtn.disabled = true;
    sendPayloadBtn.disabled = true;

    connectBtn.addEventListener("click", connectToSocket, false);
    getGameInfoBtn.addEventListener("click", getGameInfo, false);
    disconenctBtn.addEventListener("click", disconnectFromSocket, false);
    sendPayloadBtn.addEventListener("click", sendAnswer, false);
    leaveBtn.addEventListener("click", leave, false);
}

init();


function leave() {
    var http = new XMLHttpRequest();
    var url = 'http://localhost:3000/api/v1/game/disconnect';

    http.open('POST', url, true);

    const gamePayload = JSON.stringify({
        "username": document.getElementById('username').value,
        "gameId": document.getElementById('gameid').value,

    })



    //Send the proper header information along with the request
    http.setRequestHeader('Content-type', 'application/json; charset=UTF-8');
    http.setRequestHeader('Authorization', 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VybmFtZTIiLCJhdXRob3JpdGllcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9VU0VSIn1dLCJpYXQiOjE2NDMwNjEyMjEsImV4cCI6MTY0Mzg0NjQwMH0.EeozjMS9M2XwVh1eZ_nE63m0jQ9M7brB188O2dT-_CVeEH7R30Y5qk6HxPL2RDIEUgC8WIqULOHOyxsv0U5iSw');

    http.onreadystatechange = function () {//Call a function when the state changes.
        console.log(http.responseText)
        if (http.readyState == 4 && http.status == 200) {
            alert(http.responseText);
        }
    }
    http.send(gamePayload);
}


function buttonConnectedState() {
    disconenctBtn.disabled = false;
    sendPayloadBtn.disabled = false;
    connectBtn.disabled = true;
}
function buttonDisconnectedState() {
    disconenctBtn.disabled = true;
    sendPayloadBtn.disabled = true;
    connectBtn.disabled = false;
}



function connectToSocket() {
    const socket = new SockJS('http://localhost:3000/quiz-game-websocket')
    stompClient = Stomp.over(socket)
    stompClient.debug = null
    stompClient.connect({}, (frame) => {
        gameid = document.getElementById("gameid").value;
        if (gameid == "") {
            disconnectFromSocket()
            return;
        }
        console.log("connected to: ", gameid)
        buttonConnectedState();

        stompClient.subscribe('/topic/errors', (gameData) => {
            console.warn('errors: ' + gameData.body)
        });

        stompClient.subscribe('/topic/game-' + gameid, (gameData) => {



            let headers = JSON.parse(gameData.body)['headers']
            if (headers.Error) {
                console.error("Error: ", headers.Error[0])
                return;
            }

            gamePlay(gameData.body);



        });


    });
}

function gamePlay(gameData) {
    console.log(gameData)
    let data = JSON.parse(gameData);
    data = data.body;
    if (data == null) {
        return;
    }
    const gameStatus = data['gameStatus'];

    if (gameStatus == "FINISHED") {
        console.log("Game zuende");
        stompClient.disconnect();
        return;
    }
    if (gameStatus == "IN_PROGRESS") {
        console.log("Game läuft");
    }

    const currentQuestion = data['currentQuestion'];

    if (currentQuestion == "" || currentQuestion == null) {
        return;
    }


    insertPossibleAnswers(currentQuestion)

    let myUser;

    if (data['playerOne']['username'] == document.getElementById("username").value) {
        myUser = data['playerOne'];

    }
    else if (data['playerTwo']['username'] == document.getElementById("username").value) {
        myUser = data['playerTwo'];

    }
    else {
        console.log("error send correct user");
        return;
    }

    console.log("Data: ", data);

}



function getMyCurrentScore(myUserData) {
    document.getElementById("myUsername").innerText = myUserData['username'];
    document.getElementById("myUsername").innerText = myUserData['scoreObject'];
}

function insertPossibleAnswers(data) {
    document.getElementById("question").innerText = data['question'];
    document.getElementById("a1").innerText = data['answerOne'];
    document.getElementById("a2").innerText = data['answerTwo'];
    document.getElementById("a3").innerText = data['answerThree'];
    document.getElementById("a4").innerText = data['answerFour'];

}

function disconnectFromSocket() {
    stompClient.disconnect();
    if (!stompClient.connected) {
        buttonDisconnectedState()
    }

}



function sendAnswer() {
    const gameid = document.getElementById("gameid").value;
    const username = document.getElementById("username").value;
    const answer = document.getElementById("answer").value;

    if (gameid == "" || username == "" || answer == "") {

        console.log("empty payload")
        return
    }
    const gamePayload = JSON.stringify({
        gameId: gameid,
        senderUsername: username,
        answer: answer
    })

    sendWithStompClient('/app/gameplay-', gamePayload, gameid)

}



function getGameInfo() {
    const gameid = document.getElementById("gameid").value;
    const username = document.getElementById("username").value;
    if (gameid == "") {

        console.log("empty gameid")
        return
    }
    if (username == "") {
        console.log("empty username")
        return
    }

    const gamePayload = JSON.stringify({
        gameId: gameid,
        username: username
    })

    sendWithStompClient('/app/game-join-', gamePayload, gameid);

}

function sendWithStompClient(destination, payload, gameid) {


    stompClient.send(destination + gameid, {}, payload)
}