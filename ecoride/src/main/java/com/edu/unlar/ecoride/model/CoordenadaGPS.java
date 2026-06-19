package com.edu.unlar.ecoride.model;

import java.util.Objects;

public class CoordenadaGPS {
    private String patente;
    private double latitud;
    private double longitud;
    private long timestamp;

    public CoordenadaGPS(String patente, double latitud, double longitud, long timestamp) {
        this.patente = patente;
        this.latitud = latitud;
        this.longitud = longitud;
        this.timestamp = timestamp;
    }

    // Getters tradicionales
    public String getPatente() { return patente; }
    public double getLatitud() { return latitud; }
    public double getLongitud() { return longitud; }
    public long getTimestamp() { return timestamp; }

    // CRITICAL: Esto permite al HashSet identificar duplicados instantáneamente
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoordenadaGPS that = (CoordenadaGPS) o;
        return Double.compare(that.latitud, latitud) == 0 &&
                Double.compare(that.longitud, longitud) == 0 &&
                Objects.equals(patente, that.patente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patente, latitud, longitud);
    }
}