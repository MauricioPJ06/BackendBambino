-- Transiciones necesarias para que el pago simulado desde checkout pueda mover el pedido.

INSERT INTO pedido_estado_transicion_permitida (estado_origen, estado_destino, actor_tipo, activo)
VALUES
  ('CREADO', 'PAGO_PENDIENTE', 'SISTEMA', 1),
  ('PAGO_PENDIENTE', 'PAGO_APROBADO', 'SISTEMA', 1),
  ('PAGO_PENDIENTE', 'PAGO_RECHAZADO', 'SISTEMA', 1)
ON DUPLICATE KEY UPDATE activo = VALUES(activo);
