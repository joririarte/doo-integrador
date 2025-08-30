# Sistema de Ventas - Documentación y Arquitectura

## 📦 Arquitectura del Proyecto

Este proyecto está organizado como un **multi-módulo Maven** para separar la lógica de negocio, la API REST y la interfaz de escritorio. La estructura es la siguiente:

```
doo-integrador/
├── pom.xml                # Proyecto padre (multi-módulo)
├── core/                  # Módulo con lógica de negocio, modelos y DAOs
│   └── pom.xml
├── api/                   # Módulo API REST (Spring Boot)
│   └── pom.xml
└── desktop/               # Módulo interfaz gráfica (JavaFX)
    └── pom.xml
```

### Módulos

- **core**: Contiene las clases de modelo, DAOs y utilidades compartidas. No tiene método `main`.
- **api**: Expone la lógica de negocio como una API REST usando Spring Boot. Depende de `core`.
- **desktop**: Implementa la interfaz gráfica con JavaFX. Depende de `core`.

---

## ⚙️ Requisitos

- Java JDK 17 o superior
- Apache Maven 3.6 o superior

---

## 🛠️ Comandos para Compilar y Ejecutar

### 1. Compilar todos los módulos

Desde la raíz del proyecto:
```bash
mvn clean install
```

---

### 2. Ejecutar la API REST (Spring Boot)

Desde la raíz del proyecto:
```bash
mvn spring-boot:run -pl api
```
Esto inicia el servidor en el puerto configurado (por defecto, 8080).

---

### 3. Ejecutar la aplicación de escritorio (JavaFX)

Desde la raíz del proyecto:
```bash
mvn javafx:run -pl desktop
```
Esto inicia la interfaz gráfica.

---

### 4. Ejecutar una clase específica (por ejemplo, inicializar la base de datos)

Desde la raíz del proyecto:
```bash
mvn exec:java -pl core -Dexec.mainClass="com.ventas.db.DBInitializerV2"
```
> **Nota:** Ejecuta primero el archivo `DBInitializerV2.java` para crear la base de datos.  
> El script SQL se encuentra en `core/src/main/resources/sql/SistemaVentaScript.sql`.

---

## 🧩 Usuarios de prueba

```
user: admin 
pass: admin123

user: cajero
pass: cajero123
```

## 🏷️ Códigos de barra de ejemplo

```
1234567890123
43289423948
549845493
```

---

## 📚 Notas adicionales

- El módulo `core` no se ejecuta directamente, solo provee clases y lógica para los otros módulos.
- Si agregás nuevas entidades o DAOs, hacelo en `core`.
- Los módulos `api` y `desktop` deben tener solo la lógica específica de cada aplicación.

---

## 🏗️ ¿Cómo agregar nuevos módulos o dependencias?

1. Agregá el nuevo módulo en el `<modules>` del `pom.xml` principal.
2. Creá el directorio y el `pom.xml` correspondiente.
3. Si el módulo depende de `core`, agregá la dependencia en su `pom.xml`.

---

## 📝 Referencias

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [JavaFX Documentation](https://openjfx.io/)
- [Maven Multi-Module Projects](https://maven.apache.org/guides/introduction/introduction-to-the-pom.html#multi-module)

---

**¡Listo! Con esta estructura y comandos podés desarrollar, mantener y ejecutar cada parte del sistema de ventas de forma independiente y