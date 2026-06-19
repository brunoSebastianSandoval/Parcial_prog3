package com.edu.unlar.ecoride.strategy;

public class TarifaEstandar implements EstrategiaTarifa {
    @Override
    public double calcularCosto(double tarifaBase, int minutos) {
        return tarifaBase * minutos;
    }
}