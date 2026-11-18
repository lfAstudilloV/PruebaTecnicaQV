## Historia de usuario: actualización de contraseña

**Actor:** usuario registrado y autenticado

**Acción:** Actualizar la contraseña de su cuenta desde la sección de detalles de la cuenta.

**Resultado esperado:**
- La plataforma guarda la nueva contraseña cuando la contraseña actual es válida y la nueva contraseña cumple los requisitos del sitio.
- La plataforma confirma la actualización y permite iniciar sesión con la nueva contraseña.
- Si los datos no son válidos, la plataforma informa el error y no modifica la contraseña.

**Datos de prueba:**
- Iniciar sesión con una cuenta de prueba autorizada.
- Ingresar la contraseña actual.
- Ingresar la nueva contraseña y su confirmación.
- Verificar que la confirmación coincida y que la nueva contraseña cumpla los requisitos definidos por el sitio.
- Los nombres exactos de los campos, las reglas de contraseña y el botón para guardar quedan pendientes de inspección autenticada.

**URL de inicio:** https://www.bon-bonite.com/mi-cuenta/edit-account/

## Exploración autenticada

La exploración se hizo con una cuenta de prueba autorizada, sin levantar Selenium ni enviar cambios de contraseña.

- Login: `https://www.bon-bonite.com/mi-cuenta/`
  - Cédula: `#username`
  - Contraseña: `#password`
  - Botón: `button[name='login']`
  - Éxito: redirección a `/mi-cuenta/orders/`.
- Edición: `https://www.bon-bonite.com/mi-cuenta/edit-account/`
  - Botón que abre el formulario: `.update-password-btn`.
  - Formulario oculto: `#password-form`.
  - Campos: `#current_password`, `#new_password`, `#confirm_password`.
  - Botón guardar: `.save-password-btn`.
  - Mensaje: `#password-message` / `#password-message-text`.
  - El menú de cuenta está oculto hasta hacer hover; para simplificar, el test cierra la sesión borrando las cookies.
- Reglas observadas en el JavaScript de la página:
  - La confirmación debe coincidir; si no, muestra `Las contraseña nueva no coincide`.
  - La nueva debe ser diferente de la actual; si no, muestra `La nueva contraseña debe ser diferente a la actual`.
  - El sitio usa el AJAX `action=update_user_password` con `current_password` y `new_password`.
  - El medidor de fuerza exige nivel 3; la sugerencia indica al menos 12 caracteres con mayúsculas, minúsculas, números y símbolos.
  - Tras guardar correctamente redirige a `/mi-cuenta`.

## Escenario automatizado

Implementado en `ActualizacionContrasenaTest.java` con el Page Object `MiCuentaPage.java`.

1. Iniciar sesión con la cédula y contraseña de prueba.
2. Abrir la edición de cuenta y mostrar el formulario de contraseña.
3. Enviar una confirmación distinta y verificar el mensaje de error; como no se llama al AJAX, la contraseña original sigue vigente.
4. Enviar la contraseña actual, una nueva contraseña fuerte generada aleatoriamente y su confirmación.
5. Verificar la redirección a `/mi-cuenta`.
6. Borrar las cookies de sesión y volver a iniciar con la nueva contraseña.

Nota: configura `BONBONITE_CEDULA` y `BONBONITE_CONTRASENA` en el entorno local antes de ejecutar el escenario. También puedes pasar la contraseña vigente como primer argumento. No guardes credenciales en el código ni en este documento.
