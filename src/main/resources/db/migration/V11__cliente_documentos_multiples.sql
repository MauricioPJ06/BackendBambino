CREATE TABLE IF NOT EXISTS cliente_documento (
  id_documento BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  id_cliente BIGINT UNSIGNED NOT NULL,
  doc_tipo ENUM('DNI','RUC','CE') NOT NULL,
  doc_numero VARCHAR(20) NOT NULL,
  es_principal TINYINT(1) NOT NULL DEFAULT 0,
  activo TINYINT(1) NOT NULL DEFAULT 1,
  fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_actualizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_cliente_documento_cliente FOREIGN KEY (id_cliente) REFERENCES cliente_perfil(id_cliente) ON DELETE CASCADE,
  CONSTRAINT uq_cliente_documento UNIQUE (id_cliente, doc_tipo, doc_numero)
) ENGINE=InnoDB;

CREATE INDEX idx_cliente_documento_cliente ON cliente_documento(id_cliente);
CREATE INDEX idx_cliente_documento_principal ON cliente_documento(id_cliente, es_principal, activo);

INSERT INTO cliente_documento (id_cliente, doc_tipo, doc_numero, es_principal, activo, fecha_creacion, fecha_actualizacion)
SELECT
  cp.id_cliente,
  CASE
    WHEN cp.doc_tipo IN ('DNI','RUC','CE') THEN cp.doc_tipo
    ELSE 'DNI'
  END AS doc_tipo,
  cp.doc_numero,
  1 AS es_principal,
  1 AS activo,
  cp.fecha_creacion,
  cp.fecha_actualizacion
FROM cliente_perfil cp
WHERE cp.doc_numero IS NOT NULL
  AND TRIM(cp.doc_numero) <> ''
ON DUPLICATE KEY UPDATE
  activo = VALUES(activo),
  fecha_actualizacion = VALUES(fecha_actualizacion);

UPDATE cliente_documento cd
JOIN (
  SELECT id_cliente, MIN(id_documento) AS id_documento_principal
  FROM cliente_documento
  WHERE activo = 1
  GROUP BY id_cliente
) base ON base.id_cliente = cd.id_cliente
SET cd.es_principal = CASE WHEN cd.id_documento = base.id_documento_principal THEN 1 ELSE 0 END;
