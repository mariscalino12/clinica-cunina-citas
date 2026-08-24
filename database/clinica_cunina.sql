
CREATE DATABASE clinica_cunina;
GO

USE clinica_cunina;
GO

CREATE TABLE usuarios (
    id_usuario INT IDENTITY(1,1) PRIMARY KEY,
    nombre NVARCHAR(100) NOT NULL,
    apellido NVARCHAR(100) NOT NULL,
    email NVARCHAR(150) NOT NULL UNIQUE,
    password_hash NVARCHAR(255) NOT NULL,
    rol NVARCHAR(20) NOT NULL CHECK (rol IN ('ADMIN','MEDICO','TUTOR')),
    telefono NVARCHAR(20),
    direccion NVARCHAR(200),
    fecha_registro DATETIME DEFAULT GETDATE()
);

CREATE TABLE pacientes (
    id_paciente INT IDENTITY(1,1) PRIMARY KEY,
    tutor_id INT NOT NULL,
    nombre NVARCHAR(100) NOT NULL,
    apellido NVARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    genero CHAR(1) CHECK (genero IN ('M','F')),
    grupo_sanguineo NVARCHAR(5),
    alergias NVARCHAR(MAX),
    FOREIGN KEY (tutor_id) REFERENCES usuarios(id_usuario)
);
CREATE TABLE especialidades (
    id_especialidad INT IDENTITY(1,1) PRIMARY KEY,
    nombre NVARCHAR(100) NOT NULL,
    descripcion NVARCHAR(MAX),
    imagen_url NVARCHAR(500)
);

CREATE TABLE tarifas (
    id_tarifa INT IDENTITY(1,1) PRIMARY KEY,
    especialidad_id INT NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    descripcion NVARCHAR(200),
    FOREIGN KEY (especialidad_id) REFERENCES especialidades(id_especialidad)
);

CREATE TABLE medicos (
    id_medico INT IDENTITY(1,1) PRIMARY KEY,
    usuario_id INT NOT NULL UNIQUE,
    especialidad_id INT NOT NULL,
    numero_colegiatura NVARCHAR(50) NOT NULL UNIQUE,
    activo BIT DEFAULT 1,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id_usuario),
    FOREIGN KEY (especialidad_id) REFERENCES especialidades(id_especialidad)
);

CREATE TABLE consultorios (
    id_consultorio INT IDENTITY(1,1) PRIMARY KEY,
    nombre NVARCHAR(100) NOT NULL,
    ubicacion NVARCHAR(200)
);

CREATE TABLE sintomas (
    id_sintoma INT IDENTITY(1,1) PRIMARY KEY,
    nombre NVARCHAR(100) NOT NULL,
    descripcion NVARCHAR(MAX)
);

CREATE TABLE especialidad_sintoma (
    id_relacion INT IDENTITY(1,1) PRIMARY KEY,
    especialidad_id INT NOT NULL,
    sintoma_id INT NOT NULL,
    peso INT NOT NULL DEFAULT 1,
    FOREIGN KEY (especialidad_id) REFERENCES especialidades(id_especialidad),
    FOREIGN KEY (sintoma_id) REFERENCES sintomas(id_sintoma),
    CONSTRAINT UQ_especialidad_sintoma UNIQUE (especialidad_id, sintoma_id)
);

CREATE TABLE horarios_medico (
    id_horario INT IDENTITY(1,1) PRIMARY KEY,
    medico_id INT NOT NULL,
    dia_semana TINYINT NOT NULL CHECK (dia_semana BETWEEN 1 AND 7),
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    consultorio_id INT NULL,
    FOREIGN KEY (medico_id) REFERENCES medicos(id_medico),
    FOREIGN KEY (consultorio_id) REFERENCES consultorios(id_consultorio)
);

CREATE TABLE triajes (
    id_triaje INT IDENTITY(1,1) PRIMARY KEY,
    paciente_id INT NOT NULL,
    fecha_evaluacion DATETIME DEFAULT GETDATE(),
    especialidad_recomendada_id INT NOT NULL,
    notas NVARCHAR(MAX),
    FOREIGN KEY (paciente_id) REFERENCES pacientes(id_paciente),
    FOREIGN KEY (especialidad_recomendada_id) REFERENCES especialidades(id_especialidad)
);

CREATE TABLE triaje_sintomas (
    id_triaje_sintoma INT IDENTITY(1,1) PRIMARY KEY,
    triaje_id INT NOT NULL,
    sintoma_id INT NOT NULL,
    FOREIGN KEY (triaje_id) REFERENCES triajes(id_triaje),
    FOREIGN KEY (sintoma_id) REFERENCES sintomas(id_sintoma),
    CONSTRAINT UQ_triaje_sintoma UNIQUE (triaje_id, sintoma_id)
);

CREATE TABLE metodos_pago (
    id_metodo_pago INT IDENTITY(1,1) PRIMARY KEY,
    nombre NVARCHAR(50) NOT NULL,
    descripcion NVARCHAR(200)
);

CREATE TABLE citas (
    id_cita INT IDENTITY(1,1) PRIMARY KEY,
    paciente_id INT NOT NULL,
    medico_id INT NOT NULL,
    especialidad_id INT NOT NULL,
    consultorio_id INT NULL,
    triaje_id INT NULL,
    tarifa_id INT NULL,
    fecha_hora DATETIME NOT NULL,
    estado NVARCHAR(20) NOT NULL CHECK (estado IN ('PENDIENTE','CONFIRMADA','CANCELADA','ATENDIDA')),
    tipo_consulta NVARCHAR(20) NOT NULL DEFAULT 'PRESENCIAL' CHECK (tipo_consulta IN ('PRESENCIAL','TELEMEDICINA')),
    estado_pago NVARCHAR(20) NOT NULL DEFAULT 'PENDIENTE' CHECK (estado_pago IN ('PENDIENTE','PAGADO','REEMBOLSADO')),
    sintomas_descripcion NVARCHAR(MAX),
    fecha_creacion DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (paciente_id) REFERENCES pacientes(id_paciente),
    FOREIGN KEY (medico_id) REFERENCES medicos(id_medico),
    FOREIGN KEY (especialidad_id) REFERENCES especialidades(id_especialidad),
    FOREIGN KEY (consultorio_id) REFERENCES consultorios(id_consultorio),
    FOREIGN KEY (triaje_id) REFERENCES triajes(id_triaje),
    FOREIGN KEY (tarifa_id) REFERENCES tarifas(id_tarifa)
);

CREATE TABLE pagos (
    id_pago INT IDENTITY(1,1) PRIMARY KEY,
    cita_id INT NOT NULL,
    metodo_pago_id INT NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    fecha_pago DATETIME DEFAULT GETDATE(),
    comprobante NVARCHAR(100),
    estado NVARCHAR(20) NOT NULL CHECK (estado IN ('PENDIENTE','COMPLETADO','FALLIDO')),
    FOREIGN KEY (cita_id) REFERENCES citas(id_cita),
    FOREIGN KEY (metodo_pago_id) REFERENCES metodos_pago(id_metodo_pago)
);

CREATE TABLE medicamentos (
    id_medicamento INT IDENTITY(1,1) PRIMARY KEY,
    nombre NVARCHAR(150) NOT NULL,
    presentacion NVARCHAR(100),
    concentracion NVARCHAR(100),
    stock INT DEFAULT 0,
    precio_unitario DECIMAL(10,2)
);

CREATE TABLE recetas (
    id_receta INT IDENTITY(1,1) PRIMARY KEY,
    cita_id INT NOT NULL,
    paciente_id INT NOT NULL,
    medico_id INT NOT NULL,
    fecha_emision DATETIME DEFAULT GETDATE(),
    indicaciones_generales NVARCHAR(MAX),
    estado NVARCHAR(20) DEFAULT 'EMITIDA' CHECK (estado IN ('EMITIDA','DESPACHADA','ANULADA')),
    FOREIGN KEY (cita_id) REFERENCES citas(id_cita),
    FOREIGN KEY (paciente_id) REFERENCES pacientes(id_paciente),
    FOREIGN KEY (medico_id) REFERENCES medicos(id_medico)
);

CREATE TABLE receta_medicamentos (
    id_detalle INT IDENTITY(1,1) PRIMARY KEY,
    receta_id INT NOT NULL,
    medicamento_id INT NOT NULL,
    dosis NVARCHAR(100),
    frecuencia NVARCHAR(100),
    duracion NVARCHAR(100),
    instrucciones NVARCHAR(MAX),
    FOREIGN KEY (receta_id) REFERENCES recetas(id_receta),
    FOREIGN KEY (medicamento_id) REFERENCES medicamentos(id_medicamento)
);

CREATE TABLE historial_medico (
    id_historial INT IDENTITY(1,1) PRIMARY KEY,
    paciente_id INT NOT NULL,
    cita_id INT NOT NULL,
    diagnostico NVARCHAR(MAX),
    tratamiento NVARCHAR(MAX),
    notas NVARCHAR(MAX),
    receta_id INT NULL,
    fecha_atencion DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (paciente_id) REFERENCES pacientes(id_paciente),
    FOREIGN KEY (cita_id) REFERENCES citas(id_cita),
    FOREIGN KEY (receta_id) REFERENCES recetas(id_receta)
);

CREATE TABLE notificaciones (
    id_notificacion INT IDENTITY(1,1) PRIMARY KEY,
    usuario_id INT NOT NULL,
    mensaje NVARCHAR(MAX) NOT NULL,
    leida BIT DEFAULT 0,
    fecha_creacion DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id_usuario)
);
CREATE INDEX idx_citas_fecha ON citas(fecha_hora);
CREATE INDEX idx_citas_medico_fecha ON citas(medico_id, fecha_hora);
CREATE INDEX idx_pacientes_tutor ON pacientes(tutor_id);
CREATE INDEX idx_usuarios_email ON usuarios(email);
CREATE INDEX idx_triajes_paciente ON triajes(paciente_id);
CREATE INDEX idx_triaje_sintomas_triaje ON triaje_sintomas(triaje_id);
CREATE INDEX idx_pagos_cita ON pagos(cita_id);
CREATE INDEX idx_recetas_cita ON recetas(cita_id);
CREATE INDEX idx_receta_medicamentos_receta ON receta_medicamentos(receta_id);
CREATE INDEX idx_historial_paciente ON historial_medico(paciente_id);

INSERT INTO especialidades (nombre, descripcion) VALUES
(N'Pediatría General', N'Atención integral del niño sano y enfermo'),
(N'Neumología Pediátrica', N'Enfermedades respiratorias del niño'),
(N'Dermatología Pediátrica', N'Enfermedades de la piel en niños'),
(N'Gastroenterología Pediátrica', N'Enfermedades digestivas del niño'),
(N'Neurología Pediátrica', N'Trastornos neurológicos infantiles'),
(N'Otorrinolaringología Pediátrica', N'Problemas de oído, nariz y garganta');

INSERT INTO tarifas (especialidad_id, monto, descripcion) VALUES
(1, 120.00, N'Consulta Pediatría General'),
(2, 150.00, N'Consulta Neumología'),
(3, 130.00, N'Consulta Dermatología'),
(4, 140.00, N'Consulta Gastroenterología'),
(5, 160.00, N'Consulta Neurología'),
(6, 150.00, N'Consulta Otorrinolaringología');

INSERT INTO sintomas (nombre, descripcion) VALUES
(N'Fiebre alta persistente', N'Temperatura mayor a 38°C por más de 3 días'),
(N'Tos seca o con flema', N'Tos que dura más de una semana'),
(N'Dolor de oído', N'Dolor intenso en el oído, posible infección'),
(N'Erupción cutánea', N'Sarpullido o manchas en la piel'),
(N'Dificultad para respirar', N'Respiración rápida o con silbidos'),
(N'Dolor abdominal', N'Dolor en el abdomen, cólicos'),
(N'Convulsiones', N'Episodios de movimientos involuntarios'),
(N'Vómitos persistentes', N'Vómitos frecuentes que impiden la hidratación');

INSERT INTO especialidad_sintoma (especialidad_id, sintoma_id, peso) VALUES
(1,1,5), (1,2,3), (1,6,3), (1,8,4),  
(2,2,5), (2,5,5), (2,1,2),             
(3,4,5), (3,1,2),                      
(4,6,5), (4,8,4), (4,1,2),             
(5,7,5), (5,1,3),                      
(6,3,5), (6,1,2), (6,2,2);             

INSERT INTO consultorios (nombre, ubicacion) VALUES
(N'Consultorio 101', N'Primer piso, ala izquierda'),
(N'Consultorio 102', N'Primer piso, ala derecha'),
(N'Consultorio 201', N'Segundo piso, ala izquierda'),
(N'Consultorio 202', N'Segundo piso, ala derecha');

INSERT INTO metodos_pago (nombre, descripcion) VALUES
(N'Efectivo', N'Pago en caja de la clínica'),
(N'Tarjeta de crédito', N'Pago con tarjeta de crédito'),
(N'Tarjeta de débito', N'Pago con tarjeta de débito'),
(N'Transferencia', N'Transferencia bancaria'),
(N'Pago en línea', N'Pago a través de pasarela de pagos');
