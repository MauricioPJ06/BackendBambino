-- Corrige permisos runtime del usuario usado por el backend local/VPS.
-- Ejecutar con un usuario administrador de MySQL contra el servidor del VPS.
-- No incluye contrasenas ni crea usuarios; solo ajusta permisos del usuario existente.

GRANT SELECT, INSERT, UPDATE, DELETE
ON bambino_db.*
TO 'integrador'@'172.20.0.1';

FLUSH PRIVILEGES;

SHOW GRANTS FOR 'integrador'@'172.20.0.1';
