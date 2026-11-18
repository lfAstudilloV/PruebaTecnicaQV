package com.pruebatecnica.qv;

public record DatosCompra(
        String pais,
        String departamento,
        String ciudad,
        String telefono,
        String direccion
) {
    public static DatosCompra prueba() {
        return new DatosCompra("Colombia", "Antioquía", "Medellín", "3000000000", "Calle de prueba 123");
    }
}
