START TRANSACTION;

-- Series por defecto:
-- BOLETA  -> B001, genera numeros como B001-000000001
-- FACTURA -> F001, genera numeros como F001-000000001
-- El correlativo se guarda numerico; el backend lo imprime con 9 digitos.

SET @id_empresa := (
  SELECT id_empresa
  FROM empresa
  WHERE activo = 1
  ORDER BY id_empresa ASC
  LIMIT 1
);

INSERT INTO serie_comprobante (
  id_empresa,
  tipo_comprobante,
  serie,
  correlativo_actual,
  activo,
  fecha_creacion,
  fecha_actualizacion
)
SELECT @id_empresa, 'BOLETA', 'B001', 0, 1, NOW(), NOW()
WHERE @id_empresa IS NOT NULL
  AND NOT EXISTS (
    SELECT 1
    FROM serie_comprobante
    WHERE id_empresa = @id_empresa
      AND tipo_comprobante = 'BOLETA'
      AND serie = 'B001'
  );

UPDATE serie_comprobante
SET activo = 1,
    fecha_actualizacion = NOW()
WHERE @id_empresa IS NOT NULL
  AND id_empresa = @id_empresa
  AND tipo_comprobante = 'BOLETA'
  AND serie = 'B001';

INSERT INTO serie_comprobante (
  id_empresa,
  tipo_comprobante,
  serie,
  correlativo_actual,
  activo,
  fecha_creacion,
  fecha_actualizacion
)
SELECT @id_empresa, 'FACTURA', 'F001', 0, 1, NOW(), NOW()
WHERE @id_empresa IS NOT NULL
  AND NOT EXISTS (
    SELECT 1
    FROM serie_comprobante
    WHERE id_empresa = @id_empresa
      AND tipo_comprobante = 'FACTURA'
      AND serie = 'F001'
  );

UPDATE serie_comprobante
SET activo = 1,
    fecha_actualizacion = NOW()
WHERE @id_empresa IS NOT NULL
  AND id_empresa = @id_empresa
  AND tipo_comprobante = 'FACTURA'
  AND serie = 'F001';

COMMIT;
