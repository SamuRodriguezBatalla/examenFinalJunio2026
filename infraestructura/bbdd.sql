SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;

-- =====================================================
-- Base de datos clínica médica
-- =====================================================

DROP DATABASE IF EXISTS clinica;
CREATE DATABASE clinica
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE clinica;

-- =====================================================
-- TABLA PACIENTES
-- =====================================================
CREATE TABLE pacientes (
    dni               VARCHAR(20) PRIMARY KEY,
    nombre            VARCHAR(80) NOT NULL,
    apellidos         VARCHAR(120) NOT NULL,
    email             VARCHAR(150) NOT NULL,
    telefono          VARCHAR(30),
    direccion         VARCHAR(255)
);

-- =====================================================
-- TABLA MEDICAMENTOS
-- =====================================================
CREATE TABLE medicamentos (
    codigo            VARCHAR(20) PRIMARY KEY,
    nombre            VARCHAR(100) NOT NULL,
    posologia         VARCHAR(500) NOT NULL
);

-- =====================================================
-- TABLA TRATAMIENTOS
-- =====================================================
CREATE TABLE tratamientos (
    id_tratamiento    INT PRIMARY KEY,
    dni_paciente      VARCHAR(20) NOT NULL,
    fecha_inicio      DATE NOT NULL,
    dias_tratamiento  INT NOT NULL,
    diagnostico       VARCHAR(255) NOT NULL,

    CONSTRAINT fk_tratamiento_paciente
        FOREIGN KEY (dni_paciente)
        REFERENCES pacientes(dni)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- =====================================================
-- TABLA MEDICAMENTOS_TRATAMIENTO
-- Relación tratamiento-medicamento
-- =====================================================
CREATE TABLE medicamentos_tratamiento (
    id_registro       INT AUTO_INCREMENT PRIMARY KEY,
    id_tratamiento    INT NOT NULL,
    codigo_medicamento VARCHAR(20) NOT NULL,

    CONSTRAINT fk_mt_tratamiento
        FOREIGN KEY (id_tratamiento)
        REFERENCES tratamientos(id_tratamiento)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_mt_medicamento
        FOREIGN KEY (codigo_medicamento)
        REFERENCES medicamentos(codigo)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

-- =====================================================
-- DATOS DE PRUEBA
-- =====================================================

-- Pacientes
INSERT INTO pacientes
(dni, nombre, apellidos, email, telefono, direccion)
VALUES
('12345678A', 'Ana', 'López García', 'ana.lopez@correo.es', '600111111', 'Calle Mayor 10, Madrid'),
('23456789B', 'Carlos', 'Martín Pérez', 'carlos.martin@correo.es', '600222222', 'Avenida Europa 15, Madrid'),
('34567890C', 'Lucía', 'Sánchez Ruiz', 'lucia.sanchez@correo.es', '600333333', 'Plaza España 8, Madrid');

-- Medicamentos
INSERT INTO medicamentos
(codigo, nombre, posologia)
VALUES
('MED001', 'Ibuprofeno 600 mg', 'Tomar 1 comprimido cada 8 horas después de las comidas'),
('MED002', 'Amoxicilina 500 mg', 'Tomar 1 cápsula cada 8 horas durante 7 días'),
('MED003', 'Paracetamol 1 g', 'Tomar 1 comprimido cada 8 horas si existe dolor'),
('MED004', 'Omeprazol 20 mg', 'Tomar 1 cápsula en ayunas cada mañana'),
('MED005', 'Loratadina 10 mg', 'Tomar 1 comprimido al día'),
('MED006', 'Azitromicina 500 mg', 'Tomar 1 comprimido al día durante 3 días');

-- Tratamientos
INSERT INTO tratamientos
(id_tratamiento, dni_paciente, fecha_inicio, dias_tratamiento, diagnostico)
VALUES
(1001, '12345678A', '2026-05-01', 7, 'Infección respiratoria leve'),
(1002, '23456789B', '2026-05-03', 10, 'Amigdalitis bacteriana'),
(1003, '34567890C', '2026-05-05', 5, 'Rinitis alérgica estacional');

-- Medicamentos asociados a tratamientos
INSERT INTO medicamentos_tratamiento
(id_tratamiento, codigo_medicamento)
VALUES
-- Tratamiento Ana
(1001, 'MED001'),
(1001, 'MED004'),

-- Tratamiento Carlos
(1002, 'MED002'),
(1002, 'MED003'),

-- Tratamiento Lucía
(1003, 'MED005'),
(1003, 'MED006');

COMMIT;
