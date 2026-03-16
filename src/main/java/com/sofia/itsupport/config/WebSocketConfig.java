package com.sofia.itsupport.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Prefijo para los mensajes que el servidor envía a los clientes
        config.enableSimpleBroker("/topic", "/queue");
        // Prefijo para los mensajes que los clientes envían al servidor
        config.setApplicationDestinationPrefixes("/app");
        // Prefijo para las colas de usuario (notificaciones privadas)
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Endpoint que el cliente usará para conectarse
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")  // En producción, restringir orígenes
                .withSockJS();  // Habilita SockJS como fallback
    }
}