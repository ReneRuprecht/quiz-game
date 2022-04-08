package groupd.quiz.websocket;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Configuration
@AllArgsConstructor
public class WebsocketSender {

    private SimpMessagingTemplate template;

    // sends a websocket message with delay
    public void sendMessageWithDelay(String url, Object obj, int delay) {

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {

                        template.convertAndSend(
                                url,
                                obj);
                    }
                },
                delay
        );


    }
}
