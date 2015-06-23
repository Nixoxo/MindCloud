package de.pm.mindcloud.config;

import de.pm.mindcloud.web.channel.MindCloudWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry config) {
//        config.setApplicationDestinationPrefixes("/");
//        config.enableSimpleBroker("/rpc/", "/user/", "/reply/", "/notification/", "/updated/");
//    }

//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/mindcloud").addInterceptors(new HttpSessionIdHandshakeInterceptor()).setAllowedOrigins("*").withSockJS();
//    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(), "/mindcloud").addInterceptors(new HttpSessionIdHandshakeInterceptor()).setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler webSocketHandler() {
        return new MindCloudWebSocketHandler();
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