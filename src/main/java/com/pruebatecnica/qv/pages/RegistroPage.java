package com.pruebatecnica.qv.pages;

import com.pruebatecnica.qv.DatosRegistro;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URI;
import java.time.Duration;
import java.util.List;

public class RegistroPage {
    private static final String URL = "https://www.bon-bonite.com/mi-cuenta/";
    private static final By MOSTRAR_REGISTRO = By.id("show_register");
    private static final By FORMULARIO_REGISTRO = By.id("form-register");
    private static final By CEDULA = By.id("reg_username");
    private static final By NOMBRES = By.id("first_name");
    private static final By APELLIDOS = By.id("last_name");
    private static final By CORREO = By.id("reg_email");
    private static final By CONTRASENA = By.id("reg_password");
    private static final By CONFIRMAR_CONTRASENA = By.id("reg_password2");
    private static final By NOVEDADES = By.id("newsletter_authorization");
    private static final By TRATAMIENTO_DATOS = By.id("privacy_policy_reg");
    private static final By BOTON_REGISTRO = By.cssSelector("#form-register button[name='register']");
    private static final By MENSAJE_EXITO = By.cssSelector(".woocommerce-message");
    private static final By MENSAJE_ERROR = By.cssSelector(".woocommerce-error");
    private static final By USUARIO_LOGIN = By.id("username");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public RegistroPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void abrir() {
        driver.get(URL);
        wait.until(ExpectedConditions.elementToBeClickable(MOSTRAR_REGISTRO)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(FORMULARIO_REGISTRO));
    }

    public void registrar(DatosRegistro datos) {
        escribir(CEDULA, datos.cedula());
        escribir(NOMBRES, datos.nombres());
        escribir(APELLIDOS, datos.apellidos());
        escribir(CORREO, datos.correo());
        escribir(CONTRASENA, datos.contrasena());
        escribir(CONFIRMAR_CONTRASENA, datos.confirmarContrasena());
        marcar(NOVEDADES);
        marcar(TRATAMIENTO_DATOS);
        wait.until(ExpectedConditions.elementToBeClickable(BOTON_REGISTRO)).click();

        esperarRespuesta();
        verificarRedireccionAlLogin();
    }

    private void escribir(By localizador, String valor) {
        WebElement campo = wait.until(ExpectedConditions.visibilityOfElementLocated(localizador));
        campo.clear();
        campo.sendKeys(valor);
    }

    private void marcar(By localizador) {
        WebElement casilla = wait.until(ExpectedConditions.elementToBeClickable(localizador));
        if (!casilla.isSelected()) {
            casilla.click();
        }
    }

    private void esperarRespuesta() {
        wait.until(currentDriver ->
                estaVisible(currentDriver, MENSAJE_ERROR) || estaVisible(currentDriver, MENSAJE_EXITO));

        if (estaVisible(driver, MENSAJE_ERROR)) {
            String detalle = driver.findElement(MENSAJE_ERROR).getText();
            throw new IllegalStateException("El sitio rechazó el registro: " + detalle);
        }

        String mensaje = driver.findElement(MENSAJE_EXITO).getText();
        if (mensaje.isBlank()) {
            throw new IllegalStateException("El sitio mostró una respuesta de registro vacía.");
        }
    }

    private boolean estaVisible(WebDriver currentDriver, By localizador) {
        List<WebElement> elementos = currentDriver.findElements(localizador);
        return !elementos.isEmpty() && elementos.get(0).isDisplayed();
    }

    private void verificarRedireccionAlLogin() {
        String urlActual = driver.getCurrentUrl();
        if (urlActual == null || !"/mi-cuenta/".equals(URI.create(urlActual).getPath())) {
            throw new IllegalStateException("El registro no terminó en la pantalla de inicio de sesión.");
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(USUARIO_LOGIN));
    }
}
