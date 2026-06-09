ALTER TABLE zona_delivery
  ADD COLUMN hora_inicio_atencion TIME NULL,
  ADD COLUMN hora_fin_atencion TIME NULL;

UPDATE zona_delivery
SET hora_inicio_atencion = COALESCE(hora_inicio_atencion, '15:00:00'),
    hora_fin_atencion = COALESCE(hora_fin_atencion, '23:00:00')
WHERE activo = 1;
