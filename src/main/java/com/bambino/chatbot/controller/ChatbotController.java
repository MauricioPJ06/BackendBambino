package com.bambino.chatbot.controller;

import com.bambino.chatbot.dto.ChatbotConsultaRequest;
import com.bambino.chatbot.dto.ChatbotConsultaResponse;
import com.bambino.chatbot.dto.ChatbotOpcionResponse;
import com.bambino.chatbot.service.ChatbotService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
/**
 * Clase que maneja la funcionalidad de ChatbotController.
 */
public class ChatbotController {

    private final ChatbotService chatbotService;

    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @GetMapping("/api/public/chatbot/opciones")
    /**
     * Metodo que realiza la operacion de opciones.
     * @return resultado de la operacion
     */
    public List<ChatbotOpcionResponse> opciones() {
        return chatbotService.listarOpciones();
    }

    @PostMapping("/api/public/chatbot/consultar")
    /**
     * Metodo que realiza la operacion de consultarPublico.
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ChatbotConsultaResponse consultarPublico(@Valid @RequestBody ChatbotConsultaRequest request) {
        return chatbotService.consultarPublico(request);
    }

    @PostMapping("/api/cliente/chatbot/consultar")
    public ChatbotConsultaResponse consultarCliente(Authentication authentication,
                                                    @Valid @RequestBody ChatbotConsultaRequest request) {
        return chatbotService.consultarCliente(authentication.getName(), request);
    }
}
