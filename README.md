# Automatización de registro Bon-Bonite

La automatización usa Selenium con Chrome y el patrón Page Object Model. Cada ejecución genera automáticamente un usuario aleatorio: cédula numérica, nombres, apellidos, correo único y contraseña fuerte; no hay que configurar variables de entorno ni ingresar datos manualmente.

Desde PowerShell, en la raíz del proyecto, ejecuta:

```powershell
.\mvnw.cmd compile exec:java '-Dexec.mainClass=com.pruebatecnica.qv.RegistroAutomatizacion'
```

Se requiere Java 17 o superior, disponible en `JAVA_HOME` o en `PATH`, y Google Chrome; el Maven Wrapper descarga Maven automáticamente la primera vez y Selenium Manager resuelve el controlador de Chrome. La ejecución crea una cuenta real, acepta las dos casillas indicadas en la HU, verifica la redirección al inicio de sesión e imprime las credenciales generadas. Úsala solo en entornos donde estés autorizado a crear cuentas de prueba.

## Actualización de contraseña

El escenario de la HU está en `ActualizacionContrasenaTest.java` y usa el Page Object `MiCuentaPage.java`. Se ejecuta en Chrome headless con viewport de escritorio:

```powershell
.\mvnw.cmd compile exec:java '-Dexec.mainClass=com.pruebatecnica.qv.ActualizacionContrasenaTest'
```

Antes de ejecutar, configura localmente la cédula y contraseña vigentes de una cuenta de prueba autorizada:

```powershell
$env:BONBONITE_CEDULA = Read-Host 'Cédula de la cuenta de prueba'
$securePassword = Read-Host 'Contraseña vigente' -AsSecureString
$env:BONBONITE_CONTRASENA = [System.Net.NetworkCredential]::new('', $securePassword).Password
.\mvnw.cmd compile exec:java '-Dexec.mainClass=com.pruebatecnica.qv.ActualizacionContrasenaTest'
Remove-Item Env:\BONBONITE_CEDULA, Env:\BONBONITE_CONTRASENA
```

La contraseña también se puede pasar como primer argumento (`-Dexec.args=...`) si ya cambió en una ejecución anterior; se recomienda la variable de entorno para no incluirla en la línea de comandos.

**Importante:** esta prueba cambia la contraseña real de la cuenta de prueba e imprime la nueva al finalizar.

## Compra de producto

El escenario está en `CompraProductoTest.java` y usa los Page Objects `ZapatosPage`, `ProductoPage`, `CarritoPage` y `CheckoutPage`. También se ejecuta en Chrome headless:

```powershell
.\mvnw.cmd compile exec:java '-Dexec.mainClass=com.pruebatecnica.qv.CompraProductoTest'
```

Compra `Zapato en gamuza tabaco`, talla `37`, con las credenciales locales de `BONBONITE_CEDULA` y `BONBONITE_CONTRASENA`. Puedes pasar la contraseña vigente como argumento:

```powershell
.\mvnw.cmd compile exec:java '-Dexec.mainClass=com.pruebatecnica.qv.CompraProductoTest' '-Dexec.args=CONTRASENA_ACTUAL'
```

La prueba termina cuando el pedido queda creado e imprime su número. Si el sitio selecciona Wompi, la confirmación es una página de pago pendiente; no se completan datos bancarios ni se realiza el pago.
