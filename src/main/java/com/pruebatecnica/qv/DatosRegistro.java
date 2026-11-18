package com.pruebatecnica.qv;

import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

public record DatosRegistro(
        String cedula,
        String nombres,
        String apellidos,
        String correo,
        String contrasena,
        String confirmarContrasena
) {
    private static final SecureRandom ALEATORIO = new SecureRandom();
    private static final List<String> NOMBRES = List.of("Ana", "Luis", "Maria", "Juan", "Sofia", "Andres");
    private static final List<String> APELLIDOS = List.of("Garcia", "Martinez", "Lopez", "Rodriguez", "Perez", "Torres");

    public DatosRegistro {
        cedula = requerido(cedula, "cedula");
        nombres = requerido(nombres, "nombres");
        apellidos = requerido(apellidos, "apellidos");
        correo = requerido(correo, "correo");
        contrasena = requerido(contrasena, "contrasena");
        confirmarContrasena = requerido(confirmarContrasena, "confirmarContrasena");

        if (!contrasena.equals(confirmarContrasena)) {
            throw new IllegalArgumentException("La contraseña y su confirmación deben coincidir.");
        }
    }

    public static DatosRegistro aleatorios() {
        String sufijo = UUID.randomUUID().toString().substring(0, 8);
        String contrasena = "Aa1!" + sufijo + "Zz9#";

        return new DatosRegistro(
                cedulaAleatoria(),
                elegir(NOMBRES),
                elegir(APELLIDOS),
                "prueba" + sufijo + "@example.com",
                contrasena,
                contrasena
        );
    }

    private static String cedulaAleatoria() {
        return (1 + ALEATORIO.nextInt(9)) + String.format("%09d", ALEATORIO.nextInt(1_000_000_000));
    }

    private static String elegir(List<String> valores) {
        return valores.get(ALEATORIO.nextInt(valores.size()));
    }

    private static String requerido(String valor, String nombre) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("Falta proporcionar el dato requerido: " + nombre);
        }
        return valor;
    }
}
