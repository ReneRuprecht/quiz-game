package main;

import org.apache.log4j.Logger;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;


import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Created by nick on 30/09/2015.
 * https://github.com/nickebbutt/stomp-websockets-java-client/blob/master/stomp-java-client/src/main/java/com/od/helloclient/HelloClient.java
 */
public class SocketClient {

    private static Logger logger = Logger.getLogger(SocketClient.class);

    private final static WebSocketHttpHeaders headers = new WebSocketHttpHeaders();

    public ListenableFuture<StompSession> connect() {

        Transport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
        List<Transport> transports = Collections.singletonList(webSocketTransport);

        SockJsClient sockJsClient = new SockJsClient(transports);
        sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());

        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);

        String url = "ws://{host}:{port}/quiz-game-websocket";
        return stompClient.connect(url, headers, new MyHandler(), "localhost", 3000);
    }

    public static String msg="";
    public void subscribeGreetings(StompSession stompSession, String dst) {
        stompSession.subscribe(dst, new StompFrameHandler() {

            public Type getPayloadType(StompHeaders stompHeaders) {
                return byte[].class;
            }

            public void handleFrame(StompHeaders stompHeaders, Object o) {
                logger.info("Received greeting " + new String((byte[]) o));
                msg = new String((byte[]) o);
            }
        });
    }

    public void sendMsg(StompSession stompSession, String msg, String dst) {
        String jsonHello = msg;

        System.out.println(jsonHello);

        stompSession.send(dst, jsonHello.getBytes());

    }

    private class MyHandler extends StompSessionHandlerAdapter {
        public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
            logger.info("Now connected");
        }
    }



}
