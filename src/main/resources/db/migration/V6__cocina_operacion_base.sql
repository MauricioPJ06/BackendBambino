-- V6 corregido para bases donde ya existen columnas de pedido y tabla pedido_asignacion_cocina

ALTER TABLE pedido
  ADD COLUMN IF NOT EXISTS usuario_cocina_preparacion BIGINT UNSIGNED NULL,
  ADD COLUMN IF NOT EXISTS fecha_inicio_preparacion DATETIME NULL,
  ADD COLUMN IF NOT EXISTS fecha_fin_preparacion DATETIME NULL;

SET @fk_pedido_cocina := (
  SELECT COUNT(*)
  FROM information_schema.TABLE_CONSTRAINTS
  WHERE CONSTRAINT_SCHEMA = DATABASE()
    AND TABLE_NAME = 'pedido'
    AND CONSTRAINT_NAME = 'fk_pedido_usuario_cocina_preparacion'
    AND CONSTRAINT_TYPE = 'FOREIGN KEY'
);
SET @sql_pedido_fk := IF(
  @fk_pedido_cocina = 0,
  'ALTER TABLE pedido ADD CONSTRAINT fk_pedido_usuario_cocina_preparacion FOREIGN KEY (usuario_cocina_preparacion) REFERENCES usuario(id_usuario)',
  'SELECT 1'
);
PREPARE stmt FROM @sql_pedido_fk;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

CREATE TABLE IF NOT EXISTS pedido_asignacion_cocina (
  id_asignacion BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  id_pedido BIGINT UNSIGNED NOT NULL,
  id_usuario_cocina BIGINT UNSIGNED NULL,
  fecha_asignacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  motivo VARCHAR(255) NULL,
  CONSTRAINT fk_asignacion_pedido FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido) ON DELETE CASCADE,
  CONSTRAINT fk_asignacion_usuario_cocina FOREIGN KEY (id_usuario_cocina) REFERENCES usuario(id_usuario)
) ENGINE=InnoDB;

-- Compatibilidad con estructura antigua de tu tabla existente
ALTER TABLE pedido_asignacion_cocina
  ADD COLUMN IF NOT EXISTS id_usuario_cocina BIGINT UNSIGNED NULL,
  ADD COLUMN IF NOT EXISTS fecha_asignacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  ADD COLUMN IF NOT EXISTS motivo VARCHAR(255) NULL;

UPDATE pedido_asignacion_cocina
SET id_usuario_cocina = COALESCE(id_usuario_cocina, id_cocina_actual)
WHERE id_usuario_cocina IS NULL;

UPDATE pedido_asignacion_cocina
SET fecha_asignacion = COALESCE(fecha_asignacion, fecha_creacion)
WHERE fecha_asignacion IS NULL;

UPDATE pedido_asignacion_cocina
SET motivo = COALESCE(motivo, motivo_reasignacion)
WHERE motivo IS NULL;

SET @fk_asig_cocina := (
  SELECT COUNT(*)
  FROM information_schema.TABLE_CONSTRAINTS
  WHERE CONSTRAINT_SCHEMA = DATABASE()
    AND TABLE_NAME = 'pedido_asignacion_cocina'
    AND CONSTRAINT_NAME = 'fk_asignacion_usuario_cocina'
    AND CONSTRAINT_TYPE = 'FOREIGN KEY'
);
SET @sql_asig_fk := IF(
  @fk_asig_cocina = 0,
  'ALTER TABLE pedido_asignacion_cocina ADD CONSTRAINT fk_asignacion_usuario_cocina FOREIGN KEY (id_usuario_cocina) REFERENCES usuario(id_usuario)',
  'SELECT 1'
);
PREPARE stmt2 FROM @sql_asig_fk;
EXECUTE stmt2;
DEALLOCATE PREPARE stmt2;

SET @idx_asig := (
  SELECT COUNT(*)
  FROM information_schema.STATISTICS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'pedido_asignacion_cocina'
    AND INDEX_NAME = 'idx_asignacion_cocina_pedido_fecha'
);
SET @sql_asig_idx := IF(
  @idx_asig = 0,
  'CREATE INDEX idx_asignacion_cocina_pedido_fecha ON pedido_asignacion_cocina(id_pedido, fecha_asignacion)',
  'SELECT 1'
);
PREPARE stmt3 FROM @sql_asig_idx;
EXECUTE stmt3;
DEALLOCATE PREPARE stmt3;
