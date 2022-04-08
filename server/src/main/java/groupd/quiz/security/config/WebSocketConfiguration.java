package groupd.quiz.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
    /**
     * sets up the endpoints for the websocket
     *
     * @param config gets inserted automatically by spring
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * registers the endpoints and options for the cross-origin policy
     *
     * @param registry gets inserted automatically by spring
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/quiz-game-websocket")
                .setAllowedOriginPatterns("*://*")
                .withSockJS();
        registry.addEndpoint("/quiz-game-websocket")
                .setAllowedOriginPatterns("*://*");
    }
}
