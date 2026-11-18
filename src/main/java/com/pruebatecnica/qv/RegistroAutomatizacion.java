package com.pruebatecnica.qv;

import com.pruebatecnica.qv.pages.RegistroPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class RegistroAutomatizacion {
    public static void main(String[] args) {
        DatosRegistro datos = DatosRegistro.aleatorios();
        System.out.println("Usuario generado: " + datos.correo() + " / " + datos.contrasena());

        WebDriver driver = new ChromeDriver();

        try {
            RegistroPage registroPage = new RegistroPage(driver);
            registroPage.abrir();
            registroPage.registrar(datos);
            System.out.println("Registro completado y pantalla de inicio de sesión verificada.");
        } finally {
            driver.quit();
        }
    }
}
