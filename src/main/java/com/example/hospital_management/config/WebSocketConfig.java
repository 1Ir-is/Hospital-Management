package com.example.hospital_management.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // client sẽ lắng nghe tại đây
        config.setApplicationDestinationPrefixes("/app"); // prefix khi client gửi tin lên
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // endpoint WebSocket
                .setAllowedOriginPatterns("*") // cho phép từ mọi domain (tùy chỉnh nếu cần)
                .addInterceptors(new HttpSessionHandshakeInterceptor()) // liên kết session HTTP với WebSocket
                .withSockJS(); // fallback cho trình duyệt không hỗ trợ WebSocket
    }
}