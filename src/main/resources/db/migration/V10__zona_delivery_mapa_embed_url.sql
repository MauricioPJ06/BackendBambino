SET @col_exists := (
  SELECT COUNT(*)
  FROM information_schema.columns
  WHERE table_schema = DATABASE()
    AND table_name = 'zona_delivery'
    AND column_name = 'mapa_embed_url'
);

SET @sql := IF(
  @col_exists = 0,
  'ALTER TABLE zona_delivery ADD COLUMN mapa_embed_url TEXT NULL AFTER radio_km',
  'SELECT 1'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
