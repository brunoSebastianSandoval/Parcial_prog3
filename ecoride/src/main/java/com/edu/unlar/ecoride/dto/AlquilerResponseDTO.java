package com.edu.unlar.ecoride.dto;

public class AlquilerResponseDTO {
    private String patente;
    private double costoFinalCalculado;
    private long tiempoTranscurridoMinutos; // Para el endpoint de finalizar
    private String faseActualVehiculo;      // Formato amigable: "En Viaje", "En Espera"
    private String mensajeOperacion;

    
    public AlquilerResponseDTO(String patente, double costoFinalCalculado, long tiempoTranscurridoMinutos, String faseActualVehiculo, String mensajeOperacion) {
        this.patente = patente;
        this.costoFinalCalculado = costoFinalCalculado;
        this.tiempoTranscurridoMinutos = tiempoTranscurridoMinutos;
        this.faseActualVehiculo = faseActualVehiculo;
        this.mensajeOperacion = mensajeOperacion;
    }

    
    public String getPatente() { return patente; }
    public double getCostoFinalCalculado() { return costoFinalCalculado; }
    public long getTiempoTranscurridoMinutos() { return tiempoTranscurridoMinutos; }
    public String getFaseActualVehiculo() { return faseActualVehiculo; }
    public String getMensajeOperacion() { return mensajeOperacion; }
}