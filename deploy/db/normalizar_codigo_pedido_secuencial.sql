-- Normaliza codigos de pedidos al formato PED-000000001.
-- Usa 9 digitos correlativos basados en id_pedido.

UPDATE pedido
SET codigo_pedido = CONCAT('TMP-', LPAD(id_pedido, 9, '0'));

UPDATE pedido
SET codigo_pedido = CONCAT('PED-', LPAD(id_pedido, 9, '0'));
