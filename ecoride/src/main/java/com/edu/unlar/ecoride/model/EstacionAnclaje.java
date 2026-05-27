package com.edu.unlar.ecoride.model;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstacionAnclaje {
    private String nombreUnico;
    private List<Vehiculo> vehiculosDisponibles = new ArrayList<>();
}
