UPDATE zona_delivery
SET radio_km = 10.00,
    fecha_actualizacion = NOW()
WHERE activo = 1;
