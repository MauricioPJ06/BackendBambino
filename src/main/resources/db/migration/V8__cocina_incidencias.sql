CREATE TABLE IF NOT EXISTS pedido_cocina_incidencia (
  id_incidencia BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  id_pedido BIGINT UNSIGNED NOT NULL,
  id_usuario_cocina BIGINT UNSIGNED NOT NULL,
  tipo_incidencia VARCHAR(40) NOT NULL,
  detalle VARCHAR(255) NOT NULL,
  fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_cocina_incidencia_pedido FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido) ON DELETE CASCADE,
  CONSTRAINT fk_cocina_incidencia_usuario FOREIGN KEY (id_usuario_cocina) REFERENCES usuario(id_usuario)
) ENGINE=InnoDB;

CREATE INDEX idx_cocina_incidencia_pedido_fecha
  ON pedido_cocina_incidencia(id_pedido, fecha_creacion);
