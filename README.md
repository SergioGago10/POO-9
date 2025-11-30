## UPM. ETSISI. POO

Matrícula   Nombre 
-bt0220     Sergio Gago
-bv0189     Ignacio de la Vega 
-bv0033     Jesús Martínez
-bv0209     Saúl de Antonio
-bu0119     Julián Rozadillas

## Entrega E1

El cliente solicita un **módulo de tickets** que permita:

- Crear y gestionar productos.  
- Aplicar descuentos por categoría.  
- Emitir una factura de un ticket ordenada alfabéticamente por nombre de producto.

---

### Requisitos de los productos

- Cada producto está definido por:
  - **ID**: número positivo que lo identifica dentro del sistema.  
  - **Nombre**: texto no vacío y de menos de **100 caracteres**.  
  - **Categoría**: una de las siguientes  
    `MERCH`, `PAPELERIA`, `ROPA`, `LIBRO`, `ELECTRÓNICA`.  
  - **Precio**: número mayor que 0, sin límite superior.

- Dos productos con diferente ID **no son iguales**.  
- No podrá haber **dos productos con el mismo ID** en el sistema.  
- No existirán más de **200 productos diferentes** en esta versión.

---

### Descuentos automáticos por categoría

Cuando se genera un ticket, los descuentos se aplican automáticamente cuando hay más de un producto de la misma categoría.

| Categoría     | Descuento |
|----------------|------------|
| MERCH          | 0% |
| PAPELERÍA      | 5% |
| ROPA           | 7% |
| LIBRO          | 10% |
| ELECTRÓNICA    | 3% |

---

### Funcionamiento de la aplicación

- La aplicación comienza con una **lista de productos vacía** y un **ticket vacío**.  
- Se pueden **insertar productos progresivamente**.  
- En cualquier momento se puede **reiniciar el proceso** e iniciar un **nuevo ticket**.  
- Al **imprimir el ticket**, se muestra por pantalla y se inicia uno nuevo.  
- Al **agregar, modificar o borrar** un producto, se imprime el **importe provisional del ticket**, aplicando los descuentos actuales.  
- Al **eliminar un producto**, se borran todas sus apariciones del ticket.  
- Se asume que **cada ticket no tendrá más de 100 productos**.

---

### Comandos disponibles

bash
prod add <id> "<nombre>" <categoria> <precio>      → agrega un producto con nuevo ID  
prod list                                          → lista los productos actuales  
prod update <id> campo valor                       → actualiza (campos: nombre | categoria | precio)  
prod remove <id>                                   → elimina un producto por ID  

ticket new                                         → reinicia el ticket actual  
ticket add <prodId> <cantidad>                     → agrega una cantidad de producto al ticket  
ticket remove <prodId>                             → elimina todas las apariciones del producto  
ticket print                                       → imprime la factura del ticket  

help                                               → muestra los comandos disponibles  
echo "<texto>"                                     → imprime el texto indicado  
exit                                               → cierra la aplicación

---

## Justificación del diseño UML

El proyecto se ha organizado por las siguientes capas:

- **Modelos** (`Product`, `Ticket`): Solo contienen los datos del dominio.  
- **Repositorio** (`Catalog`): Gestiona el almacenamiento y las operaciones CRUD sobre los productos.  
- **Servicio** (`App`): Coordina los comandos principales de la aplicación.  
- **Utilities**: Agrupa métodos de apoyo generales.

Esta separación facilita la comprensión del código, la reutilización y las pruebas.

---

### Uso de librerías Java

- **ArrayList**: se utiliza en `Ticket` para almacenar los productos, ya que permite crecer dinámicamente y recorrerlos fácilmente para imprimir o calcular descuentos.  
- **Map (HashMap)**: se usa en `Ticket` para comprobar si una categoría aparece más de una vez, simplificando el cálculo de descuentos.  
- **Arrays / Collections**: se emplean en `Catalog` para manejar el listado de productos de forma eficiente.  
- **Scanner**: se usa en `App` para leer los comandos del usuario por consola.
## Entrega E2

En la segunda entrega se amplía el módulo de tickets incorporando:

- Gestión de **usuarios** (clientes y cajeros).
- **Nuevos tipos de productos** (comidas en campus y reuniones).
- **Productos personalizables** con recargos por texto.
- Gestión avanzada de **tickets** con identificadores, estados y permisos por cajero.

Todas las funcionalidades de la Entrega E1 se mantienen y se extienden en esta versión.

---

### Usuarios: Clientes y Cajeros

#### Clientes

- Se dan de alta con:
    - **Nombre**
    - **DNI** (identificador único en el sistema)
    - **Correo electrónico**
    - **Cajero** que lo da de alta (`cashId`)
- Un cliente conoce los **tickets** que tiene asociados.

#### Cajeros

- Se dan de alta con:
    - **Nombre**
    - **Correo electrónico corporativo**
- Identificador: código formado por `UW` + **7 dígitos** (puede pasarse como parámetro o generarse automáticamente).
- Un cajero guarda la **lista de tickets** que ha creado.
- Si se borra un cajero, se borran también **todos los tickets creados por él**.
- Un cajero no puede ser cliente simultáneamente; si desea ser cliente, debe registrarse con su correo personal como un usuario nuevo.

---

### Ampliación de productos

Se mantienen todos los requisitos de producto de E1 (ID único, nombre, categoría, precio y máximo de 200 productos) y se incorporan nuevas variantes:
Los **descuentos por categoría** de la Entrega E1 (MERCH, PAPELERÍA, ROPA, LIBRO, ELECTRÓNICA) siguen aplicándose automáticamente cuando hay más de un producto de la misma categoría.

#### Productos personalizables

- Extienden a los productos básicos.
- Añaden:
    - Número máximo de **textos personalizables** por producto.
    - Lista de textos aplicados a cada compra.
- El precio final se calcula a partir del precio base añadiendo un **recargo del 10 % por cada texto personalizado**.
- No todos los productos básicos son personalizables.
- Un producto básico **no puede convertirse a personalizable en el futuro**: su naturaleza queda fijada al crearse.

#### Comidas en campus

- Productos sin categoría, pensados para eventos de comida.
- Atributos:
    - **Fecha de caducidad**.
    - **Número máximo de personas** (hasta 100).
    - **Precio por persona**.
- Deben crearse con al menos **3 días de antelación** respecto a la fecha actual.
- En un ticket no puede añadirse **dos veces la misma comida**.

#### Reuniones

- Estructura similar a las comidas:
    - Fecha de caducidad.
    - Número máximo de personas.
    - Precio por persona.
- Requieren una **anticipación mínima de 12 horas**.
- En un ticket no puede añadirse **dos veces la misma reunión**.

---

### Gestión de tickets

Cada ticket tiene ahora:

- **Identificador único** con el formato:
    - `YY-MM-dd-HH:mm-<número_aleatorio_5_dígitos>` en la apertura.
    - Al cerrarse se añade la fecha de cierre:  
      `...-YY-MM-dd-HH:mm`.
- **Estado**:
    - `VACIO`
    - `ACTIVO`
    - `CERRADO`
- Está asociado a:
    - Un **cliente**.
    - Un **cajero** que lo abre.

Reglas de funcionamiento:

- Solo el **cajero que abrió el ticket** puede ejecutar sobre él las operaciones `add`, `remove`, `print` y `close`.
- Imprimir un ticket (`ticket print`) implica **cerrarlo** (emisión de la factura).
- Un ticket **cerrado** puede volver a imprimirse, pero **no puede modificarse ni reabrirse**.

En los productos de tipo comida y reunión:

- El campo `amount` del comando se interpreta como **número de personas**.
- No se permite añadir el mismo producto de comida/reunión más de una vez por ticket.

---
## COMANDOS

CLIENTES Y CAJEROS

client add "<nombre>" <DNI> <email> <cashId> → da de alta un cliente  
client remove <DNI> → elimina un cliente por DNI  
client list → lista los clientes ordenados por nombre 

cash add [<id>] "<nombre>" <email> → da de alta un cajero  
cash remove <id> → elimina un cajero por ID  
cash list → lista los cajeros ordenados por nombre  
cash tickets <id> → muestra los tickets de un cajero  

TICKETS

ticket new [<id>] <cashId> <userId> → crea un nuevo ticket  
ticket add <ticketId> <cashId> <prodId> <amount> → añade un producto al ticket  
ticket remove <ticketId> <cashId> <prodId> → elimina un producto del ticket  
ticket print <ticketId> <cashId> → imprime y cierra el ticket  
ticket list → lista todos los tickets  

PRODUCTOS

prod add [<id>] "<nombre>" <categoria> <precio> [<maxPers>] → añade un producto (personalizable si se indica maxPers)  
prod addFood [<id>] "<nombre>" <precio> <fecha> <max_people> → añade una comida en campus  
prod addMeeting [<id>] "<nombre>" <precio> <fecha> <max_people> → añade una reunión  
prod update <id> campo valor → actualiza (campos: nombre | categoria | precio)  
prod list → lista los productos actuales  
prod remove <id> → elimina un producto por ID  

GENERALES

help → muestra los comandos disponibles  
echo "<texto>" → imprime el texto indicado  
exit → cierra la aplicación  

---

### Uso de librerías Java (Entrega E2)

- **ArrayList**: se utiliza para almacenar y gestionar dinámicamente listas de productos, clientes, cajeros y tickets, permitiendo insertar, recorrer y modificar elementos de forma sencilla.
- **Map (HashMap)**: se emplea para acceder rápidamente a productos, clientes, cajeros y tickets por su identificador, y para contar categorías o agrupar información de forma eficiente.
- **Collections**: se usa para ordenar listas de productos, clientes, cajeros y tickets según los criterios requeridos (por nombre, por identificador de cajero, por id de ticket, etc.).
- **Scanner**: se utiliza para leer comandos de entrada tanto desde la consola en modo interactivo como desde un archivo de texto en modo batch.
- **LocalDate / LocalDateTime**: se emplean para gestionar fechas y horas de creación y cierre de tickets, fechas de caducidad de productos de comida/reunión y comprobación de las restricciones temporales (3 días y 12 horas).

Estas clases estándar de la biblioteca Java se eligen por su simplicidad, eficiencia y claridad, adecuadas al propósito educativo del proyecto.


