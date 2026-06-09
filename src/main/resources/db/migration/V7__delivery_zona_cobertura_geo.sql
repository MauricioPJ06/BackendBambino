ALTER TABLE zona_delivery
  ADD COLUMN IF NOT EXISTS latitud_centro DECIMAL(10,7) NULL AFTER cobertura_descripcion,
  ADD COLUMN IF NOT EXISTS longitud_centro DECIMAL(10,7) NULL AFTER latitud_centro,
  ADD COLUMN IF NOT EXISTS radio_km DECIMAL(10,2) NULL AFTER longitud_centro;

UPDATE zona_delivery
SET radio_km = COALESCE(radio_km, 5.00)
WHERE activo = 1;
