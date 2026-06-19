package com.edu.unlar.ecoride.strategy;

public class TarifaHoraPico implements EstrategiaTarifa {
    @Override
    public double calcularCosto(double tarifaBase, int minutos) {
        double costoBase = tarifaBase * minutos;
        return costoBase + (costoBase * 0.40); 
    }
}