ALTER TABLE comprobante
    ADD COLUMN IF NOT EXISTS correo_enviado BOOLEAN NOT NULL DEFAULT FALSE AFTER fecha_emision,
    ADD COLUMN IF NOT EXISTS correo_destino VARCHAR(190) NULL AFTER correo_enviado,
    ADD COLUMN IF NOT EXISTS fecha_correo_envio DATETIME NULL AFTER correo_destino,
    ADD COLUMN IF NOT EXISTS correo_error VARCHAR(500) NULL AFTER fecha_correo_envio;
