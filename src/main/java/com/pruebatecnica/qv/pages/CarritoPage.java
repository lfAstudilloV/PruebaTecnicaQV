package com.pruebatecnica.qv.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CarritoPage {
    private static final String URL = "https://www.bon-bonite.com/carrito/";
    private static final By FILA_PRODUCTO = By.cssSelector(".woocommerce-cart-form__cart-item");
    private static final By IR_A_CHECKOUT = By.cssSelector("a.checkout-button");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public CarritoPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void abrir() {
        driver.get(URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(FILA_PRODUCTO));
    }

    public void verificarProducto(String nombre) {
        boolean encontrado = driver.findElements(FILA_PRODUCTO).stream()
                .anyMatch(fila -> fila.getText().contains(nombre));
        if (!encontrado) {
            throw new IllegalStateException("El producto '" + nombre + "' no está en el carrito.");
        }
    }

    public void irACheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(IR_A_CHECKOUT)).click();
        wait.until(ExpectedConditions.urlContains("finalizar-compra"));
    }
}
