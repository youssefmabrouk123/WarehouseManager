package com.twd.Warehouse.config;

import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SocketIOConfig {
    private static final Logger logger = LoggerFactory.getLogger(SocketIOConfig.class);

    @Value("${socketio.host:localhost}")
    private String host;

    @Value("${socketio.port:9090}")
    private Integer port;

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(host);
        config.setPort(port);

        SocketIOServer server = new SocketIOServer(config);

        // Log connection/disconnection events
        server.addConnectListener(client ->
                logger.info("Client connected: {}", client.getSessionId()));
        server.addDisconnectListener(client ->
                logger.info("Client disconnected: {}", client.getSessionId()));

        logger.info("Starting Socket.IO server on {}:{}", host, port);
        server.start();

        return server;
    }

    @PreDestroy
    public void stopSocketIOServer() {
        logger.info("Shutting down Socket.IO server");
        socketIOServer().stop();
    }
}