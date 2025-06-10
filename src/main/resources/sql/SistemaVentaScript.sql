DROP TABLE IF EXISTS DetalleVenta;
DROP TABLE IF EXISTS Venta;
DROP TABLE IF EXISTS Stock;
DROP TABLE IF EXISTS Precio;
DROP TABLE IF EXISTS Producto;
DROP TABLE IF EXISTS DescuentoRecargo;
DROP TABLE IF EXISTS MedioPago;
DROP TABLE IF EXISTS Usuario;
DROP TABLE IF EXISTS Empleado;
DROP TABLE IF EXISTS Cliente;
DROP TABLE IF EXISTS Persona;

-- Tabla Persona (base abstracta para Cliente y Empleado)
CREATE TABLE IF NOT EXISTS Persona (
    personaId INTEGER PRIMARY KEY AUTOINCREMENT,
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
CREATE TABLE IF NOT EXISTS Cliente (
    personaId INTEGER PRIMARY KEY,
    nroCliente TEXT,
    FOREIGN KEY (personaId) REFERENCES Persona(personaId)
);

-- Tabla Empleado
CREATE TABLE IF NOT EXISTS Empleado (
    personaId INTEGER PRIMARY KEY,
    cargo TEXT,
    FOREIGN KEY (personaId) REFERENCES Persona(personaId)
);

-- Tabla Usuario
CREATE TABLE IF NOT EXISTS Usuario (
    empleadoId INTEGER PRIMARY KEY,
    username TEXT UNIQUE,
    password TEXT,
    ultimoAcceso TEXT,
    FOREIGN KEY (empleadoId) REFERENCES Empleado(empleadoId)
);

-- Tabla MedioPago
CREATE TABLE IF NOT EXISTS MedioPago (
    medioPagoId INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT,
    habilitado BOOLEAN,
    fechaHabilitadoDesde TEXT,
    fechaHabilitadoHasta TEXT
);

-- Tabla DescuentoRecargo
CREATE TABLE IF NOT EXISTS DescuentoRecargo (
    medioPagoId INTEGER NOT NULL,
    descuentoRecargoId INTEGER NOT NULL,
    nombre TEXT,
    tipo TEXT,
    monto REAL,
    fechaInicio TEXT,
    fechaFin TEXT,
    habilitado BOOLEAN,
    PRIMARY KEY (medioPagoId, descuentoRecargoId),
    FOREIGN KEY (medioPagoId) REFERENCES MedioPago(medioPagoId)
);

-- Tabla Producto
CREATE TABLE IF NOT EXISTS Producto (
    productoId INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT,
    marca TEXT,
    codigoBarras TEXT
);

-- Tabla Precio
CREATE TABLE IF NOT EXISTS Precio (
    productoId INTEGER NOT NULL,
    precioId INTEGER NOT NULL,
    monto REAL,
    fecha TEXT,
    PRIMARY KEY (productoId, precioId),
    FOREIGN KEY (productoId) REFERENCES Producto(productoId)
);

-- Tabla Stock
CREATE TABLE IF NOT EXISTS Stock (
    productoId INTEGER NOT NULL,
    stockId INTEGER NOT NULL,
    cantidad REAL,
    fecha TEXT,
    PRIMARY KEY (productoId, stockId),
    FOREIGN KEY (productoId) REFERENCES Producto(productoId)
);

-- Tabla Venta
CREATE TABLE IF NOT EXISTS Venta (
    ventaId INTEGER PRIMARY KEY AUTOINCREMENT,
    codigoVenta TEXT UNIQUE NOT NULL, 
    vendedorId INTEGER,
    fecha TEXT,
    estado TEXT,
    montoPagado REAL,
    medioPagoId INTEGER,
    clienteId INTEGER,
    FOREIGN KEY (vendedorId) REFERENCES Empleado(empleadoId),
    FOREIGN KEY (medioPagoId) REFERENCES MedioPago(medioPagoId),
    FOREIGN KEY (clienteId) REFERENCES Cliente(clienteId)
);

-- Tabla DetalleVenta
CREATE TABLE IF NOT EXISTS DetalleVenta (
    ventaId INTEGER,
    detalleVentaId INTEGER,
    nombre TEXT,
    cantidad INTEGER,
    precioVenta REAL,
    productoId INTEGER NOT NULL,
    PRIMARY KEY (ventaId, detalleVentaId),
    FOREIGN KEY (productoId) REFERENCES Producto(productoId),
    FOREIGN KEY (ventaId) REFERENCES Venta(ventaId)
);

-- Datos de prueba

-- Clientes (Persona)
INSERT INTO Persona (nombreApellido, tipoDocumento, nroDocumento, CUIT, condicionAfip, genero, fechaNacimiento, domicilio, email)
VALUES 
('Juan Perez', 'DNI', '30123456', '20-30123456-3', 'Consumidor Final', 'M', '1985-06-15', 'Calle Falsa 123', 'juan@example.com'),
('Ana Garcia', 'DNI', '27876543', '27-27876543-9', 'Monotributista', 'F', '1990-04-22', 'Av. Siempre Viva 742', 'ana@example.com');

-- Empleados (Persona)
INSERT INTO Persona (nombreApellido, tipoDocumento, nroDocumento, CUIT, condicionAfip, genero, fechaNacimiento, domicilio, email)
VALUES 
('Carlos Lopez', 'DNI', '25678901', '23-25678901-1', 'Responsable Inscripto', 'M', '1980-03-10', 'Mitre 1000', 'carlos@example.com'),
('Lucia Martinez', 'DNI', '29876543', '27-29876543-5', 'Monotributista', 'F', '1995-11-05', 'Belgrano 500', 'lucia@example.com');

-- Asumiendo personaId = 1 (Juan) y 2 (Ana)
INSERT INTO Cliente (personaId, nroCliente)
VALUES 
(1, 'CL001'),
(2, 'CL002');

-- Asumiendo personaId = 3 (Carlos) y 4 (Lucía)
INSERT INTO Empleado (personaId, cargo)
VALUES 
(3, 'Administrador'),
(4, 'Cajero');

-- Usamos mismo personaId como empleadoId (3 y 4)
INSERT INTO Usuario (empleadoId, username, password, ultimoAcceso)
VALUES 
(3, 'admin', 'admin123', '2025-06-08 10:00:00'),
(4, 'cajero', 'cajero123', '2025-06-08 09:00:00');

INSERT INTO MedioPago (nombre, habilitado, fechaHabilitadoDesde, fechaHabilitadoHasta)
VALUES 
('Efectivo', 1, '2024-01-01', '2030-12-31'),
('Tarjeta de Credito', 1, '2024-01-01', '2030-12-31');

INSERT INTO DescuentoRecargo (medioPagoId, descuentoRecargoId, nombre, tipo, monto, fechaInicio, fechaFin, habilitado)
VALUES 
(1, 1, 'Descuento 10% efectivo', 'Descuento', 10.0, '2024-01-01', '2030-12-31', 1),
(2, 1, 'Recargo 5% tarjeta', 'Recargo', 5.0, '2024-01-01', '2030-12-31', 1);

INSERT INTO Producto (nombre, marca, codigoBarras)
VALUES 
('Leche Entera', 'La Serenisima', '7791234567890'),
('Yerba Mate', 'Natura', '7790987654321'),
('Galletitas Dulces', 'Bagley', '7791122334455');

-- Producto 1
INSERT INTO Precio (productoId, precioId, monto, fecha)
VALUES 
(1, 1, 1200.0, '2025-06-01');

-- Producto 2
INSERT INTO Precio (productoId, precioId, monto, fecha)
VALUES 
(2, 1, 1800.0, '2025-06-01');

-- Producto 3
INSERT INTO Precio (productoId, precioId, monto, fecha)
VALUES 
(3, 1, 900.0, '2025-06-01');

INSERT INTO Stock (productoId, stockId, cantidad, fecha)
VALUES 
(1, 1, 50, '2025-06-01'),
(2, 1, 30, '2025-06-01'),
(3, 1, 80, '2025-06-01');

-- Venta realizada por Lucía (empleadoId = 4) al cliente Juan (clienteId = 1) con efectivo (medioPagoId = 1)
INSERT INTO Venta (vendedorId, codigoVenta, fecha, estado, montoPagado, medioPagoId, clienteId)
VALUES 
(4,'C-001', '2025-06-08 11:00:00', 'Confirmada', 3000.0, 1, 1);

-- Supongamos ventaId autogenerado es 1

INSERT INTO DetalleVenta (ventaId, detalleVentaId, nombre, cantidad, precioVenta, productoId)
VALUES 
(1, 1, 'Leche Entera', 2, 1200.0, 1),
(1, 2, 'Galletitas Dulces', 1, 900.0, 3);
