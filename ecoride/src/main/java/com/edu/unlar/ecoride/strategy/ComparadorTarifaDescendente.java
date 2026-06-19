package com.edu.unlar.ecoride.strategy;

import com.edu.unlar.ecoride.model.Vehiculo;
import java.util.Comparator;

//  Criterio Alternativo externo
public class ComparadorTarifaDescendente implements Comparator<Vehiculo> {
    
    @Override
    public int compare(Vehiculo v1, Vehiculo v2) {
        // Para que sea DESCENDENTE (mayor a menor), invertimos el orden: comparamos v2 contra v1
        return Double.compare(v2.getTarifaFijaBase(), v1.getTarifaFijaBase());
    }
}