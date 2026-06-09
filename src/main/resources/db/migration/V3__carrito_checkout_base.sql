CREATE TABLE IF NOT EXISTS carrito (
  id_carrito BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  id_cliente BIGINT UNSIGNED NOT NULL,
  estado ENUM('ABIERTO','CONFIRMADO','ABANDONADO','CONVERTIDO') NOT NULL DEFAULT 'ABIERTO',
  canal ENUM('WEB','CHATBOT_WEB') NOT NULL DEFAULT 'WEB',
  usuario_creacion BIGINT UNSIGNED NULL,
  usuario_actualizacion BIGINT UNSIGNED NULL,
  fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_actualizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_carrito_cliente FOREIGN KEY (id_cliente) REFERENCES cliente_perfil(id_cliente),
  CONSTRAINT fk_carrito_usuario_creacion FOREIGN KEY (usuario_creacion) REFERENCES usuario(id_usuario),
  CONSTRAINT fk_carrito_usuario_actualizacion FOREIGN KEY (usuario_actualizacion) REFERENCES usuario(id_usuario)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS carrito_item (
  id_carrito_item BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  id_carrito BIGINT UNSIGNED NOT NULL,
  id_producto BIGINT UNSIGNED NOT NULL,
  cantidad DECIMAL(10,3) NOT NULL,
  precio_unitario_snapshot DECIMAL(10,2) NOT NULL,
  descuento_unitario_snapshot DECIMAL(10,2) NOT NULL DEFAULT 0,
  observacion VARCHAR(220),
  fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_actualizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY uq_carrito_item (id_carrito, id_producto),
  CONSTRAINT fk_carrito_item_carrito FOREIGN KEY (id_carrito) REFERENCES carrito(id_carrito) ON DELETE CASCADE,
  CONSTRAINT fk_carrito_item_producto FOREIGN KEY (id_producto) REFERENCES producto(id_producto)
) ENGINE=InnoDB;
