## UPM. ETSISI. POO

Matrícula   Nombre 
-bt0220     Sergio Gago
-bv0189     Ignacio de la Vega 
-bv0033     Jesús Martínez
-bv0209     Saúl de Antonio
-bu0119     Julián Rozadillas
```text
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

#### Productos

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

Estas clases estándar de la biblioteca Java se eligen por su simplicidad, eficiencia y claridad, adecuadas al propósito educativo del proyecto.

