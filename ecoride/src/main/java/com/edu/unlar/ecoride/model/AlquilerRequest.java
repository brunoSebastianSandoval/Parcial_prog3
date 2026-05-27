package com.edu.unlar.ecoride.model;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlquilerRequest {
    private String idUsuario;
    private String patente;
    private String metodoPago; // "TARJETA" o "BILLETERA"
}
