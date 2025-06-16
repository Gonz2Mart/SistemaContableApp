-- Eliminar la base de datos si existe (borra todo lo que contiene)
DROP DATABASE IF EXISTS SistemaContable;

-- Crear la base de datos
CREATE DATABASE SistemaContable;
USE SistemaContable;

-- Crear tabla Contribuyente
CREATE TABLE Contribuyente (
    RFC VARCHAR(13) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    regimen_fiscal VARCHAR(50) NOT NULL
);

-- Crear tabla Factura
CREATE TABLE Factura (
    folio INT AUTO_INCREMENT PRIMARY KEY,
    rfc_emisor VARCHAR(13),
    cliente VARCHAR(100),
    monto DECIMAL(10, 2),
    fecha DATE,
    estatus ENUM('Pagada', 'Pendiente', 'Cancelada'),
    FOREIGN KEY (rfc_emisor) REFERENCES Contribuyente(RFC)
);

-- Crear tabla DeclaracionFiscal
CREATE TABLE DeclaracionFiscal (
    id INT AUTO_INCREMENT PRIMARY KEY,
    rfc_contribuyente VARCHAR(13),
    tipo_declaracion ENUM('Mensual', 'Anual', 'Complementaria'),
    monto DECIMAL(10,2),
    fecha DATE,
    estatus ENUM('Presentada', 'Pendiente', 'Vencida'),
    FOREIGN KEY (rfc_contribuyente) REFERENCES Contribuyente(RFC)
);

-- Crear tabla OpinionCumplimiento32D
CREATE TABLE OpinionCumplimiento32D (
    id INT AUTO_INCREMENT PRIMARY KEY,
    rfc_contribuyente VARCHAR(13),
    fecha_emision DATE,
    resultado ENUM('Positiva', 'Negativa'),
    FOREIGN KEY (rfc_contribuyente) REFERENCES Contribuyente(RFC)
);

-- Crear tabla LogBorradoFactura para trigger
CREATE TABLE LogBorradoFactura (
    id INT AUTO_INCREMENT PRIMARY KEY,
    folio INT,
    fecha_borrado DATETIME
);

-- Insertar datos en Contribuyente
INSERT INTO Contribuyente VALUES 
('MAMG850101ABC', 'Martinez Gonzalo', 'General de Ley Personas Físicas'),
('JUAP920202DEF', 'Juan Perez', 'Régimen Simplificado de Confianza'),
('LOPR830303GHI', 'Lorena Prado', 'Régimen de Incorporación Fiscal'),
('RAMC940404JKL', 'Raúl Martínez', 'Personas Morales con Fines Lucrativos'),
('GARC750505MNO', 'Gabriela Cruz', 'Arrendamiento');

-- Insertar datos en Factura
INSERT INTO Factura (rfc_emisor, cliente, monto, fecha, estatus) VALUES
('MAMG850101ABC', 'Empresa XYZ', 10000.00, '2025-06-01', 'Pagada'),
('MAMG850101ABC', 'Cliente A', 5000.00, '2025-06-10', 'Pendiente'),
('JUAP920202DEF', 'Comercial ABC', 7800.00, '2025-06-12', 'Pagada'),
('LOPR830303GHI', 'Cliente B', 3000.00, '2025-06-05', 'Cancelada'),
('RAMC940404JKL', 'Cliente C', 12000.00, '2025-06-15', 'Pendiente');

-- Insertar datos en DeclaracionFiscal
INSERT INTO DeclaracionFiscal (rfc_contribuyente, tipo_declaracion, monto, fecha, estatus) VALUES
('MAMG850101ABC', 'Mensual', 3000.00, '2025-06-16', 'Pendiente'),
('JUAP920202DEF', 'Anual', 15000.00, '2025-06-15', 'Presentada'),
('LOPR830303GHI', 'Mensual', 2500.00, '2025-06-17', 'Vencida'),
('RAMC940404JKL', 'Complementaria', 4500.00, '2025-06-10', 'Presentada'),
('GARC750505MNO', 'Mensual', 3500.00, '2025-06-16', 'Pendiente');

-- Insertar datos en OpinionCumplimiento32D
INSERT INTO OpinionCumplimiento32D (rfc_contribuyente, fecha_emision, resultado) VALUES
('MAMG850101ABC', '2025-06-15', 'Positiva'),
('JUAP920202DEF', '2025-06-14', 'Negativa'),
('LOPR830303GHI', '2025-06-12', 'Positiva'),
('RAMC940404JKL', '2025-06-10', 'Negativa'),
('GARC750505MNO', '2025-06-13', 'Positiva');

-- Procedimiento almacenado corregido (usar VARCHAR en lugar de ENUM en parámetros)
DELIMITER //
CREATE PROCEDURE insertarDeclaracion(
    IN p_rfc VARCHAR(13),
    IN p_tipo VARCHAR(20),
    IN p_monto DECIMAL(10,2),
    IN p_fecha DATE,
    IN p_estatus VARCHAR(20)
)
BEGIN
    INSERT INTO DeclaracionFiscal (rfc_contribuyente, tipo_declaracion, monto, fecha, estatus)
    VALUES (p_rfc, p_tipo, p_monto, p_fecha, p_estatus);
END //
DELIMITER ;

-- Trigger para registrar borrado de facturas
DELIMITER //
CREATE TRIGGER after_delete_factura
AFTER DELETE ON Factura
FOR EACH ROW
BEGIN
    INSERT INTO LogBorradoFactura (folio, fecha_borrado)
    VALUES (OLD.folio, NOW());
END //
DELIMITER ;

-- Vista para JOIN entre contribuyente y declaraciones
CREATE VIEW VistaDeclaraciones AS
SELECT 
    c.RFC,
    c.nombre,
    d.tipo_declaracion,
    d.monto,
    d.fecha,
    d.estatus
FROM Contribuyente c
JOIN DeclaracionFiscal d ON c.RFC = d.rfc_contribuyente;


