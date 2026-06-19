package com.edu.unlar.ecoride.model;

import com.edu.unlar.ecoride.state.estadoVehiculo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Vehiculo implements Comparable<Vehiculo> {
    private String patente;
    private int porcentajeBateria; // 0 a 100
    private double tarifaFijaBase;
    private estadoVehiculo estadoActual;
    public void desbloquear() {
        this.estadoActual.desbloquear(this);
    }

    public void finalizarViaje() {
        this.estadoActual.finalizarViaje(this);
    }

    public void enviarAMantenimiento() {
        this.estadoActual.enviarAMantenimiento(this);
    }

    // Setter para que los estados cambien la fase del ciclo de vida
    public void setEstado(estadoVehiculo nuevoEstado) {
        this.estadoActual = nuevoEstado;
    }

    @Override
    public int compareTo(Vehiculo otro) {
        return this.patente.compareTo(otro.patente);
    }

    // Tus getters y setters del primer parcial se mantienen abajo...
    public String getPatente() { return patente; }
    public int getPorcentajeBateria() { return porcentajeBateria; }
    public double getTarifaFijaBase() { return tarifaFijaBase; }
    public estadoVehiculo getEstadoActual() { return estadoActual; }
    
}

