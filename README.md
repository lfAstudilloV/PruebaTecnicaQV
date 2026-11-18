# Cómo ejecutar las pruebas

Estas instrucciones son para Windows. Chrome se inicia automáticamente cuando hace falta. La primera vez, el proyecto puede descargar los componentes que necesita.

## Antes de comenzar

1. Instala Google Chrome.
2. Instala Java 17 o una versión más reciente.
3. Descarga este proyecto y ábrelo en IntelliJ IDEA.
4. En IntelliJ, abre **View → Tool Windows → Terminal**. La terminal debe estar ubicada en la carpeta del proyecto.

Si la terminal muestra un mensaje indicando que no encuentra Java, instala Java y reinicia IntelliJ.

## Crear una cuenta nueva

Esta prueba crea una cuenta nueva en el sitio y muestra los datos de acceso al terminar. Ejecuta el siguiente comando:

```powershell
.\mvnw.cmd compile exec:java '-Dexec.mainClass=com.pruebatecnica.qv.RegistroAutomatizacion'
```

Cada ejecución crea otra cuenta. Úsala solo si tienes autorización para hacerlo.

## Cambiar la contraseña de una cuenta

Necesitas una cuenta de prueba y su contraseña actual. La prueba cambiará la contraseña y mostrará la nueva al terminar.

1. Copia estas instrucciones en la terminal y ejecútalas. Escribe la cédula cuando se solicite; la contraseña no se mostrará mientras la escribes.

```powershell
$env:BONBONITE_CEDULA = Read-Host 'Cédula de la cuenta de prueba'
$securePassword = Read-Host 'Contraseña actual' -AsSecureString
$env:BONBONITE_CONTRASENA = [System.Net.NetworkCredential]::new('', $securePassword).Password
```

2. Ejecuta la prueba:

```powershell
.\mvnw.cmd compile exec:java '-Dexec.mainClass=com.pruebatecnica.qv.ActualizacionContrasenaTest'
```

3. Guarda la nueva contraseña que aparece al finalizar. Después, borra los datos temporales de la terminal:

```powershell
Remove-Item Env:\BONBONITE_CEDULA, Env:\BONBONITE_CONTRASENA
```

## Probar una compra

Necesitas una cuenta de prueba y su contraseña actual. Esta prueba crea un pedido real del zapato disponible en talla 37. No ingreses datos de pago: si aparece una página de pago, el pedido queda pendiente.

Primero, sigue los pasos 1 y 2 de **Cambiar la contraseña de una cuenta** para introducir las credenciales. Después ejecuta:

```powershell
.\mvnw.cmd compile exec:java '-Dexec.mainClass=com.pruebatecnica.qv.CompraProductoTest'
```

Al finalizar, puedes borrar los datos temporales de la terminal con el comando `Remove-Item` del paso 3 anterior.

## Si una prueba muestra un error

Comprueba que Chrome esté instalado, que la cédula y contraseña sean correctas y que la terminal siga abierta en la carpeta del proyecto. Si ya cambiaste la contraseña, usa la contraseña más reciente.
