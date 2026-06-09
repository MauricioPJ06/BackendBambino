CREATE TABLE IF NOT EXISTS recuperacion_password_codigo (
  id_codigo BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  id_usuario BIGINT UNSIGNED NOT NULL,
  correo VARCHAR(190) NOT NULL,
  codigo VARCHAR(10) NOT NULL,
  estado ENUM('PENDIENTE','USADO','EXPIRADO') NOT NULL DEFAULT 'PENDIENTE',
  fecha_expiracion DATETIME NOT NULL,
  intentos_fallidos INT NOT NULL DEFAULT 0,
  fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_actualizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_recuperacion_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
) ENGINE=InnoDB;

CREATE INDEX idx_recuperacion_correo_estado ON recuperacion_password_codigo(correo, estado);
