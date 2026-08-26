-- Especialidades
INSERT INTO especialidades (nombre, descripcion) VALUES
(N'Pediatría General', N'Atención integral del niño sano y enfermo'),
(N'Neumología Pediátrica', N'Enfermedades respiratorias del niño'),
(N'Dermatología Pediátrica', N'Enfermedades de la piel en niños'),
(N'Gastroenterología Pediátrica', N'Enfermedades digestivas del niño'),
(N'Neurología Pediátrica', N'Trastornos neurológicos infantiles'),
(N'Otorrinolaringología Pediátrica', N'Problemas de oído, nariz y garganta');

-- Tarifas
INSERT INTO tarifas (especialidad_id, monto, descripcion) VALUES
(1, 120.00, N'Consulta Pediatría General'),
(2, 150.00, N'Consulta Neumología'),
(3, 130.00, N'Consulta Dermatología'),
(4, 140.00, N'Consulta Gastroenterología'),
(5, 160.00, N'Consulta Neurología'),
(6, 150.00, N'Consulta Otorrinolaringología');

-- Síntomas
INSERT INTO sintomas (nombre, descripcion) VALUES
(N'Fiebre alta persistente', N'Temperatura mayor a 38°C por más de 3 días'),
(N'Tos seca o con flema', N'Tos que dura más de una semana'),
(N'Dolor de oído', N'Dolor intenso en el oído, posible infección'),
(N'Erupción cutánea', N'Sarpullido o manchas en la piel'),
(N'Dificultad para respirar', N'Respiración rápida o con silbidos'),
(N'Dolor abdominal', N'Dolor en el abdomen, cólicos'),
(N'Convulsiones', N'Episodios de movimientos involuntarios'),
(N'Vómitos persistentes', N'Vómitos frecuentes que impiden la hidratación');

-- Relación especialidad-síntoma
INSERT INTO especialidad_sintoma (especialidad_id, sintoma_id, peso) VALUES
(1,1,5), (1,2,3), (1,6,3), (1,8,4),
(2,2,5), (2,5,5), (2,1,2),
(3,4,5), (3,1,2),
(4,6,5), (4,8,4), (4,1,2),
(5,7,5), (5,1,3),
(6,3,5), (6,1,2), (6,2,2);

-- Consultorios
INSERT INTO consultorios (nombre, ubicacion) VALUES
(N'Consultorio 101', N'Primer piso, ala izquierda'),
(N'Consultorio 102', N'Primer piso, ala derecha'),
(N'Consultorio 201', N'Segundo piso, ala izquierda'),
(N'Consultorio 202', N'Segundo piso, ala derecha');

-- Métodos de pago
INSERT INTO metodos_pago (nombre, descripcion) VALUES
(N'Efectivo', N'Pago en caja de la clínica'),
(N'Tarjeta de crédito', N'Pago con tarjeta de crédito'),
(N'Tarjeta de débito', N'Pago con tarjeta de débito'),
(N'Transferencia', N'Transferencia bancaria'),
(N'Pago en línea', N'Pago a través de pasarela de pagos');