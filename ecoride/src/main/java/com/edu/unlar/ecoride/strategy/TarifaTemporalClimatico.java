package com.edu.unlar.ecoride.strategy;

public class TarifaTemporalClimatico implements EstrategiaTarifa {
    @Override
    public double calcularCosto(double tarifaBase, int minutos) {
        double costoBase = tarifaBase * minutos;
        return costoBase + 150.0;
    }
}