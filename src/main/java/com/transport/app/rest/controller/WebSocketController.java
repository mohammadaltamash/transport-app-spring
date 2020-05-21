package com.transport.app.rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.ExecutionException;

//@CrossOrigin
//        (origins = "*", allowedHeaders = "http://localhost:4200", allowCredentials = "true")
@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate template;

//    @Autowired
//    private WebSocketStompClient stompClient;

//    @Autowired
//    WebSocketController(SimpMessagingTemplate template, WebSocketStompClient stompClient){
//        this.template = template;
//        this.stompClient = stompClient;
//    }

    @MessageMapping("/message")
    @SendTo("/topic/message")
    public void sendMessage(String message){
        this.template.convertAndSend("/topic/message",  message);
    }

    /*@GetMapping("/socketmessage/{message}")
    public void sendMessageToSubscribers(@PathVariable("message") String message) throws ExecutionException, InterruptedException {
        StompSessionHandler sessionHandler = new CustomStompSessionHandler();

        StompSession stompSession = stompClient.connect("ws://localhost:8080/transportapp/ws/websocket",
                sessionHandler).get();

        stompSession.send("topic/message", message);
    }*/
}

/*
class CustomStompSessionHandler extends StompSessionHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(CustomStompSessionHandler .class);

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        logger.info("New session established : " + session.getSessionId());
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers,
                                byte[] payload, Throwable exception) {
        logger.error("Got an exception", exception);
    }
}*/
