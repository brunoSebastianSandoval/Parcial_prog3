package com.edu.unlar.ecoride.service;

import com.edu.unlar.ecoride.model.*;
import org.springframework.stereotype.Service;
import com.edu.unlar.ecoride.strategy.*;
import com.edu.unlar.ecoride.state.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AlquilerService {

    // REQUERIMIENTO B.1: Reemplazamos la búsqueda en listas por un HashMap global indexado por patente
    // Esto asegura Acceso Instantáneo O(1) sin importar la escala del sistema.
    private final Map<String, Vehiculo> vehiculosMap = new HashMap<>();
    // Mantenemos una relación para saber de qué estación remover el vehículo
    private final Map<String, EstacionAnclaje> estacionPorVehiculoMap = new HashMap<>();
    
    private final List<Usuario> usuarios = new ArrayList<>();
    private EstrategiaTarifa estrategiaActual = new TarifaEstandar();

    // Constructor para inicializar datos de prueba
    public AlquilerService() {
        Usuario u1 = new Usuario("USR11", "Isma Flores", "REGULAR");
        Usuario u2 = new Usuario("USR02", "penelope Lopez", "PREMIUM");
        usuarios.add(u1);
        usuarios.add(u2);

        Monopatin m1 = new Monopatin(true);
        m1.setPatente("AAC111");
        m1.setPorcentajeBateria(80);
        m1.setTarifaFijaBase(500.0);

        BicicletaElectrica b1 = new BicicletaElectrica(1500);
        b1.setPatente("BAB222");
        b1.setPorcentajeBateria(10); // Generará error de batería
        b1.setTarifaFijaBase(600.0);

        EstacionAnclaje est1 = new EstacionAnclaje();
        est1.setNombreUnico("Estacion-Central");
        est1.getVehiculosDisponibles().add(m1);
        est1.getVehiculosDisponibles().add(b1);
        
        // Indexamos los vehículos en el mapa global para que su acceso sea instantáneo O(1)
        vehiculosMap.put(m1.getPatente().toUpperCase(), m1);
        estacionPorVehiculoMap.put(m1.getPatente().toUpperCase(), est1);

        vehiculosMap.put(b1.getPatente().toUpperCase(), b1);
        estacionPorVehiculoMap.put(b1.getPatente().toUpperCase(), est1);
    }

    public String procesarDesbloqueo(AlquilerRequest request) {
        Usuario usuario = buscarUsuario(request.getIdUsuario());
        if (usuario == null) {
            return "Error de negocio: Usuario no registrado.";
        }

        //  Acceso directo por clave en O(1)
        // Eliminamos los bucles anidados "for" que penalizaban el rendimiento
        String patenteBuscada = request.getPatente().toUpperCase();
        Vehiculo vehiculoEncontrado = vehiculosMap.get(patenteBuscada);

        // Regla de Alerta 1: Vehículo No Encontrado
        if (vehiculoEncontrado == null) {
            return "Alarma del Sistema: Vehículo No Encontrado.";
        }

        // Regla de Alerta 2: Batería Insuficiente (< 15%)
        if (vehiculoEncontrado.getPorcentajeBateria() < 15) {
            return "Alarma del Sistema: Batería Insuficiente. Operación bloqueada.";
        }

        //  Aplicamos el Patrón State de forma rígida
        // El vehículo intenta cambiar su estado. Si está en reparación o viaje, el método
        // lanzará una IllegalStateException, controlando las transiciones sin IFs.
        try {
            vehiculoEncontrado.desbloquear();
        } catch (IllegalStateException e) {
            return "Alarma del Sistema: " + e.getMessage();
        }

        // : Cálculo Adaptativo de Tarifa Base de Desbloqueo (Estrategia Activa)
        // Nota: El enunciado pide aplicar diferentes criterios económicos intercambiables
        double importeFinal = estrategiaActual.calcularCosto(vehiculoEncontrado.getTarifaFijaBase(), 1); // Usamos 1 para costo inicial fijado
        
        if (usuario.getTipoUsuario().equalsIgnoreCase("PREMIUM")) {
            importeFinal = importeFinal * 0.85; // Mantiene beneficio exclusivo
        }

        // Desacoplamiento de la creación de pagos y efectuar cobro
        ProcesarPago procesador = DesacopladorPagosFactory.crearProcesador(request.getMetodoPago());
        if (procesador == null) {
            // Revertimos el estado si el pago falla
            vehiculoEncontrado.finalizarViaje(); 
            return "Error de negocio: Medio de pago no soportado.";
        }
        
        procesador.procesarCobro(importeFinal);

        // Remover el vehículo de la estructura física y lógica por alquiler exitoso
        EstacionAnclaje estacionContenedora = estacionPorVehiculoMap.get(patenteBuscada);
        if (estacionContenedora != null) {
            estacionContenedora.getVehiculosDisponibles().remove(vehiculoEncontrado);
        }

        return "Desbloqueo Exitoso. Vehículo Patente: " + vehiculoEncontrado.getPatente() 
                + " | Estado: " + vehiculoEncontrado.getEstadoActual().getNombreEstado()
                + " | Monto cobrado: $" + importeFinal;
    }

    private Usuario buscarUsuario(String idUsuario) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getIdUsuario().equalsIgnoreCase(idUsuario)) {
                return usuarios.get(i);
            }
        }
        return null;
    }

    // Método para cambiar la tarifa en tiempo real (según clima, horario, etc.)
    public void cambiarEstrategiaTarifa(EstrategiaTarifa nuevaEstrategia) {
        this.estrategiaActual = nuevaEstrategia;
    }

    public double calcularCostoViaje(Vehiculo vehiculo, int minutos) {
        double tarifaBase = vehiculo.getTarifaFijaBase();
        return this.estrategiaActual.calcularCosto(tarifaBase, minutos);
    }
}