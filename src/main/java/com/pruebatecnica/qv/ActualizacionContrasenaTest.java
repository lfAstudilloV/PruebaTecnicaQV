package com.pruebatecnica.qv;

import com.pruebatecnica.qv.pages.MiCuentaPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.UUID;

public class ActualizacionContrasenaTest {
    public static void main(String[] args) {
        String cedula = ConfiguracionPrueba.variableRequerida("BONBONITE_CEDULA");
        String contrasenaActual = args.length > 0
                ? args[0]
                : ConfiguracionPrueba.variableRequerida("BONBONITE_CONTRASENA");
        String contrasenaNueva = "Ab1!" + UUID.randomUUID().toString().substring(0, 12) + "Zy9#";

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--disable-gpu", "--no-sandbox", "--window-size=1920,1080");

        WebDriver driver = new ChromeDriver(options);

        try {
            MiCuentaPage cuenta = new MiCuentaPage(driver);

            cuenta.abrirLogin();
            cuenta.iniciarSesion(cedula, contrasenaActual);
            cuenta.abrirEdicionCuenta();
            cuenta.mostrarFormularioContrasena();

            cuenta.actualizar(contrasenaActual, contrasenaNueva, "confirmacion-distinta");
            cuenta.verificarMensaje("no coincide");

            cuenta.actualizar(contrasenaActual, contrasenaNueva, contrasenaNueva);
            cuenta.verificarRedireccion();
            cuenta.cerrarSesion();

            cuenta.abrirLogin();
            cuenta.iniciarSesion(cedula, contrasenaNueva);

            System.out.println("Contraseña actualizada e inicio de sesión verificado.");
            System.out.println("Nueva contraseña: " + contrasenaNueva);
        } finally {
            driver.quit();
        }
    }
}
