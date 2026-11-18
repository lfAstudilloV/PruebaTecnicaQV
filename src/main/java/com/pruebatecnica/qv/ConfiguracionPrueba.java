package com.pruebatecnica.qv;

public final class ConfiguracionPrueba {
    private ConfiguracionPrueba() {
    }

    public static String variableRequerida(String nombre) {
        String valor = System.getenv(nombre);
        if (valor == null || valor.isBlank()) {
            throw new IllegalStateException("Configura la variable de entorno " + nombre + " para ejecutar la prueba.");
        }
        return valor;
    }
}
