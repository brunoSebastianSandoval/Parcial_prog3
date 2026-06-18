package com.edu.unlar.ecoride.state;

import com.edu.unlar.ecoride.model.Vehiculo;

public class estadoEnReparacion implements estadoVehiculo {
    @Override
    public void desbloquear(Vehiculo vehiculo) {
        // Evita el problema grave del taller reportado por gerencia técnica
        throw new IllegalStateException("No se puede iniciar viaje: El vehículo está en reparación en el taller.");
    }

    @Override
    public void finalizarViaje(Vehiculo vehiculo) {
        throw new IllegalStateException("Operación inválida para un vehículo en reparación.");
    }

    @Override
    public void enviarAMantenimiento(Vehiculo vehiculo) {
        // Ya está ahí, no hace nada
    }

    @Override
    public String getNombreEstado() {
        return "En Reparación";
    }
}