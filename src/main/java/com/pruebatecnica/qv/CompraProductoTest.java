package com.pruebatecnica.qv;

import com.pruebatecnica.qv.pages.CarritoPage;
import com.pruebatecnica.qv.pages.CheckoutPage;
import com.pruebatecnica.qv.pages.MiCuentaPage;
import com.pruebatecnica.qv.pages.ProductoPage;
import com.pruebatecnica.qv.pages.ZapatosPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class CompraProductoTest {
    private static final String PRODUCTO = "Zapato en gamuza tabaco";
    private static final String TALLA = "37";

    public static void main(String[] args) {
        String cedula = ConfiguracionPrueba.variableRequerida("BONBONITE_CEDULA");
        String contrasena = args.length > 0
                ? args[0]
                : ConfiguracionPrueba.variableRequerida("BONBONITE_CONTRASENA");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--disable-gpu", "--no-sandbox", "--window-size=1920,1080");

        WebDriver driver = new ChromeDriver(options);

        try {
            MiCuentaPage cuenta = new MiCuentaPage(driver);
            cuenta.abrirLogin();
            cuenta.iniciarSesion(cedula, contrasena);

            ZapatosPage zapatos = new ZapatosPage(driver);
            zapatos.abrir();
            zapatos.abrirProducto();

            ProductoPage producto = new ProductoPage(driver);
            producto.seleccionarTalla(TALLA);
            producto.agregarAlCarrito();

            CarritoPage carrito = new CarritoPage(driver);
            carrito.abrir();
            carrito.verificarProducto(PRODUCTO);
            carrito.irACheckout();

            CheckoutPage checkout = new CheckoutPage(driver);
            checkout.completar(DatosCompra.prueba());
            String pedido = checkout.registrarOrden();

            System.out.println("Compra completada. Pedido: " + pedido);
        } finally {
            driver.quit();
        }
    }
}
