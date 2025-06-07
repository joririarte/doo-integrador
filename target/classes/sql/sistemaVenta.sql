-- Tabla Persona (base abstracta para Cliente y Empleado)
CREATE TABLE Persona (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombreApellido TEXT,
    tipoDocumento TEXT,
    nroDocumento TEXT,
    CUIT TEXT,
    condicionAfip TEXT,
    genero TEXT,
    fechaNacimiento TEXT,
    domicilio TEXT,
    email TEXT
);

-- Tabla Cliente
CREATE TABLE Cliente (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nro_cliente TEXT,
    persona_id INTEGER,
    FOREIGN KEY (persona_id) REFERENCES Persona(id)
);

-- Tabla Empleado
CREATE TABLE Empleado (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    cargo TEXT,
    persona_id INTEGER,
    FOREIGN KEY (persona_id) REFERENCES Persona(id)
);

-- Tabla Usuario
CREATE TABLE Usuario (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT,
    password TEXT,
    ultimoAcceso TEXT,
    empleado_id INTEGER,
    FOREIGN KEY (empleado_id) REFERENCES Empleado(id)
);

-- Tabla MedioPago
CREATE TABLE MedioPago (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT,
    habilitado BOOLEAN,
    fechaHabilitadoDesde DATE,
    fechaHabilitadoHasta DATE
);

-- Tabla DescuentoRecargo
CREATE TABLE DescuentoRecargo (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT,
    tipo TEXT,
    monto REAL,
    fechaInicio DATE,
    fechaFin DATE,
    habilitado BOOLEAN,
    medio_pago_id INTEGER,
    FOREIGN KEY (medio_pago_id) REFERENCES MedioPago(id)
);

-- Tabla Producto
CREATE TABLE Producto (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT,
    Marca TEXT,
    codigoBarras TEXT
);

-- Tabla Precio
CREATE TABLE Precio (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    monto REAL,
    fecha DATE,
    producto_id INTEGER,
    FOREIGN KEY (producto_id) REFERENCES Producto(id)
);

-- Tabla Stock
CREATE TABLE Stock (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    cantidad REAL,
    fecha DATE,
    producto_id INTEGER,
    FOREIGN KEY (producto_id) REFERENCES Producto(id)
);

-- Tabla Venta
CREATE TABLE Venta (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    vendedor_id INTEGER,
    fecha DATE,
    estado TEXT,
    montoPagado REAL,
    medio_pago_id INTEGER,
    cliente_id INTEGER,
    FOREIGN KEY (vendedor_id) REFERENCES Empleado(id),
    FOREIGN KEY (medio_pago_id) REFERENCES MedioPago(id),
    FOREIGN KEY (cliente_id) REFERENCES Cliente(id)
);

-- Tabla DetalleVenta
CREATE TABLE DetalleVenta (
    venta_id INTEGER,
    id INTEGER,
    nombre TEXT,
    cantidad INTEGER,
    precioVenta REAL,
    producto_id INTEGER,
    PRIMARY KEY (venta_id, id),
    FOREIGN KEY (producto_id) REFERENCES Producto(id),
    FOREIGN KEY (venta_id) REFERENCES Venta(id)
);

-- Datos de prueba
INSERT INTO Persona (nombreApellido, tipoDocumento, nroDocumento, CUIT, condicionAfip, genero, fechaNacimiento, domicilio, email)
VALUES ('Juan Pérez', 'DNI', '12345678', '20-12345678-9', 'Responsable Inscripto', 'M', '1985-06-01', 'Calle Falsa 123', 'juan@example.com');

INSERT INTO Cliente (nro_cliente, persona_id) VALUES ('C001', 1);
INSERT INTO Empleado (cargo, persona_id) VALUES ('Cajero', 1);
INSERT INTO Usuario (username, password, empleado_id) VALUES ('jperez', '1234', 1);
INSERT INTO MedioPago (nombre, habilitado, fechaHabilitadoDesde, fechaHabilitadoHasta)
VALUES ('Efectivo', 1, '2024-01-01', '2025-12-31');
INSERT INTO DescuentoRecargo (nombre, tipo, monto, fechaInicio, fechaFin, habilitado, medio_pago_id)
VALUES ('Descuento 10%', 'Descuento', 10.0, '2024-01-01', '2024-12-31', 1, 1);
INSERT INTO Producto (nombre, Marca, codigoBarras)
VALUES ('Coca-Cola 500ml', 'Coca-Cola', '7790895001020');
INSERT INTO Precio (monto, fecha, producto_id)
VALUES (250.00, '2024-06-01', 1);
INSERT INTO Stock (cantidad, fecha, producto_id)
VALUES (100, '2024-06-01', 1);
INSERT INTO Venta (vendedor_id, fecha, estado, montoPagado, medio_pago_id, cliente_id)
VALUES (1, '2025-06-01', 'Finalizada', 250.00, 1, 1);
INSERT INTO DetalleVenta (nombre, cantidad, descuentoRecargo, precioVenta, producto_id, venta_id)
VALUES ('Coca-Cola 500ml', 1, 0.0, 250.00, 1, 1);
