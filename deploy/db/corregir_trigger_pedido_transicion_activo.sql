-- Corrige el trigger de transicion de pedido para usar la columna normalizada `activo`.
-- Ejecutar contra bambino_db si aparece: Unknown column 't.activa' in 'where clause'.

DROP TRIGGER IF EXISTS trg_pedido_transicion_estado;

INSERT INTO pedido_estado_transicion_permitida (estado_origen, estado_destino, actor_tipo, activo)
VALUES
  ('CREADO', 'PAGO_PENDIENTE', 'SISTEMA', 1),
  ('PAGO_PENDIENTE', 'PAGO_APROBADO', 'SISTEMA', 1),
  ('PAGO_PENDIENTE', 'PAGO_RECHAZADO', 'SISTEMA', 1)
ON DUPLICATE KEY UPDATE activo = VALUES(activo);

DELIMITER //

CREATE TRIGGER trg_pedido_transicion_estado
BEFORE UPDATE ON pedido
FOR EACH ROW
BEGIN
  DECLARE v_count INT DEFAULT 0;
  DECLARE v_rol_usuario VARCHAR(20);

  IF NEW.estado_actual <> OLD.estado_actual THEN
    SELECT COUNT(*) INTO v_count
    FROM pedido_estado_transicion_permitida t
    WHERE t.estado_origen = OLD.estado_actual
      AND t.estado_destino = NEW.estado_actual
      AND t.activo = 1;

    IF v_count = 0 THEN
      SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Transicion de estado no permitida';
    END IF;

    IF NEW.estado_actual = 'EN_PREPARACION' THEN
      IF NEW.usuario_actualizacion IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Debe enviar usuario_actualizacion al iniciar preparacion';
      END IF;

      SELECT r.nombre INTO v_rol_usuario
      FROM usuario u
      JOIN rol r ON r.id_rol = u.id_rol
      WHERE u.id_usuario = NEW.usuario_actualizacion
      LIMIT 1;

      IF v_rol_usuario IS NULL OR v_rol_usuario <> 'COCINA' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Solo un usuario con rol COCINA puede iniciar preparacion';
      END IF;

      IF NEW.usuario_cocina_preparacion IS NULL THEN
        SET NEW.usuario_cocina_preparacion = NEW.usuario_actualizacion;
      END IF;

      IF NEW.fecha_inicio_preparacion IS NULL THEN
        SET NEW.fecha_inicio_preparacion = NOW();
      END IF;
    END IF;

    IF NEW.estado_actual IN ('LISTO_RECOJO', 'LISTO_DESPACHO')
       AND OLD.estado_actual = 'EN_PREPARACION'
       AND NEW.fecha_fin_preparacion IS NULL THEN
      SET NEW.fecha_fin_preparacion = NOW();
    END IF;
  END IF;
END//

DELIMITER ;
