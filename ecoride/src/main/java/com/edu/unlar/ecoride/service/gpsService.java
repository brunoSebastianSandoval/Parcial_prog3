package com.edu.unlar.ecoride.service;

import org.springframework.stereotype.Service;
import com.edu.unlar.ecoride.model.CoordenadaGPS;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GpsService {

    // REQUERIMIENTO B.2: Procesamiento lineal ultra-eficiente O(N)
    public List<CoordenadaGPS> deduplicarReportes(List<CoordenadaGPS> reportesDuplicados) {
        if (reportesDuplicados == null) {
            return new ArrayList<>();
        }

        // Estructura auxiliar indexada que no permite duplicados
        Set<CoordenadaGPS> conjuntoLimpio = new HashSet<>();
        
        // UNA SOLA PASADA: Recorremos la lista secuencialmente una única vez
        for (int i = 0; i < reportesDuplicados.size(); i++) {
            CoordenadaGPS reporte = reportesDuplicados.get(i);
            // El método .add() de HashSet comprueba la existencia en tiempo constante O(1)
            conjuntoLimpio.add(reporte); 
        }

        // Convertimos el conjunto limpio de vuelta a una lista tradicional para retornar
        return new ArrayList<>(conjuntoLimpio);
    }
}