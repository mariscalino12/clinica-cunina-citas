-- Eliminar tablas en orden correcto (primero las que dependen de otras)
DROP TABLE IF EXISTS notificaciones;
DROP TABLE IF EXISTS historial_medico;
DROP TABLE IF EXISTS receta_medicamentos;
DROP TABLE IF EXISTS recetas;
DROP TABLE IF EXISTS pagos;
DROP TABLE IF EXISTS citas;
DROP TABLE IF EXISTS triaje_sintomas;
DROP TABLE IF EXISTS triajes;
DROP TABLE IF EXISTS horarios_medico;
DROP TABLE IF EXISTS especialidad_sintoma;
DROP TABLE IF EXISTS medicos;
DROP TABLE IF EXISTS tarifas;
DROP TABLE IF EXISTS consultorios;
DROP TABLE IF EXISTS sintomas;
DROP TABLE IF EXISTS especialidades;
DROP TABLE IF EXISTS pacientes;
DROP TABLE IF EXISTS usuarios;
DROP TABLE IF EXISTS medicamentos;
DROP TABLE IF EXISTS metodos_pago;

-- Crear tablas en orden correcto (primero las independientes)
CREATE TABLE usuarios (
    id_usuario BIGINT IDENTITY(1,1) PRIMARY KEY,
    dni NVARCHAR(20) UNIQUE,
    nombre NVARCHAR(100) NOT NULL,
    apellido NVARCHAR(100) NOT NULL,
    email NVARCHAR(150) NOT NULL UNIQUE,
    password_hash NVARCHAR(255) NOT NULL,
    rol NVARCHAR(20) NOT NULL CHECK (rol IN ('ADMIN','MEDICO','TUTOR')),
    telefono NVARCHAR(20),
    direccion NVARCHAR(200),
    fecha_registro DATETIME DEFAULT GETDATE()
);

CREATE TABLE especialidades (
    id_especialidad BIGINT IDENTITY(1,1) PRIMARY KEY,
    nombre NVARCHAR(100) NOT NULL,
    descripcion NVARCHAR(MAX),
    imagen_url NVARCHAR(500)
);

CREATE TABLE pacientes (
    id_paciente BIGINT IDENTITY(1,1) PRIMARY KEY,
    tutor_id BIGINT NOT NULL,
    dni NVARCHAR(20) UNIQUE,
    nombre NVARCHAR(100) NOT NULL,
    apellido NVARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    genero CHAR(1) CHECK (genero IN ('M','F')),
    grupo_sanguineo NVARCHAR(5),
    alergias NVARCHAR(MAX),
    FOREIGN KEY (tutor_id) REFERENCES usuarios(id_usuario)
);

CREATE TABLE tarifas (
    id_tarifa BIGINT IDENTITY(1,1) PRIMARY KEY,
    especialidad_id BIGINT NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    descripcion NVARCHAR(200),
    FOREIGN KEY (especialidad_id) REFERENCES especialidades(id_especialidad)
);

CREATE TABLE medicos (
    id_medico BIGINT IDENTITY(1,1) PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE,
    especialidad_id BIGINT NOT NULL,
    numero_colegiatura NVARCHAR(50) NOT NULL UNIQUE,
    activo BIT DEFAULT 1,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id_usuario),
    FOREIGN KEY (especialidad_id) REFERENCES especialidades(id_especialidad)
);

CREATE TABLE consultorios (
    id_consultorio BIGINT IDENTITY(1,1) PRIMARY KEY,
    nombre NVARCHAR(100) NOT NULL,
    ubicacion NVARCHAR(200)
);

CREATE TABLE sintomas (
    id_sintoma BIGINT IDENTITY(1,1) PRIMARY KEY,
    nombre NVARCHAR(100) NOT NULL,
    descripcion NVARCHAR(MAX)
);

CREATE TABLE especialidad_sintoma (
    id_relacion BIGINT IDENTITY(1,1) PRIMARY KEY,
    especialidad_id BIGINT NOT NULL,
    sintoma_id BIGINT NOT NULL,
    peso INT NOT NULL DEFAULT 1,
    FOREIGN KEY (especialidad_id) REFERENCES especialidades(id_especialidad),
    FOREIGN KEY (sintoma_id) REFERENCES sintomas(id_sintoma),
    CONSTRAINT UQ_especialidad_sintoma UNIQUE (especialidad_id, sintoma_id)
);

CREATE TABLE horarios_medico (
    id_horario BIGINT IDENTITY(1,1) PRIMARY KEY,
    medico_id BIGINT NOT NULL,
    dia_semana TINYINT NOT NULL CHECK (dia_semana BETWEEN 1 AND 7),
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    consultorio_id BIGINT NULL,
    FOREIGN KEY (medico_id) REFERENCES medicos(id_medico),
    FOREIGN KEY (consultorio_id) REFERENCES consultorios(id_consultorio)
);

CREATE TABLE triajes (
    id_triaje BIGINT IDENTITY(1,1) PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    fecha_evaluacion DATETIME DEFAULT GETDATE(),
    especialidad_recomendada_id BIGINT NOT NULL,
    notas NVARCHAR(MAX),
    FOREIGN KEY (paciente_id) REFERENCES pacientes(id_paciente),
    FOREIGN KEY (especialidad_recomendada_id) REFERENCES especialidades(id_especialidad)
);

CREATE TABLE triaje_sintomas (
    id_triaje_sintoma BIGINT IDENTITY(1,1) PRIMARY KEY,
    triaje_id BIGINT NOT NULL,
    sintoma_id BIGINT NOT NULL,
    FOREIGN KEY (triaje_id) REFERENCES triajes(id_triaje),
    FOREIGN KEY (sintoma_id) REFERENCES sintomas(id_sintoma),
    CONSTRAINT UQ_triaje_sintoma UNIQUE (triaje_id, sintoma_id)
);

CREATE TABLE citas (
    id_cita BIGINT IDENTITY(1,1) PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    medico_id BIGINT NOT NULL,
    especialidad_id BIGINT NOT NULL,
    consultorio_id BIGINT NULL,
    triaje_id BIGINT NULL,
    tarifa_id BIGINT NULL,
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

CREATE TABLE metodos_pago (
    id_metodo_pago BIGINT IDENTITY(1,1) PRIMARY KEY,
    nombre NVARCHAR(50) NOT NULL,
    descripcion NVARCHAR(200)
);

CREATE TABLE pagos (
    id_pago BIGINT IDENTITY(1,1) PRIMARY KEY,
    cita_id BIGINT NOT NULL,
    metodo_pago_id BIGINT NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    fecha_pago DATETIME DEFAULT GETDATE(),
    comprobante NVARCHAR(100),
    estado NVARCHAR(20) NOT NULL CHECK (estado IN ('PENDIENTE','COMPLETADO','FALLIDO')),
    FOREIGN KEY (cita_id) REFERENCES citas(id_cita),
    FOREIGN KEY (metodo_pago_id) REFERENCES metodos_pago(id_metodo_pago)
);

CREATE TABLE medicamentos (
    id_medicamento BIGINT IDENTITY(1,1) PRIMARY KEY,
    nombre NVARCHAR(150) NOT NULL,
    presentacion NVARCHAR(100),
    concentracion NVARCHAR(100),
    stock INT DEFAULT 0,
    precio_unitario DECIMAL(10,2)
);

CREATE TABLE recetas (
    id_receta BIGINT IDENTITY(1,1) PRIMARY KEY,
    cita_id BIGINT NOT NULL,
    paciente_id BIGINT NOT NULL,
    medico_id BIGINT NOT NULL,
    fecha_emision DATETIME DEFAULT GETDATE(),
    indicaciones_generales NVARCHAR(MAX),
    estado NVARCHAR(20) DEFAULT 'EMITIDA' CHECK (estado IN ('EMITIDA','DESPACHADA','ANULADA')),
    FOREIGN KEY (cita_id) REFERENCES citas(id_cita),
    FOREIGN KEY (paciente_id) REFERENCES pacientes(id_paciente),
    FOREIGN KEY (medico_id) REFERENCES medicos(id_medico)
);

CREATE TABLE receta_medicamentos (
    id_detalle BIGINT IDENTITY(1,1) PRIMARY KEY,
    receta_id BIGINT NOT NULL,
    medicamento_id BIGINT NOT NULL,
    dosis NVARCHAR(100),
    frecuencia NVARCHAR(100),
    duracion NVARCHAR(100),
    instrucciones NVARCHAR(MAX),
    FOREIGN KEY (receta_id) REFERENCES recetas(id_receta),
    FOREIGN KEY (medicamento_id) REFERENCES medicamentos(id_medicamento)
);

CREATE TABLE historial_medico (
    id_historial BIGINT IDENTITY(1,1) PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    cita_id BIGINT NOT NULL,
    diagnostico NVARCHAR(MAX),
    tratamiento NVARCHAR(MAX),
    notas NVARCHAR(MAX),
    receta_id BIGINT NULL,
    fecha_atencion DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (paciente_id) REFERENCES pacientes(id_paciente),
    FOREIGN KEY (cita_id) REFERENCES citas(id_cita),
    FOREIGN KEY (receta_id) REFERENCES recetas(id_receta)
);

CREATE TABLE notificaciones (
    id_notificacion BIGINT IDENTITY(1,1) PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    mensaje NVARCHAR(MAX) NOT NULL,
    leida BIT DEFAULT 0,
    fecha_creacion DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id_usuario)
);

-- Índices
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