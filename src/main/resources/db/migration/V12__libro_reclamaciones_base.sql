CREATE TABLE IF NOT EXISTS libro_reclamaciones (
  id_reclamo BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  numero_reclamo VARCHAR(40) NOT NULL,
  id_empresa BIGINT UNSIGNED NOT NULL,
  id_cliente BIGINT UNSIGNED NULL,
  id_pedido BIGINT UNSIGNED NULL,
  codigo_pedido VARCHAR(40) NULL,
  tipo_registro ENUM('RECLAMO','QUEJA') NOT NULL,
  consumidor_nombres VARCHAR(120) NOT NULL,
  consumidor_apellidos VARCHAR(120) NOT NULL,
  doc_tipo ENUM('DNI','RUC','CE','OTRO') NOT NULL,
  doc_numero VARCHAR(20) NOT NULL,
  correo VARCHAR(190) NOT NULL,
  telefono VARCHAR(20) NULL,
  direccion_consumidor VARCHAR(300) NULL,
  fecha_consumo DATETIME NULL,
  monto_reclamado DECIMAL(10,2) NULL,
  detalle_hechos TEXT NOT NULL,
  pedido_consumidor TEXT NOT NULL,
  estado VARCHAR(20) NOT NULL,
  fecha_registro DATETIME NOT NULL,
  fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_actualizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT uq_libro_reclamaciones_numero UNIQUE (numero_reclamo),
  CONSTRAINT fk_libro_reclamaciones_empresa FOREIGN KEY (id_empresa) REFERENCES empresa(id_empresa),
  CONSTRAINT fk_libro_reclamaciones_cliente FOREIGN KEY (id_cliente) REFERENCES cliente_perfil(id_cliente),
  CONSTRAINT fk_libro_reclamaciones_pedido FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido)
) ENGINE=InnoDB;

CREATE INDEX idx_libro_reclamaciones_fecha ON libro_reclamaciones(fecha_registro);
CREATE INDEX idx_libro_reclamaciones_cliente ON libro_reclamaciones(id_cliente);
CREATE INDEX idx_libro_reclamaciones_pedido ON libro_reclamaciones(id_pedido);
