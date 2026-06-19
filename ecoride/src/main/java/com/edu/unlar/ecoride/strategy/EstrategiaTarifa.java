package com.edu.unlar.ecoride.strategy;

public interface EstrategiaTarifa {
    double calcularCosto(double tarifaBase, int minutos);
}