package com.edu.unlar.ecoride.state;
import com.edu.unlar.ecoride.model.Vehiculo;
public interface estadoVehiculo {
    void desbloquear(Vehiculo vehiculo);
    void finalizarViaje(Vehiculo vehiculo);
    void enviarAMantenimiento(Vehiculo vehiculo);
    String getNombreEstado();

}
