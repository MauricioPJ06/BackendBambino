ALTER TABLE comprobante
    ADD COLUMN IF NOT EXISTS pdf_path VARCHAR(300) NULL AFTER correo_error,
    ADD COLUMN IF NOT EXISTS pdf_token VARCHAR(80) NULL AFTER pdf_path,
    ADD COLUMN IF NOT EXISTS fecha_pdf_generado DATETIME NULL AFTER pdf_token;

SET @idx_exists := (
    SELECT COUNT(1)
    FROM information_schema.statistics
    WHERE table_schema = DATABASE()
      AND table_name = 'comprobante'
      AND index_name = 'uq_comprobante_pdf_token'
);

SET @sql := IF(
    @idx_exists = 0,
    'CREATE UNIQUE INDEX uq_comprobante_pdf_token ON comprobante (pdf_token)',
    'SELECT 1'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
