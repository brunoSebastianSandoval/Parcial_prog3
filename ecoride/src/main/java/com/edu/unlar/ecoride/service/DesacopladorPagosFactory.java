package com.edu.unlar.ecoride.service;

public class DesacopladorPagosFactory {
public static ProcesarPago crearProcesador(String metodoPago) {
        if (metodoPago.equalsIgnoreCase("TARJETA")) {
            return new PagoTarjeta();
        } else if (metodoPago.equalsIgnoreCase("BILLETERA")) {
            return new PagoBilletera();
        }
        return null;
    }
}
