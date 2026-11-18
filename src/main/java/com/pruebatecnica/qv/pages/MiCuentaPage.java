package com.pruebatecnica.qv.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URI;
import java.time.Duration;

public class MiCuentaPage {
    private static final String LOGIN_URL = "https://www.bon-bonite.com/mi-cuenta/";
    private static final String EDICION_URL = "https://www.bon-bonite.com/mi-cuenta/edit-account/";
    private static final By USUARIO = By.id("username");
    private static final By CONTRASENA = By.id("password");
    private static final By BOTON_INGRESAR = By.cssSelector("button[name='login']");
    private static final By ACTUALIZAR_CONTRASENA = By.cssSelector(".update-password-btn");
    private static final By FORMULARIO_CONTRASENA = By.id("password-form");
    private static final By GUARDAR_CONTRASENA = By.cssSelector(".save-password-btn");
    private static final By CONTRASENA_ACTUAL = By.id("current_password");
    private static final By CONTRASENA_NUEVA = By.id("new_password");
    private static final By CONFIRMAR_CONTRASENA = By.id("confirm_password");
    private static final By MENSAJE_CONTRASENA = By.id("password-message");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public MiCuentaPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void abrirLogin() {
        driver.get(LOGIN_URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(USUARIO));
    }

    public void iniciarSesion(String cedula, String contrasena) {
        escribir(USUARIO, cedula);
        escribir(CONTRASENA, contrasena);
        wait.until(ExpectedConditions.elementToBeClickable(BOTON_INGRESAR)).click();
        wait.until(ExpectedConditions.urlContains("/mi-cuenta/orders"));
    }

    public void abrirEdicionCuenta() {
        driver.get(EDICION_URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(ACTUALIZAR_CONTRASENA));
    }

    public void mostrarFormularioContrasena() {
        wait.until(ExpectedConditions.elementToBeClickable(ACTUALIZAR_CONTRASENA)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(FORMULARIO_CONTRASENA));
    }

    public void actualizar(String actual, String nueva, String confirmacion) {
        escribir(CONTRASENA_ACTUAL, actual);
        escribir(CONTRASENA_NUEVA, nueva);
        escribir(CONFIRMAR_CONTRASENA, confirmacion);
        wait.until(ExpectedConditions.elementToBeClickable(GUARDAR_CONTRASENA)).click();
    }

    public void verificarMensaje(String textoEsperado) {
        WebElement mensaje = wait.until(ExpectedConditions.visibilityOfElementLocated(MENSAJE_CONTRASENA));
        if (!mensaje.getText().contains(textoEsperado)) {
            throw new IllegalStateException("Se esperaba el mensaje '" + textoEsperado + "', pero se mostró: " + mensaje.getText());
        }
    }

    public void verificarRedireccion() {
        wait.until(currentDriver -> {
            String ruta = URI.create(currentDriver.getCurrentUrl()).getPath();
            return "/mi-cuenta".equals(ruta) || "/mi-cuenta/".equals(ruta);
        });
    }

    public void cerrarSesion() {
        driver.manage().deleteAllCookies();
    }

    private void escribir(By localizador, String valor) {
        WebElement campo = wait.until(ExpectedConditions.visibilityOfElementLocated(localizador));
        campo.clear();
        campo.sendKeys(valor);
    }
}
