package com.edu.unlar.ecoride.state;

import com.edu.unlar.ecoride.model.Vehiculo;

public class estadoEnEspera implements estadoVehiculo {
    @Override
    public void desbloquear(Vehiculo vehiculo) {
        
        vehiculo.setEstado(new estadoEnViaje());
    }

    @Override
    public void finalizarViaje(Vehiculo vehiculo) {
        throw new IllegalStateException("No se puede finalizar un viaje que no ha comenzado.");
    }

    @Override
    public void enviarAMantenimiento(Vehiculo vehiculo) {
        
        vehiculo.setEstado(new estadoEnReparacion());
    }

    @Override
    public String getNombreEstado() {
        return "En Espera";
    }
}