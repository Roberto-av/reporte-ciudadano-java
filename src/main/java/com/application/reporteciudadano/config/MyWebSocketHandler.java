package com.application.reporteciudadano.config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler {

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Maneja los mensajes entrantes
        String payload = message.getPayload();
        // Puedes procesar el mensaje aquí según tus necesidades
        System.out.println("Mensaje recibido: " + payload);
        // Aquí puedes implementar la lógica para responder al mensaje, enviar actualizaciones, etc.
    }

}
