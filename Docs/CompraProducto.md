## Historia de usuario: compra de producto

**Actor:** usuario registrado y autenticado.

**Acción:** Comprar un producto de la categoría Zapatos.

**Resultado esperado:**
- El producto queda en el carrito con la talla seleccionada.
- El checkout acepta los datos de facturación y termina en una confirmación de pedido.

## Exploración

La exploración se hizo sin levantar navegador, usando la sesión autenticada de la cuenta de prueba.

- Categoría: `https://www.bon-bonite.com/categoria-producto/zapatos-mujer/`
  - Producto elegido: `Zapato en gamuza tabaco`.
  - Enlace: `a[href*='/producto/zapato-en-gamuza-tabaco/']`.
- Producto: `https://www.bon-bonite.com/producto/zapato-en-gamuza-tabaco/`
  - Tallas: botones `button.variation-button[data-value='...']`.
  - Talla disponible: `37`; las demás estaban marcadas como agotadas.
  - Botón: `.single_add_to_cart_button`.
  - Confirmación: contador `.cart-contents-count` deja de estar en `0`.
- Carrito: `https://www.bon-bonite.com/carrito/`
  - Fila: `.woocommerce-cart-form__cart-item`.
  - Botón: `a.checkout-button`.
- Checkout: `https://www.bon-bonite.com/finalizar-compra/`
  - Campos usados: `#billing_tipo_documento`, `#billing_gender`, `#billing_phone`, `#billing_country`, `#billing_state`, `#billing_city`, `#billing_address_1`, `#terms`.
  - Botón: `#place_order`.
  - Error: `.woocommerce-error`.
  - El checkout es un asistente de 3 pasos; el formulario de facturación está en `#step3`.
  - En headless, `#step3` queda con `opacity: 0`, así que el Page Object lo activa explícitamente.
  - Éxito: redirección a `/order-pay/` u `/order-received/`, con el número de pedido en la URL.
  - Si queda una pasarela externa (Wompi), el pedido queda creado y pendiente de pago.

## Escenario automatizado

Implementado en `CompraProductoTest.java` con los Page Objects:

- `ZapatosPage`
- `ProductoPage`
- `CarritoPage`
- `CheckoutPage`

Pasos:

1. Iniciar sesión con la cuenta de prueba.
2. Abrir Zapatos y elegir `Zapato en gamuza tabaco`.
3. Seleccionar talla `37` y añadir al carrito.
4. Verificar el producto en el carrito e ir al checkout.
5. Completar los datos de facturación, autorizar el tratamiento de datos y registrar la orden.
6. Verificar la confirmación y mostrar el número de pedido.

El escenario se ejecuta en Chrome headless. La comprobación termina cuando el pedido existe; si el sitio elige Wompi, la página final es de pago pendiente y el test reporta el número de orden creado.

Nota: usa la contraseña vigente de la cuenta de prueba. Puedes pasarla como primer argumento.
