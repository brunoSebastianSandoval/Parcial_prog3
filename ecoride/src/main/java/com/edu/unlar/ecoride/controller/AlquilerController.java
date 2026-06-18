package com.edu.unlar.ecoride.controller;

import com.edu.unlar.ecoride.model.*;
import com.edu.unlar.ecoride.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alquiler")
public class AlquilerController {

    @Autowired
    private AlquilerService alquilerService;

    @GetMapping("/desbloquear")
    public ResponseEntity<String> desbloquearVehiculo(@RequestParam String idUsuario, @RequestParam String patente, @RequestParam String metodoPago) {
        AlquilerRequest request = new AlquilerRequest(idUsuario, patente, metodoPago);
        String resultado = alquilerService.procesarDesbloqueo(request);
        
        // Manejo de códigos de respuesta HTTP según la lógica devuelta por el servicio
        if (resultado.contains("Alarma") || resultado.contains("Error")) {
            return ResponseEntity.badRequest().body(resultado);
        }
        
        return ResponseEntity.ok(resultado);
    }
    
}

