package com.pruebatecnica.qv.pages;

import com.pruebatecnica.qv.DatosCompra;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CheckoutPage {
    private static final By TIPO_DOCUMENTO = By.id("billing_tipo_documento");
    private static final By GENERO = By.id("billing_gender");
    private static final By TELEFONO = By.id("billing_phone");
    private static final By PAIS = By.id("billing_country");
    private static final By DEPARTAMENTO = By.id("billing_state");
    private static final By CIUDAD = By.id("billing_city");
    private static final By DIRECCION = By.id("billing_address_1");
    private static final By TERMINOS = By.id("terms");
    private static final By REGISTRAR_ORDEN = By.id("place_order");
    private static final By ERROR = By.cssSelector(".woocommerce-error");
    private static final Pattern NUMERO_PEDIDO = Pattern.compile("/order-(?:received|pay)/(\\d+)");

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.js = (JavascriptExecutor) driver;
    }

    public void completar(DatosCompra datos) {
        mostrarPasoFacturacion();
        wait.until(ExpectedConditions.visibilityOfElementLocated(TELEFONO));

        seleccionarPorTexto(TIPO_DOCUMENTO, "Cédula de Ciudadanía (CC)");
        seleccionarPorTexto(GENERO, "N/A");
        escribir(TELEFONO, datos.telefono());
        seleccionarPorTexto(PAIS, datos.pais());
        esperarOpcion(DEPARTAMENTO, datos.departamento());
        seleccionarPorTexto(DEPARTAMENTO, datos.departamento());
        esperarOpcion(CIUDAD, datos.ciudad());
        seleccionarPorTexto(CIUDAD, datos.ciudad());
        escribir(DIRECCION, datos.direccion());
        marcar(TERMINOS);
    }

    public String registrarOrden() {
        WebElement boton = wait.until(ExpectedConditions.presenceOfElementLocated(REGISTRAR_ORDEN));
        js.executeScript("arguments[0].click();", boton);
        wait.until(currentDriver -> esConfirmacion(currentDriver.getCurrentUrl()) || hayError(currentDriver));

        if (hayError(driver)) {
            throw new IllegalStateException("El sitio rechazó la orden: " + mensajeError());
        }

        String pedido = numeroPedido();
        if (pedido.isEmpty()) {
            throw new IllegalStateException("La orden se creó pero no se pudo obtener el número en " + driver.getCurrentUrl());
        }
        return pedido;
    }

    private void mostrarPasoFacturacion() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("step3")));
        js.executeScript(
                "document.querySelectorAll('.step').forEach(function(s){s.classList.remove('active','disabled');});" +
                        "var s=document.getElementById('step3');" +
                        "s.classList.add('active'); s.style.opacity='1'; s.style.pointerEvents='auto';");
    }

    private void escribir(By localizador, String valor) {
        WebElement campo = wait.until(ExpectedConditions.visibilityOfElementLocated(localizador));
        campo.clear();
        campo.sendKeys(valor);
    }

    private void marcar(By localizador) {
        WebElement casilla = wait.until(ExpectedConditions.presenceOfElementLocated(localizador));
        if (!casilla.isSelected()) {
            js.executeScript("arguments[0].click();", casilla);
        }
        if (!casilla.isSelected()) {
            throw new IllegalStateException("No se pudo marcar la casilla " + localizador);
        }
    }

    private void esperarOpcion(By localizador, String texto) {
        wait.until(currentDriver -> currentDriver.findElement(localizador).findElements(By.tagName("option")).stream()
                .anyMatch(opcion -> texto.equals(opcion.getText().trim())));
    }

    private void seleccionarPorTexto(By localizador, String texto) {
        WebElement select = wait.until(ExpectedConditions.presenceOfElementLocated(localizador));
        List<WebElement> opciones = select.findElements(By.tagName("option"));

        for (WebElement opcion : opciones) {
            if (texto.equals(opcion.getText().trim())) {
                js.executeScript(
                        "var s=arguments[0], v=arguments[1];" +
                                "if (window.jQuery && window.jQuery(s).data('select2')) {" +
                                "  window.jQuery(s).val(v).trigger('change');" +
                                "} else {" +
                                "  s.value=v; s.dispatchEvent(new Event('change', {bubbles:true}));" +
                                "}",
                        select, opcion.getAttribute("value"));
                return;
            }
        }
        throw new IllegalStateException("No se encontró la opción '" + texto + "' en " + localizador);
    }

    private boolean hayError(WebDriver currentDriver) {
        return currentDriver.findElements(ERROR).stream().anyMatch(WebElement::isDisplayed);
    }

    private String mensajeError() {
        return driver.findElements(ERROR).stream()
                .filter(WebElement::isDisplayed)
                .map(WebElement::getText)
                .collect(Collectors.joining(" | "));
    }

    private boolean esConfirmacion(String url) {
        return url.contains("/order-received/") || url.contains("/order-pay/");
    }

    private String numeroPedido() {
        Matcher matcher = NUMERO_PEDIDO.matcher(driver.getCurrentUrl());
        return matcher.find() ? matcher.group(1) : "";
    }
}
