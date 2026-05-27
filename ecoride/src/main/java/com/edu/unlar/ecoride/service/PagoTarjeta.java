package com.edu.unlar.ecoride.service;

public class PagoTarjeta implements ProcesarPago {
    @Override
    public double procesarCobro(double monto) {
        // Simulación de procesamiento de pago con tarjeta
        System.out.println("Procesando pago con tarjeta... Monto: $" + monto);
        // Aquí se podrían agregar validaciones específicas para tarjetas, etc.
        return monto; // Retorna el monto cobrado (sin descuentos adicionales en este caso)
    }

}
