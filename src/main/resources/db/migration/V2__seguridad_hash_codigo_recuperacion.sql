ALTER TABLE recuperacion_password_codigo
ADD COLUMN codigo_hash VARCHAR(255) NULL AFTER codigo;

ALTER TABLE recuperacion_password_codigo
MODIFY COLUMN codigo VARCHAR(10) NULL;

CREATE INDEX idx_recuperacion_correo_estado_expiracion
ON recuperacion_password_codigo(correo, estado, fecha_expiracion);
