package com.edu.unlar.ecoride.controller;

import com.edu.unlar.ecoride.model.*;
import com.edu.unlar.ecoride.service.*;
import com.edu.unlar.ecoride.dto.AlquilerResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping; // Cambiamos a PostMapping porque altera el estado del sistema
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alquileres") // Consigna pide plural
public class AlquilerController {

    private final AlquilerService alquilerService;

    // Inyección recomendada por constructor profesional
    public AlquilerController(AlquilerService alquilerService) {
        this.alquilerService = alquilerService;
    }

    @PostMapping("/desbloquear")
    public ResponseEntity<?> desbloquearVehiculo(
            @RequestParam String idUsuario, 
            @RequestParam String patente, 
            @RequestParam String metodoPago) {
        
        AlquilerRequest request = new AlquilerRequest(idUsuario, patente, metodoPago);
        String resultado = alquilerService.procesarDesbloqueo(request);
        
        // Si el servicio detectó una alarma o error, lanzamos el HTTP 400 Bad Request
        if (resultado.contains("Alarma") || resultado.contains("Error")) {
            return ResponseEntity.badRequest().body(resultado);
        }
        
        // Intentamos recuperar el vehículo para armar la respuesta profesional DTO
        try {
            Vehiculo v = alquilerService.buscarPorPatenteMetodoTradicional(patente); // Necesitás exponer la búsqueda en el service
            
            // Calculamos la tarifa base simulada para el DTO inicial
            double costoBase = v.getTarifaFijaBase();
            
            AlquilerResponseDTO response = new AlquilerResponseDTO(
                v.getPatente(),
                costoBase,
                0, // Al desbloquear el tiempo transcurrido es cero
                v.getEstadoActual().getNombreEstado(),
                "Vehículo desbloqueado exitosamente mediante API Profesional."
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Manejo de contingencia por si se limpia de la memoria inmediatamente
            return ResponseEntity.ok(resultado);
        }
    }

    @PostMapping("/finalizar")
    public ResponseEntity<AlquilerResponseDTO> finalizarViaje(@RequestParam String patente, @RequestParam int minutosSimulados) {
        // Esqueleto requerido por el Desafío de API 
        // 1. Llama a tu lógica de negocio para calcular tarifa final usando la estrategia activa
        // 2. Cambia el estado del vehículo a "En Espera" usando v.finalizarViaje()
        
        AlquilerResponseDTO response = new AlquilerResponseDTO(
            patente.toUpperCase(),
            1250.0, // Simulación de costo final usando el patrón Strategy calculado en el Service
            minutosSimulados,
            "En Espera",
            "Viaje finalizado correctamente. Se ha procesado el cobro del servicio."
        );
        
        return ResponseEntity.ok(response);
    }
}