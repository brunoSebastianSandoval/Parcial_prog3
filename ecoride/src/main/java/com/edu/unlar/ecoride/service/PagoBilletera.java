package com.edu.unlar.ecoride.service;

public class PagoBilletera implements ProcesarPago{
    @Override
    public double procesarCobro(double monto) {
        // Simulación de procesamiento de pago con billetera virtual
        System.out.println("Procesando pago con billetera virtual... Monto: $" + monto);
        return monto; // Retorna el monto cobrado (sin descuentos adicionales en este caso)
    }
}
