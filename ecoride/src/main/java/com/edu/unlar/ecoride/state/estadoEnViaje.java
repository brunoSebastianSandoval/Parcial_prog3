package com.edu.unlar.ecoride.state;

import com.edu.unlar.ecoride.model.Vehiculo;

public class estadoEnViaje implements estadoVehiculo {
    @Override
    public void desbloquear(Vehiculo vehiculo) {
        
        throw new IllegalStateException("El vehículo ya se encuentra en viaje con otro usuario.");
    }

    @Override
    public void finalizarViaje(Vehiculo vehiculo) {
        
        vehiculo.setEstado(new estadoEnEspera());
    }

    @Override
    public void enviarAMantenimiento(Vehiculo vehiculo) {
        throw new IllegalStateException("No se puede enviar a reparación un vehículo que está en movimiento.");
    }

    @Override
    public String getNombreEstado() {
        return "En Viaje";
    }
}