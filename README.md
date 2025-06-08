## 🚀 Cómo correr la aplicación con Maven

### Requisitos previos

Asegúrate de tener instalado lo siguiente:

- Java (JDK 17 o superior)
- Apache Maven (versión 3.6 o superior)
- Un IDE o terminal de tu preferencia

---


### 🔧 Comandos para compilar y ejecutar

#### 1. Compilar el proyecto

```bash
mvn clean compile

mvn javafx:run 

mvn exec:java -D'exec.mainClass="com.ventas.db.DBInitializerV2' -- Ejecutar una clase particular
```
**NOTA** correr primero el archivo `DBInitializerV2.java` para ejecutar la base de datos. El script de la base de datos se encuentra en `src/main/resources/sql/SistemaVentaScript.sql`.
#### 2. Usuarios
```
user: admin 
pass: admin123
```
```
user: cajero
pass: cajero123
```

#### 3. Codigos de barra
```
1234567890123
43289423948
549845493
```