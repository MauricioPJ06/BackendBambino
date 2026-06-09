
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
DROP TABLE IF EXISTS `auditoria_evento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `auditoria_evento` (
  `id_evento` bigint unsigned NOT NULL AUTO_INCREMENT,
  `entidad` varchar(80) NOT NULL,
  `entidad_id` varchar(80) NOT NULL,
  `accion` varchar(80) NOT NULL,
  `actor_tipo` enum('CLIENTE','COCINA','ADMIN') DEFAULT NULL,
  `id_actor` bigint unsigned DEFAULT NULL,
  `canal` enum('WEB','CHATBOT_WEB') DEFAULT NULL,
  `metadata_json` json DEFAULT NULL,
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_evento`),
  KEY `fk_aud_actor` (`id_actor`),
  CONSTRAINT `fk_aud_actor` FOREIGN KEY (`id_actor`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=147 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `carrito`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carrito` (
  `id_carrito` bigint unsigned NOT NULL AUTO_INCREMENT,
  `id_cliente` bigint unsigned NOT NULL,
  `estado` enum('ABIERTO','CONFIRMADO','ABANDONADO','CONVERTIDO') NOT NULL DEFAULT 'ABIERTO',
  `canal` enum('WEB','CHATBOT_WEB') NOT NULL DEFAULT 'WEB',
  `usuario_creacion` bigint unsigned DEFAULT NULL,
  `usuario_actualizacion` bigint unsigned DEFAULT NULL,
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_carrito`),
  KEY `fk_carrito_cliente` (`id_cliente`),
  KEY `fk_carrito_usuario_creacion` (`usuario_creacion`),
  KEY `fk_carrito_usuario_actualizacion` (`usuario_actualizacion`),
  CONSTRAINT `fk_carrito_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `cliente_perfil` (`id_cliente`),
  CONSTRAINT `fk_carrito_usuario_actualizacion` FOREIGN KEY (`usuario_actualizacion`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `fk_carrito_usuario_creacion` FOREIGN KEY (`usuario_creacion`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `carrito_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carrito_item` (
  `id_carrito_item` bigint unsigned NOT NULL AUTO_INCREMENT,
  `id_carrito` bigint unsigned NOT NULL,
  `id_producto` bigint unsigned NOT NULL,
  `cantidad` decimal(10,3) NOT NULL,
  `precio_unitario_snapshot` decimal(10,2) NOT NULL,
  `descuento_unitario_snapshot` decimal(10,2) NOT NULL DEFAULT '0.00',
  `observacion` varchar(220) DEFAULT NULL,
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_carrito_item`),
  UNIQUE KEY `uq_carrito_item` (`id_carrito`,`id_producto`),
  KEY `fk_carrito_item_producto` (`id_producto`),
  CONSTRAINT `fk_carrito_item_carrito` FOREIGN KEY (`id_carrito`) REFERENCES `carrito` (`id_carrito`) ON DELETE CASCADE,
  CONSTRAINT `fk_carrito_item_producto` FOREIGN KEY (`id_producto`) REFERENCES `producto` (`id_producto`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `categoria_producto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categoria_producto` (
  `id_categoria` bigint unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(120) NOT NULL,
  `descripcion` varchar(220) DEFAULT NULL,
  `orden_visual` int NOT NULL DEFAULT '0',
  `activa` tinyint(1) NOT NULL DEFAULT '1',
  `usuario_creacion` bigint unsigned DEFAULT NULL,
  `usuario_actualizacion` bigint unsigned DEFAULT NULL,
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_categoria`),
  UNIQUE KEY `nombre` (`nombre`),
  KEY `fk_categoria_usuario_creacion` (`usuario_creacion`),
  KEY `fk_categoria_usuario_actualizacion` (`usuario_actualizacion`),
  CONSTRAINT `fk_categoria_usuario_actualizacion` FOREIGN KEY (`usuario_actualizacion`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `fk_categoria_usuario_creacion` FOREIGN KEY (`usuario_creacion`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `cliente_direccion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cliente_direccion` (
  `id_direccion` bigint unsigned NOT NULL AUTO_INCREMENT,
  `id_cliente` bigint unsigned NOT NULL,
  `direccion_linea1` varchar(220) NOT NULL,
  `referencia` varchar(220) DEFAULT NULL,
  `distrito` varchar(120) DEFAULT NULL,
  `ciudad` varchar(120) DEFAULT NULL,
  `latitud` decimal(10,7) DEFAULT NULL,
  `longitud` decimal(10,7) DEFAULT NULL,
  `google_place_id` varchar(120) DEFAULT NULL,
  `google_plus_code` varchar(40) DEFAULT NULL,
  `es_principal` tinyint(1) NOT NULL DEFAULT '0',
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_direccion`),
  KEY `fk_direccion_cliente` (`id_cliente`),
  KEY `idx_direccion_geo` (`latitud`,`longitud`),
  CONSTRAINT `fk_direccion_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `cliente_perfil` (`id_cliente`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `cliente_documento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cliente_documento` (
  `id_documento` bigint unsigned NOT NULL AUTO_INCREMENT,
  `id_cliente` bigint unsigned NOT NULL,
  `doc_tipo` enum('DNI','RUC','CE') NOT NULL,
  `doc_numero` varchar(20) NOT NULL,
  `es_principal` tinyint(1) NOT NULL DEFAULT '0',
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_documento`),
  UNIQUE KEY `uq_cliente_documento` (`id_cliente`,`doc_tipo`,`doc_numero`),
  KEY `idx_cliente_documento_cliente` (`id_cliente`),
  KEY `idx_cliente_documento_principal` (`id_cliente`,`es_principal`,`activo`),
  CONSTRAINT `fk_cliente_documento_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `cliente_perfil` (`id_cliente`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `cliente_perfil`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cliente_perfil` (
  `id_cliente` bigint unsigned NOT NULL,
  `doc_tipo` enum('DNI','RUC','CE','OTRO') NOT NULL DEFAULT 'DNI',
  `doc_numero` varchar(20) DEFAULT NULL,
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_cliente`),
  CONSTRAINT `fk_cliente_usuario` FOREIGN KEY (`id_cliente`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `comprobante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comprobante` (
  `id_comprobante` bigint unsigned NOT NULL AUTO_INCREMENT,
  `id_empresa` bigint unsigned NOT NULL,
  `id_pedido` bigint unsigned NOT NULL,
  `tipo` enum('BOLETA','FACTURA') NOT NULL,
  `serie` varchar(10) NOT NULL,
  `correlativo` bigint unsigned NOT NULL,
  `numero_completo` varchar(30) NOT NULL,
  `estado` enum('EMITIDO','ANULADO','OBSERVADO') NOT NULL DEFAULT 'EMITIDO',
  `doc_receptor_tipo` enum('DNI','RUC','CE','OTRO') DEFAULT NULL,
  `doc_receptor_numero` varchar(20) DEFAULT NULL,
  `razon_social_receptor` varchar(200) DEFAULT NULL,
  `direccion_fiscal_receptor` varchar(300) DEFAULT NULL,
  `subtotal` decimal(12,2) NOT NULL,
  `impuesto_total` decimal(12,2) NOT NULL,
  `total` decimal(12,2) NOT NULL,
  `fecha_emision` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_creacion` bigint unsigned DEFAULT NULL,
  `usuario_actualizacion` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id_comprobante`),
  UNIQUE KEY `id_pedido` (`id_pedido`),
  UNIQUE KEY `numero_completo` (`numero_completo`),
  KEY `fk_comprobante_empresa` (`id_empresa`),
  KEY `fk_comprobante_usuario_creacion` (`usuario_creacion`),
  KEY `fk_comprobante_usuario_actualizacion` (`usuario_actualizacion`),
  CONSTRAINT `fk_comprobante_empresa` FOREIGN KEY (`id_empresa`) REFERENCES `empresa` (`id_empresa`),
  CONSTRAINT `fk_comprobante_pedido` FOREIGN KEY (`id_pedido`) REFERENCES `pedido` (`id_pedido`) ON DELETE CASCADE,
  CONSTRAINT `fk_comprobante_usuario_actualizacion` FOREIGN KEY (`usuario_actualizacion`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `fk_comprobante_usuario_creacion` FOREIGN KEY (`usuario_creacion`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `comprobante_detalle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comprobante_detalle` (
  `id_comprobante_detalle` bigint unsigned NOT NULL AUTO_INCREMENT,
  `id_comprobante` bigint unsigned NOT NULL,
  `id_pedido_item` bigint unsigned DEFAULT NULL,
  `descripcion_item` varchar(220) NOT NULL,
  `cantidad` decimal(10,3) NOT NULL,
  `precio_unitario` decimal(10,2) NOT NULL,
  `descuento_unitario` decimal(10,2) NOT NULL DEFAULT '0.00',
  `subtotal_linea` decimal(12,2) NOT NULL,
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_comprobante_detalle`),
  KEY `fk_comp_det_pedido_item` (`id_pedido_item`),
  KEY `idx_comp_det_comp` (`id_comprobante`),
  CONSTRAINT `fk_comp_det_comprobante` FOREIGN KEY (`id_comprobante`) REFERENCES `comprobante` (`id_comprobante`) ON DELETE CASCADE,
  CONSTRAINT `fk_comp_det_pedido_item` FOREIGN KEY (`id_pedido_item`) REFERENCES `pedido_item` (`id_pedido_item`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `configuracion_global`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `configuracion_global` (
  `id_config` tinyint unsigned NOT NULL,
  `moneda` varchar(10) NOT NULL DEFAULT 'PEN',
  `igv_porcentaje` decimal(5,2) NOT NULL DEFAULT '18.00',
  `delivery_monto_minimo` decimal(10,2) NOT NULL DEFAULT '0.00',
  `delivery_tiempo_min_minutos` int NOT NULL DEFAULT '20',
  `delivery_tiempo_max_minutos` int NOT NULL DEFAULT '60',
  `timezone` varchar(60) NOT NULL DEFAULT 'America/Lima',
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_config`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `configuracion_media`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `configuracion_media` (
  `id_media` bigint unsigned NOT NULL AUTO_INCREMENT,
  `clave` varchar(80) NOT NULL,
  `nombre` varchar(160) NOT NULL,
  `descripcion` varchar(220) DEFAULT NULL,
  `tipo` enum('IMAGEN','PDF','VIDEO') NOT NULL DEFAULT 'IMAGEN',
  `url` text NOT NULL,
  `public_id` varchar(220) DEFAULT NULL,
  `version_tag` varchar(120) DEFAULT NULL,
  `activa` tinyint(1) NOT NULL DEFAULT '1',
  `usuario_creacion` bigint unsigned DEFAULT NULL,
  `usuario_actualizacion` bigint unsigned DEFAULT NULL,
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_media`),
  UNIQUE KEY `uq_config_media_clave` (`clave`),
  KEY `fk_config_media_usuario_creacion` (`usuario_creacion`),
  KEY `fk_config_media_usuario_actualizacion` (`usuario_actualizacion`),
  CONSTRAINT `fk_config_media_usuario_actualizacion` FOREIGN KEY (`usuario_actualizacion`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `fk_config_media_usuario_creacion` FOREIGN KEY (`usuario_creacion`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `empresa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `empresa` (
  `id_empresa` bigint unsigned NOT NULL AUTO_INCREMENT,
  `ruc` varchar(20) NOT NULL,
  `razon_social` varchar(220) NOT NULL,
  `nombre_comercial` varchar(220) DEFAULT NULL,
  `direccion_fiscal` varchar(300) NOT NULL,
  `telefono` varchar(30) DEFAULT NULL,
  `correo` varchar(190) DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_empresa`),
  UNIQUE KEY `ruc` (`ruc`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `libro_reclamaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `libro_reclamaciones` (
  `id_reclamo` bigint unsigned NOT NULL AUTO_INCREMENT,
  `numero_reclamo` varchar(40) NOT NULL,
  `id_empresa` bigint unsigned NOT NULL,
  `id_cliente` bigint unsigned DEFAULT NULL,
  `id_pedido` bigint unsigned DEFAULT NULL,
  `codigo_pedido` varchar(40) DEFAULT NULL,
  `tipo_registro` enum('RECLAMO','QUEJA') NOT NULL,
  `consumidor_nombres` varchar(120) NOT NULL,
  `consumidor_apellidos` varchar(120) NOT NULL,
  `doc_tipo` enum('DNI','RUC','CE','OTRO') NOT NULL,
  `doc_numero` varchar(20) NOT NULL,
  `correo` varchar(190) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `direccion_consumidor` varchar(300) DEFAULT NULL,
  `fecha_consumo` datetime DEFAULT NULL,
  `monto_reclamado` decimal(10,2) DEFAULT NULL,
  `detalle_hechos` text NOT NULL,
  `pedido_consumidor` text NOT NULL,
  `estado` varchar(20) NOT NULL,
  `fecha_registro` datetime NOT NULL,
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_reclamo`),
  UNIQUE KEY `uq_libro_reclamaciones_numero` (`numero_reclamo`),
  KEY `fk_libro_reclamaciones_empresa` (`id_empresa`),
  KEY `idx_libro_reclamaciones_fecha` (`fecha_registro`),
  KEY `idx_libro_reclamaciones_cliente` (`id_cliente`),
  KEY `idx_libro_reclamaciones_pedido` (`id_pedido`),
  CONSTRAINT `fk_libro_reclamaciones_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `cliente_perfil` (`id_cliente`),
  CONSTRAINT `fk_libro_reclamaciones_empresa` FOREIGN KEY (`id_empresa`) REFERENCES `empresa` (`id_empresa`),
  CONSTRAINT `fk_libro_reclamaciones_pedido` FOREIGN KEY (`id_pedido`) REFERENCES `pedido` (`id_pedido`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `oferta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `oferta` (
  `id_oferta` bigint unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(180) NOT NULL,
  `tipo` enum('PORCENTAJE','MONTO_FIJO','PRECIO_ESPECIAL','COMBO') NOT NULL,
  `valor_descuento` decimal(10,2) DEFAULT NULL,
  `precio_especial` decimal(10,2) DEFAULT NULL,
  `estado` enum('BORRADOR','PROGRAMADA','ACTIVA','INACTIVA','EXPIRADA') NOT NULL DEFAULT 'BORRADOR',
  `fecha_inicio` datetime DEFAULT NULL,
  `fecha_fin` datetime DEFAULT NULL,
  `usuario_creacion` bigint unsigned DEFAULT NULL,
  `usuario_actualizacion` bigint unsigned DEFAULT NULL,
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_oferta`),
  KEY `fk_oferta_usuario_creacion` (`usuario_creacion`),
  KEY `fk_oferta_usuario_actualizacion` (`usuario_actualizacion`),
  CONSTRAINT `fk_oferta_usuario_actualizacion` FOREIGN KEY (`usuario_actualizacion`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `fk_oferta_usuario_creacion` FOREIGN KEY (`usuario_creacion`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `oferta_producto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `oferta_producto` (
  `id_oferta_producto` bigint unsigned NOT NULL AUTO_INCREMENT,
  `id_oferta` bigint unsigned NOT NULL,
  `id_producto` bigint unsigned NOT NULL,
  PRIMARY KEY (`id_oferta_producto`),
  UNIQUE KEY `uq_oferta_producto` (`id_oferta`,`id_producto`),
  KEY `fk_oferta_producto_producto` (`id_producto`),
  CONSTRAINT `fk_oferta_producto_oferta` FOREIGN KEY (`id_oferta`) REFERENCES `oferta` (`id_oferta`) ON DELETE CASCADE,
  CONSTRAINT `fk_oferta_producto_producto` FOREIGN KEY (`id_producto`) REFERENCES `producto` (`id_producto`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `pago`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pago` (
  `id_pago` bigint unsigned NOT NULL AUTO_INCREMENT,
  `id_pedido` bigint unsigned NOT NULL,
  `metodo` enum('TARJETA','YAPE','PLIN','TRANSFERENCIA','EFECTIVO','OTRO') NOT NULL,
  `estado` enum('INICIADO','PENDIENTE','APROBADO','RECHAZADO','ANULADO','EXPIRADO') NOT NULL DEFAULT 'INICIADO',
  `monto` decimal(12,2) NOT NULL,
  `proveedor` varchar(80) DEFAULT NULL,
  `proveedor_txn_id` varchar(120) DEFAULT NULL,
  `idempotency_key` char(36) NOT NULL,
  `url_pago` text,
  `usuario_creacion` bigint unsigned DEFAULT NULL,
  `usuario_actualizacion` bigint unsigned DEFAULT NULL,
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_pago`),
  UNIQUE KEY `uq_pago_idempotency` (`idempotency_key`),
  KEY `fk_pago_usuario_creacion` (`usuario_creacion`),
  KEY `fk_pago_usuario_actualizacion` (`usuario_actualizacion`),
  KEY `idx_pago_pedido_estado` (`id_pedido`,`estado`),
  CONSTRAINT `fk_pago_pedido` FOREIGN KEY (`id_pedido`) REFERENCES `pedido` (`id_pedido`) ON DELETE CASCADE,
  CONSTRAINT `fk_pago_usuario_actualizacion` FOREIGN KEY (`usuario_actualizacion`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `fk_pago_usuario_creacion` FOREIGN KEY (`usuario_creacion`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `pedido`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedido` (
  `id_pedido` bigint unsigned NOT NULL AUTO_INCREMENT,
  `codigo_pedido` varchar(40) NOT NULL,
  `id_cliente` bigint unsigned NOT NULL,
  `canal_origen` enum('WEB','CHATBOT_WEB') NOT NULL,
  `modalidad` enum('RECOJO','DELIVERY') NOT NULL,
  `estado_actual` enum('CREADO','PAGO_PENDIENTE','PAGO_RECHAZADO','PAGO_APROBADO','CONFIRMADO','EN_PREPARACION','LISTO_RECOJO','LISTO_DESPACHO','EN_CAMINO','ENTREGADO','CANCELADO','ANULADO') NOT NULL,
  `tipo_comprobante` enum('BOLETA','FACTURA') NOT NULL,
  `id_direccion_entrega` bigint unsigned DEFAULT NULL,
  `latitud_entrega_snapshot` decimal(10,7) DEFAULT NULL,
  `longitud_entrega_snapshot` decimal(10,7) DEFAULT NULL,
  `google_place_id_entrega_snapshot` varchar(120) DEFAULT NULL,
  `direccion_formateada_snapshot` varchar(300) DEFAULT NULL,
  `costo_delivery` decimal(10,2) NOT NULL DEFAULT '0.00',
  `cliente_doc_tipo_snapshot` enum('DNI','RUC','CE','OTRO') DEFAULT NULL,
  `cliente_doc_numero_snapshot` varchar(20) DEFAULT NULL,
  `razon_social_snapshot` varchar(200) DEFAULT NULL,
  `direccion_fiscal_snapshot` varchar(300) DEFAULT NULL,
  `subtotal` decimal(12,2) NOT NULL,
  `descuento_total` decimal(12,2) NOT NULL DEFAULT '0.00',
  `impuesto_total` decimal(12,2) NOT NULL DEFAULT '0.00',
  `total` decimal(12,2) NOT NULL,
  `usuario_cocina_preparacion` bigint unsigned DEFAULT NULL,
  `fecha_inicio_preparacion` datetime DEFAULT NULL,
  `fecha_fin_preparacion` datetime DEFAULT NULL,
  `usuario_creacion` bigint unsigned DEFAULT NULL,
  `usuario_actualizacion` bigint unsigned DEFAULT NULL,
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_pedido`),
  UNIQUE KEY `codigo_pedido` (`codigo_pedido`),
  KEY `fk_pedido_direccion` (`id_direccion_entrega`),
  KEY `fk_pedido_usuario_cocina_preparacion` (`usuario_cocina_preparacion`),
  KEY `fk_pedido_usuario_creacion` (`usuario_creacion`),
  KEY `fk_pedido_usuario_actualizacion` (`usuario_actualizacion`),
  KEY `idx_pedido_estado_fecha` (`estado_actual`,`fecha_creacion`),
  KEY `idx_pedido_cliente_fecha` (`id_cliente`,`fecha_creacion`),
  CONSTRAINT `fk_pedido_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `cliente_perfil` (`id_cliente`),
  CONSTRAINT `fk_pedido_direccion` FOREIGN KEY (`id_direccion_entrega`) REFERENCES `cliente_direccion` (`id_direccion`),
  CONSTRAINT `fk_pedido_usuario_actualizacion` FOREIGN KEY (`usuario_actualizacion`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `fk_pedido_usuario_cocina_preparacion` FOREIGN KEY (`usuario_cocina_preparacion`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `fk_pedido_usuario_creacion` FOREIGN KEY (`usuario_creacion`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `pedido_asignacion_cocina`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedido_asignacion_cocina` (
  `id_asignacion` bigint unsigned NOT NULL AUTO_INCREMENT,
  `id_pedido` bigint unsigned NOT NULL,
  `id_cocina_actual` bigint unsigned DEFAULT NULL,
  `id_cocina_anterior` bigint unsigned DEFAULT NULL,
  `motivo_reasignacion` varchar(220) DEFAULT NULL,
  `usuario_creacion` bigint unsigned DEFAULT NULL,
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `id_usuario_cocina` bigint unsigned DEFAULT NULL,
  `fecha_asignacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `motivo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_asignacion`),
  KEY `fk_asig_cocina_actual` (`id_cocina_actual`),
  KEY `fk_asig_cocina_anterior` (`id_cocina_anterior`),
  KEY `fk_asig_usuario_creacion` (`usuario_creacion`),
  KEY `fk_asignacion_usuario_cocina` (`id_usuario_cocina`),
  KEY `idx_asignacion_cocina_pedido_fecha` (`id_pedido`,`fecha_asignacion`),
  CONSTRAINT `fk_asig_cocina_actual` FOREIGN KEY (`id_cocina_actual`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `fk_asig_cocina_anterior` FOREIGN KEY (`id_cocina_anterior`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `fk_asig_pedido` FOREIGN KEY (`id_pedido`) REFERENCES `pedido` (`id_pedido`) ON DELETE CASCADE,
  CONSTRAINT `fk_asig_usuario_creacion` FOREIGN KEY (`usuario_creacion`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `fk_asignacion_usuario_cocina` FOREIGN KEY (`id_usuario_cocina`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `pedido_cocina_incidencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedido_cocina_incidencia` (
  `id_incidencia` bigint unsigned NOT NULL AUTO_INCREMENT,
  `id_pedido` bigint unsigned NOT NULL,
  `id_usuario_cocina` bigint unsigned NOT NULL,
  `tipo_incidencia` varchar(40) NOT NULL,
  `detalle` varchar(255) NOT NULL,
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_incidencia`),
  KEY `fk_cocina_incidencia_usuario` (`id_usuario_cocina`),
  KEY `idx_cocina_incidencia_pedido_fecha` (`id_pedido`,`fecha_creacion`),
  CONSTRAINT `fk_cocina_incidencia_pedido` FOREIGN KEY (`id_pedido`) REFERENCES `pedido` (`id_pedido`) ON DELETE CASCADE,
  CONSTRAINT `fk_cocina_incidencia_usuario` FOREIGN KEY (`id_usuario_cocina`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `pedido_estado_historial`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedido_estado_historial` (
  `id_historial` bigint unsigned NOT NULL AUTO_INCREMENT,
  `id_pedido` bigint unsigned NOT NULL,
  `estado_anterior` enum('CREADO','PAGO_PENDIENTE','PAGO_RECHAZADO','PAGO_APROBADO','CONFIRMADO','EN_PREPARACION','LISTO_RECOJO','LISTO_DESPACHO','EN_CAMINO','ENTREGADO','CANCELADO','ANULADO') DEFAULT NULL,
  `estado_nuevo` enum('CREADO','PAGO_PENDIENTE','PAGO_RECHAZADO','PAGO_APROBADO','CONFIRMADO','EN_PREPARACION','LISTO_RECOJO','LISTO_DESPACHO','EN_CAMINO','ENTREGADO','CANCELADO','ANULADO') NOT NULL,
  `actor_tipo` enum('CLIENTE','COCINA','ADMIN') DEFAULT NULL,
  `id_actor` bigint unsigned DEFAULT NULL,
  `canal` enum('WEB','CHATBOT_WEB') DEFAULT NULL,
  `motivo` varchar(240) DEFAULT NULL,
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_historial`),
  KEY `fk_hist_pedido` (`id_pedido`),
  KEY `fk_hist_actor` (`id_actor`),
  CONSTRAINT `fk_hist_actor` FOREIGN KEY (`id_actor`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `fk_hist_pedido` FOREIGN KEY (`id_pedido`) REFERENCES `pedido` (`id_pedido`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `pedido_estado_transicion_permitida`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedido_estado_transicion_permitida` (
  `id_transicion` bigint unsigned NOT NULL AUTO_INCREMENT,
  `estado_origen` enum('CREADO','PAGO_PENDIENTE','PAGO_RECHAZADO','PAGO_APROBADO','CONFIRMADO','EN_PREPARACION','LISTO_RECOJO','LISTO_DESPACHO','EN_CAMINO','ENTREGADO','CANCELADO','ANULADO') NOT NULL,
  `estado_destino` enum('CREADO','PAGO_PENDIENTE','PAGO_RECHAZADO','PAGO_APROBADO','CONFIRMADO','EN_PREPARACION','LISTO_RECOJO','LISTO_DESPACHO','EN_CAMINO','ENTREGADO','CANCELADO','ANULADO') NOT NULL,
  `rol_habilitado` enum('CLIENTE','COCINA','ADMIN') NOT NULL,
  `activa` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id_transicion`),
  UNIQUE KEY `uq_transicion` (`estado_origen`,`estado_destino`,`rol_habilitado`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `pedido_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedido_item` (
  `id_pedido_item` bigint unsigned NOT NULL AUTO_INCREMENT,
  `id_pedido` bigint unsigned NOT NULL,
  `id_producto` bigint unsigned DEFAULT NULL,
  `nombre_producto_snapshot` varchar(180) NOT NULL,
  `cantidad` decimal(10,3) NOT NULL,
  `precio_unitario` decimal(10,2) NOT NULL,
  `descuento_unitario` decimal(10,2) NOT NULL DEFAULT '0.00',
  `subtotal_linea` decimal(12,2) NOT NULL,
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_pedido_item`),
  KEY `fk_pedido_item_pedido` (`id_pedido`),
  KEY `fk_pedido_item_producto` (`id_producto`),
  CONSTRAINT `fk_pedido_item_pedido` FOREIGN KEY (`id_pedido`) REFERENCES `pedido` (`id_pedido`) ON DELETE CASCADE,
  CONSTRAINT `fk_pedido_item_producto` FOREIGN KEY (`id_producto`) REFERENCES `producto` (`id_producto`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `producto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `producto` (
  `id_producto` bigint unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(160) NOT NULL,
  `descripcion` text,
  `id_categoria` bigint unsigned DEFAULT NULL,
  `precio_base` decimal(10,2) NOT NULL,
  `visible_web` tinyint(1) NOT NULL DEFAULT '1',
  `disponible` tinyint(1) NOT NULL DEFAULT '1',
  `estado` enum('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  `imagen_url` text,
  `orden_visual` int NOT NULL DEFAULT '0',
  `usuario_creacion` bigint unsigned DEFAULT NULL,
  `usuario_actualizacion` bigint unsigned DEFAULT NULL,
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_producto`),
  KEY `fk_producto_categoria` (`id_categoria`),
  KEY `fk_producto_usuario_creacion` (`usuario_creacion`),
  KEY `fk_producto_usuario_actualizacion` (`usuario_actualizacion`),
  CONSTRAINT `fk_producto_categoria` FOREIGN KEY (`id_categoria`) REFERENCES `categoria_producto` (`id_categoria`),
  CONSTRAINT `fk_producto_usuario_actualizacion` FOREIGN KEY (`usuario_actualizacion`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `fk_producto_usuario_creacion` FOREIGN KEY (`usuario_creacion`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `recuperacion_password_codigo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recuperacion_password_codigo` (
  `id_codigo` bigint unsigned NOT NULL AUTO_INCREMENT,
  `id_usuario` bigint unsigned NOT NULL,
  `correo` varchar(190) NOT NULL,
  `codigo` varchar(10) DEFAULT NULL,
  `codigo_hash` varchar(255) DEFAULT NULL,
  `estado` enum('PENDIENTE','USADO','EXPIRADO') NOT NULL DEFAULT 'PENDIENTE',
  `fecha_expiracion` datetime NOT NULL,
  `intentos_fallidos` int NOT NULL DEFAULT '0',
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_codigo`),
  KEY `fk_recuperacion_usuario` (`id_usuario`),
  KEY `idx_recuperacion_correo_estado` (`correo`,`estado`),
  KEY `idx_recuperacion_correo_estado_expiracion` (`correo`,`estado`,`fecha_expiracion`),
  CONSTRAINT `fk_recuperacion_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `rol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rol` (
  `id_rol` smallint unsigned NOT NULL AUTO_INCREMENT,
  `nombre` enum('CLIENTE','COCINA','ADMIN') NOT NULL,
  `descripcion` varchar(120) DEFAULT NULL,
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_rol`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `serie_comprobante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `serie_comprobante` (
  `id_serie` bigint unsigned NOT NULL AUTO_INCREMENT,
  `id_empresa` bigint unsigned NOT NULL,
  `tipo_comprobante` enum('BOLETA','FACTURA') NOT NULL,
  `serie` varchar(10) NOT NULL,
  `correlativo_actual` bigint unsigned NOT NULL DEFAULT '0',
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_serie`),
  UNIQUE KEY `uq_serie_empresa_tipo` (`id_empresa`,`tipo_comprobante`,`serie`),
  CONSTRAINT `fk_serie_empresa` FOREIGN KEY (`id_empresa`) REFERENCES `empresa` (`id_empresa`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `id_usuario` bigint unsigned NOT NULL AUTO_INCREMENT,
  `uuid_usuario` char(36) NOT NULL,
  `email` varchar(190) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `nombres` varchar(120) NOT NULL,
  `apellidos` varchar(120) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `documento` varchar(20) NOT NULL,
  `estado` enum('ACTIVO','INACTIVO','BLOQUEADO') NOT NULL DEFAULT 'ACTIVO',
  `id_rol` smallint unsigned NOT NULL,
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `uuid_usuario` (`uuid_usuario`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `uk_usuario_documento` (`documento`),
  KEY `fk_usuario_rol` (`id_rol`),
  CONSTRAINT `fk_usuario_rol` FOREIGN KEY (`id_rol`) REFERENCES `rol` (`id_rol`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `zona_delivery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `zona_delivery` (
  `id_zona` bigint unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(120) NOT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `tarifa_base` decimal(10,2) NOT NULL DEFAULT '0.00',
  `monto_minimo` decimal(10,2) NOT NULL DEFAULT '0.00',
  `tiempo_estimado_minutos` int NOT NULL DEFAULT '35',
  `cobertura_descripcion` varchar(300) DEFAULT NULL,
  `latitud_centro` decimal(10,7) DEFAULT NULL,
  `longitud_centro` decimal(10,7) DEFAULT NULL,
  `radio_km` decimal(10,2) DEFAULT NULL,
  `mapa_embed_url` text,
  `usuario_creacion` bigint unsigned DEFAULT NULL,
  `usuario_actualizacion` bigint unsigned DEFAULT NULL,
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `hora_inicio_atencion` time DEFAULT NULL,
  `hora_fin_atencion` time DEFAULT NULL,
  PRIMARY KEY (`id_zona`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
--
-- WARNING: can't read the INFORMATION_SCHEMA.libraries table. It's most probably an old server 8.4.9.
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
