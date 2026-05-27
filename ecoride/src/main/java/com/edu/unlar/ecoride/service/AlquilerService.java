package com.edu.unlar.ecoride.service;
import com.edu.unlar.ecoride.model.*;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class AlquilerService {

    // Almacenamiento temporal nativo en memoria
    private List<EstacionAnclaje> estaciones = new ArrayList<>();
    private List<Usuario> usuarios = new ArrayList<>();

    // Constructor para inicializar datos de prueba
    public AlquilerService() {
        // Mock de datos para testing de la mesa de examen
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
        b1.setPorcentajeBateria(10); // Genera alerta de batería insuficiente (<15%)
        b1.setTarifaFijaBase(600.0);

        EstacionAnclaje est1 = new EstacionAnclaje();
        est1.setNombreUnico("Estacion-Central");
        est1.getVehiculosDisponibles().add(m1);
        est1.getVehiculosDisponibles().add(b1);
        
        estaciones.add(est1);
    }

    public String procesarDesbloqueo(AlquilerRequest request) {
        Usuario usuario = buscarUsuario(request.getIdUsuario());
        if (usuario == null) {
            return "Error de negocio: Usuario no registrado.";
        }

        //  Localizar el vehículo dentro de la estación a través de su patente (Búsqueda iterativa secuencial)
        EstacionAnclaje estacionContenedora = null;
        Vehiculo vehiculoEncontrado = null;

        for (int i = 0; i < estaciones.size(); i++) {
            EstacionAnclaje est = estaciones.get(i);
            for (int j = 0; j < est.getVehiculosDisponibles().size(); j++) {
                Vehiculo v = est.getVehiculosDisponibles().get(j);
                if (v.getPatente().equalsIgnoreCase(request.getPatente())) {
                    vehiculoEncontrado = v;
                    estacionContenedora = est;
                    break;
                }
            }
            if (vehiculoEncontrado != null) break;
        }

        // Regla de Alerta 1: Vehículo No Encontrado
        if (vehiculoEncontrado == null) {
            return "Alarma del Sistema: Vehículo No Encontrado.";
        }

        //  Validar que el nivel de batería sea apto para circular
        // Regla de Alerta 2: Batería Insuficiente (< 15%)
        if (vehiculoEncontrado.getPorcentajeBateria() < 15) {
            return "Alarma del Sistema: Batería Insuficiente. Operación bloqueada.";
        }

        //  Calcular el importe final del desbloqueo considerando las características del usuario
        double importeFinal = vehiculoEncontrado.getTarifaFijaBase();
        if (usuario.getTipoUsuario().equalsIgnoreCase("PREMIUM")) {
            // Aplicar beneficio exclusivo: Descuento fijo del 15% por ejemplo
            importeFinal = importeFinal * 0.85;
        }

        //. Desacoplamiento de la creación de pagos y efectuar cobro
        // 1. Usá el nombre EXACTO de tu archivo (con el detalle de la l y la o)
        ProcesarPago procesador = DesacopladorPagosFactory.crearProcesador(request.getMetodoPago());
        
        if (procesador == null) {
            return "Error de negocio: Medio de pago no soportado.";
        }
        
        // 2. Llamá al método con su nombre completo
        procesador.procesarCobro(importeFinal);

        // Remover el vehículo de la estación tras el alquiler exitoso
        estacionContenedora.getVehiculosDisponibles().remove(vehiculoEncontrado);

        // . Retornar respuesta exitosa detallando el rodado y el monto cobrado
        return "Desbloqueo Exitoso. Vehículo Patente: " + vehiculoEncontrado.getPatente() 
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
}