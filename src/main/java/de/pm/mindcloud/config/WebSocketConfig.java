package de.pm.mindcloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/");
//        config.enableSimpleBroker("/rpc/", "/user/", "/reply/", "/notification/", "/updated/");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/mindcloud").addInterceptors(new HttpSessionIdHandshakeInterceptor()).setAllowedOrigins("*").withSockJS();
    }

//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.setInterceptors(inBoundChannel());
//    }
//
//    @Bean
//    public InBoundChannel inBoundChannel() {
//        return new InBoundChannel();
//    }

    @Bean
    public HandshakeInterceptor handshaker() {
        return new HttpSessionIdHandshakeInterceptor();
    }

//    @Bean
//    public SimpleMetadataStore webSocketSessionStore() {
//        return new SimpleMetadataStore();
//    }
//
//    @Bean
//    public SimpleMessageStore chatMessagesStore() {
//        return new SimpleMessageStore();
//    }
}