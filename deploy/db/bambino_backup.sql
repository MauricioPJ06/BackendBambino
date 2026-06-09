-- MySQL dump 10.13  Distrib 9.5.0, for macos26.1 (arm64)
--
-- Host: viaduct.proxy.rlwy.net    Database: bambino_db
-- ------------------------------------------------------
-- Server version	9.4.0

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

--
-- Table structure for table `auditoria_evento`
--

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
) ENGINE=InnoDB AUTO_INCREMENT=144 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auditoria_evento`
--

LOCK TABLES `auditoria_evento` WRITE;
/*!40000 ALTER TABLE `auditoria_evento` DISABLE KEYS */;
INSERT INTO `auditoria_evento` VALUES (1,'USUARIO','3','REGISTRO_CLIENTE','CLIENTE',3,'WEB','{\"email\": \"kevinespiritu_154@gmail.com\"}','2026-05-09 02:55:02'),(2,'USUARIO','4','REGISTRO_CLIENTE','CLIENTE',4,'WEB','{\"email\": \"kevinespiritu16@gmail.com\"}','2026-05-09 03:08:22'),(3,'USUARIO','5','REGISTRO_CLIENTE','CLIENTE',5,'WEB','{\"email\": \"kevinespiritu1617@gmail.com\"}','2026-05-09 03:11:37'),(4,'USUARIO','6','REGISTRO_CLIENTE','CLIENTE',6,'WEB','{\"email\": \"kevinespiritu154@gmail.com\"}','2026-05-09 03:12:39'),(5,'SEGURIDAD','6','SOLICITAR_RECUPERACION_PASSWORD','CLIENTE',6,'WEB','{\"email\": \"kevinespiritu154@gmail.com\"}','2026-05-09 03:24:24'),(6,'SEGURIDAD','4','SOLICITAR_RECUPERACION_PASSWORD','CLIENTE',4,'WEB','{\"email\": \"kevinespiritu16@gmail.com\"}','2026-05-09 03:25:07'),(7,'SEGURIDAD','4','SOLICITAR_RECUPERACION_PASSWORD','CLIENTE',4,'WEB','{\"email\": \"kevinespiritu16@gmail.com\"}','2026-05-09 03:26:57'),(8,'SEGURIDAD','4','SOLICITAR_RECUPERACION_PASSWORD','CLIENTE',4,'WEB','{\"email\": \"kevinespiritu16@gmail.com\"}','2026-05-09 03:29:30'),(9,'SEGURIDAD','4','SOLICITAR_RECUPERACION_PASSWORD','CLIENTE',4,'WEB','{\"email\": \"kevinespiritu16@gmail.com\"}','2026-05-09 03:29:42'),(10,'SEGURIDAD','4','SOLICITAR_RECUPERACION_PASSWORD','CLIENTE',4,'WEB','{\"email\": \"kevinespiritu16@gmail.com\"}','2026-05-09 03:33:43'),(11,'SEGURIDAD','4','SOLICITAR_RECUPERACION_PASSWORD','CLIENTE',4,'WEB','{\"email\": \"kevinespiritu16@gmail.com\"}','2026-05-09 03:35:41'),(12,'SEGURIDAD','4','SOLICITAR_RECUPERACION_PASSWORD','CLIENTE',4,'WEB','{\"email\": \"kevinespiritu16@gmail.com\"}','2026-05-09 03:40:26'),(13,'SEGURIDAD','4','SOLICITAR_RECUPERACION_PASSWORD','CLIENTE',4,'WEB','{\"email\": \"kevinespiritu16@gmail.com\"}','2026-05-09 09:19:06'),(14,'SEGURIDAD','4','SOLICITAR_RECUPERACION_PASSWORD','CLIENTE',4,'WEB','{\"email\": \"kevinespiritu16@gmail.com\"}','2026-05-09 09:19:52'),(15,'SEGURIDAD','4','SOLICITAR_RECUPERACION_PASSWORD','CLIENTE',4,'WEB','{\"email\": \"kevinespiritu16@gmail.com\"}','2026-05-09 09:23:49'),(16,'SEGURIDAD','4','SOLICITAR_RECUPERACION_PASSWORD','CLIENTE',4,'WEB','{\"email\": \"kevinespiritu16@gmail.com\"}','2026-05-09 09:25:52'),(17,'SEGURIDAD','4','CONFIRMAR_RECUPERACION_PASSWORD','CLIENTE',4,'WEB','{\"email\": \"kevinespiritu16@gmail.com\"}','2026-05-09 09:27:27'),(18,'CATEGORIA_PRODUCTO','1','CREAR_CATEGORIA','ADMIN',7,'WEB','{\"nombre\": \"pollo\"}','2026-05-09 11:14:02'),(19,'CATEGORIA_PRODUCTO','1','ACTUALIZAR_CATEGORIA','ADMIN',7,'WEB','{\"nombre\": \"pollo2\"}','2026-05-09 11:15:44'),(20,'CATEGORIA_PRODUCTO','1','ACTUALIZAR_CATEGORIA','ADMIN',7,'WEB','{\"nombre\": \"pollo23\"}','2026-05-09 11:17:50'),(21,'CATEGORIA_PRODUCTO','1','ACTUALIZAR_CATEGORIA','ADMIN',7,'WEB','{\"nombre\": \"pollo23\"}','2026-05-09 11:18:32'),(22,'CATEGORIA_PRODUCTO','1','ACTUALIZAR_CATEGORIA','ADMIN',7,'WEB','{\"nombre\": \"pollo23\"}','2026-05-09 11:18:35'),(23,'CATEGORIA_PRODUCTO','1','ACTUALIZAR_CATEGORIA','ADMIN',7,'WEB','{\"nombre\": \"pollo23\"}','2026-05-09 11:18:38'),(24,'CATEGORIA_PRODUCTO','1','ACTUALIZAR_CATEGORIA','ADMIN',7,'WEB','{\"nombre\": \"pollo230\"}','2026-05-09 11:18:43'),(25,'CATEGORIA_PRODUCTO','1','ACTUALIZAR_CATEGORIA','ADMIN',7,'WEB','{\"nombre\": \"pollo230\"}','2026-05-09 11:25:02'),(26,'CATEGORIA_PRODUCTO','1','ACTUALIZAR_CATEGORIA','ADMIN',7,'WEB','{\"nombre\": \"pollo230\"}','2026-05-09 11:25:17'),(27,'CATEGORIA_PRODUCTO','1','ACTUALIZAR_CATEGORIA','ADMIN',7,'WEB','{\"nombre\": \"pollo230\"}','2026-05-09 11:25:27'),(28,'CATEGORIA_PRODUCTO','1','ACTUALIZAR_CATEGORIA','ADMIN',7,'WEB','{\"nombre\": \"pollo230\"}','2026-05-09 11:25:31'),(29,'CATEGORIA_PRODUCTO','1','ACTUALIZAR_CATEGORIA','ADMIN',7,'WEB','{\"nombre\": \"pollo230\"}','2026-05-09 11:26:10'),(30,'CATEGORIA_PRODUCTO','1','ACTUALIZAR_CATEGORIA','ADMIN',7,'WEB','{\"nombre\": \"pollo230\"}','2026-05-09 11:26:12'),(31,'CATEGORIA_PRODUCTO','1','ACTUALIZAR_CATEGORIA','ADMIN',7,'WEB','{\"nombre\": \"pollo230\"}','2026-05-09 11:26:21'),(32,'CATEGORIA_PRODUCTO','1','ACTUALIZAR_CATEGORIA','ADMIN',7,'WEB','{\"nombre\": \"pollo230\"}','2026-05-09 11:26:24'),(33,'CATEGORIA_PRODUCTO','1','ACTUALIZAR_CATEGORIA','ADMIN',7,'WEB','{\"nombre\": \"pollo230\"}','2026-05-09 11:26:31'),(34,'CATEGORIA_PRODUCTO','1','ACTUALIZAR_CATEGORIA','ADMIN',7,'WEB','{\"nombre\": \"pollo230\"}','2026-05-09 11:26:39'),(35,'CATEGORIA_PRODUCTO','1','ACTUALIZAR_CATEGORIA','ADMIN',7,'WEB','{\"nombre\": \"pollo230\"}','2026-05-09 11:39:41'),(36,'CATEGORIA_PRODUCTO','2','CREAR_CATEGORIA','ADMIN',7,'WEB','{\"nombre\": \"pollo23\"}','2026-05-09 11:46:28'),(37,'PRODUCTO','2','CREAR_PRODUCTO','ADMIN',7,'WEB','{\"nombre\": \"kevin\"}','2026-05-09 12:04:56'),(38,'PRODUCTO','1','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"producto test oferta\"}','2026-05-09 12:06:05'),(39,'CATEGORIA_PRODUCTO','1','ACTUALIZAR_CATEGORIA','ADMIN',7,'WEB','{\"nombre\": \"Familiares\"}','2026-05-09 12:10:18'),(40,'CATEGORIA_PRODUCTO','2','ACTUALIZAR_CATEGORIA','ADMIN',7,'WEB','{\"nombre\": \"Mostros\"}','2026-05-09 12:12:05'),(41,'CATEGORIA_PRODUCTO','3','CREAR_CATEGORIA','ADMIN',7,'WEB','{\"nombre\": \"Combos\"}','2026-05-09 12:12:36'),(42,'CATEGORIA_PRODUCTO','4','CREAR_CATEGORIA','ADMIN',7,'WEB','{\"nombre\": \"Fusión Oriental\"}','2026-05-09 12:12:48'),(43,'CATEGORIA_PRODUCTO','5','CREAR_CATEGORIA','ADMIN',7,'WEB','{\"nombre\": \"Platos a la Carta\"}','2026-05-09 12:13:03'),(44,'PRODUCTO','3','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"1/8 de Pollo\"}','2026-05-09 12:26:24'),(45,'PRODUCTO','4','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"1/4 de Pollo\"}','2026-05-09 12:26:26'),(46,'PRODUCTO','6','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"Mostro\"}','2026-05-09 12:26:30'),(47,'PRODUCTO','5','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"1/2 de Pollo\"}','2026-05-09 12:26:34'),(48,'CATEGORIA_PRODUCTO','BULK','ORDENAR_CATEGORIAS','ADMIN',7,'WEB','{\"cantidad\": 5}','2026-05-09 12:42:45'),(49,'CATEGORIA_PRODUCTO','BULK','ORDENAR_CATEGORIAS','ADMIN',7,'WEB','{\"cantidad\": 5}','2026-05-09 12:42:54'),(50,'PRODUCTO','BULK','ORDENAR_PRODUCTOS','ADMIN',7,'WEB','{\"cantidad\": 20}','2026-05-09 13:14:17'),(51,'CONFIGURACION_MEDIA','1','SUBIR_ARCHIVO_CONFIGURACION_MEDIA','ADMIN',7,'WEB','{\"clave\": \"HOME_HERO_BANNER\"}','2026-05-09 13:52:48'),(52,'CONFIGURACION_MEDIA','1','ACTUALIZAR_CONFIGURACION_MEDIA','ADMIN',7,'WEB','{\"tipo\": \"IMAGEN\", \"clave\": \"HOME_HERO_BANNER\"}','2026-05-09 13:56:55'),(53,'CONFIGURACION_MEDIA','1','ACTUALIZAR_CONFIGURACION_MEDIA','ADMIN',7,'WEB','{\"tipo\": \"IMAGEN\", \"clave\": \"HOME_HERO_BANNER\"}','2026-05-09 14:01:43'),(54,'CONFIGURACION_MEDIA','1','SUBIR_ARCHIVO_CONFIGURACION_MEDIA','ADMIN',7,'WEB','{\"clave\": \"HOME_HERO_BANNER\"}','2026-05-09 14:04:26'),(55,'CONFIGURACION_MEDIA','1','ACTUALIZAR_CONFIGURACION_MEDIA','ADMIN',7,'WEB','{\"tipo\": \"IMAGEN\", \"clave\": \"HOME_HERO_BANNER\"}','2026-05-09 14:04:26'),(56,'CONFIGURACION_MEDIA','1','SUBIR_ARCHIVO_CONFIGURACION_MEDIA','ADMIN',7,'WEB','{\"clave\": \"HOME_HERO_BANNER\"}','2026-05-09 14:13:26'),(57,'CONFIGURACION_MEDIA','1','ACTUALIZAR_CONFIGURACION_MEDIA','ADMIN',7,'WEB','{\"tipo\": \"IMAGEN\", \"clave\": \"HOME_HERO_BANNER\"}','2026-05-09 14:13:26'),(58,'CONFIGURACION_MEDIA','1','SUBIR_ARCHIVO_CONFIGURACION_MEDIA','ADMIN',7,'WEB','{\"clave\": \"HOME_HERO_BANNER\"}','2026-05-09 14:14:42'),(59,'CONFIGURACION_MEDIA','1','ACTUALIZAR_CONFIGURACION_MEDIA','ADMIN',7,'WEB','{\"tipo\": \"IMAGEN\", \"clave\": \"HOME_HERO_BANNER\"}','2026-05-09 14:14:42'),(60,'CONFIGURACION_MEDIA','1','SUBIR_ARCHIVO_CONFIGURACION_MEDIA','ADMIN',7,'WEB','{\"clave\": \"HOME_HERO_BANNER\"}','2026-05-09 14:17:48'),(61,'CONFIGURACION_MEDIA','1','ACTUALIZAR_CONFIGURACION_MEDIA','ADMIN',7,'WEB','{\"tipo\": \"IMAGEN\", \"clave\": \"HOME_HERO_BANNER\"}','2026-05-09 14:17:48'),(62,'CONFIGURACION_MEDIA','1','SUBIR_ARCHIVO_CONFIGURACION_MEDIA','ADMIN',7,'WEB','{\"clave\": \"HOME_HERO_BANNER\"}','2026-05-09 14:31:54'),(63,'CONFIGURACION_MEDIA','1','ACTUALIZAR_CONFIGURACION_MEDIA','ADMIN',7,'WEB','{\"tipo\": \"IMAGEN\", \"clave\": \"HOME_HERO_BANNER\"}','2026-05-09 14:31:54'),(64,'CONFIGURACION_MEDIA','1','SUBIR_ARCHIVO_CONFIGURACION_MEDIA','ADMIN',7,'WEB','{\"clave\": \"HOME_HERO_BANNER\"}','2026-05-09 14:32:23'),(65,'CONFIGURACION_MEDIA','1','ACTUALIZAR_CONFIGURACION_MEDIA','ADMIN',7,'WEB','{\"tipo\": \"IMAGEN\", \"clave\": \"HOME_HERO_BANNER\"}','2026-05-09 14:32:23'),(66,'CONFIGURACION_MEDIA','1','SUBIR_ARCHIVO_CONFIGURACION_MEDIA','ADMIN',7,'WEB','{\"clave\": \"HOME_HERO_BANNER\"}','2026-05-09 14:33:08'),(67,'CONFIGURACION_MEDIA','1','ACTUALIZAR_CONFIGURACION_MEDIA','ADMIN',7,'WEB','{\"tipo\": \"IMAGEN\", \"clave\": \"HOME_HERO_BANNER\"}','2026-05-09 14:33:08'),(68,'CONFIGURACION_MEDIA','1','SUBIR_ARCHIVO_CONFIGURACION_MEDIA','ADMIN',7,'WEB','{\"clave\": \"HOME_HERO_BANNER\"}','2026-05-09 14:38:51'),(69,'CONFIGURACION_MEDIA','1','ACTUALIZAR_CONFIGURACION_MEDIA','ADMIN',7,'WEB','{\"tipo\": \"IMAGEN\", \"clave\": \"HOME_HERO_BANNER\"}','2026-05-09 14:38:51'),(70,'CONFIGURACION_MEDIA','1','SUBIR_ARCHIVO_CONFIGURACION_MEDIA','ADMIN',7,'WEB','{\"clave\": \"HOME_HERO_BANNER\"}','2026-05-09 14:42:05'),(71,'CONFIGURACION_MEDIA','1','ACTUALIZAR_CONFIGURACION_MEDIA','ADMIN',7,'WEB','{\"tipo\": \"IMAGEN\", \"clave\": \"HOME_HERO_BANNER\"}','2026-05-09 14:42:05'),(72,'CONFIGURACION_MEDIA','1','SUBIR_ARCHIVO_CONFIGURACION_MEDIA','ADMIN',7,'WEB','{\"clave\": \"HOME_HERO_BANNER\"}','2026-05-09 14:47:32'),(73,'CONFIGURACION_MEDIA','1','ACTUALIZAR_CONFIGURACION_MEDIA','ADMIN',7,'WEB','{\"tipo\": \"IMAGEN\", \"clave\": \"HOME_HERO_BANNER\"}','2026-05-09 14:47:33'),(74,'CONFIGURACION_MEDIA','1','SUBIR_ARCHIVO_CONFIGURACION_MEDIA','ADMIN',7,'WEB','{\"clave\": \"HOME_HERO_BANNER\"}','2026-05-09 14:48:58'),(75,'CONFIGURACION_MEDIA','1','ACTUALIZAR_CONFIGURACION_MEDIA','ADMIN',7,'WEB','{\"tipo\": \"IMAGEN\", \"clave\": \"HOME_HERO_BANNER\"}','2026-05-09 14:48:58'),(76,'CONFIGURACION_MEDIA','3','SUBIR_ARCHIVO_CONFIGURACION_MEDIA','ADMIN',7,'WEB','{\"clave\": \"CARTA_PDF\"}','2026-05-09 15:24:03'),(77,'CONFIGURACION_MEDIA','3','ACTUALIZAR_CONFIGURACION_MEDIA','ADMIN',7,'WEB','{\"tipo\": \"PDF\", \"clave\": \"CARTA_PDF\"}','2026-05-09 15:24:03'),(78,'PRODUCTO','4','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"1/4 de Pollo\"}','2026-05-09 16:13:02'),(79,'PRODUCTO','3','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"1/8 de Pollo\"}','2026-05-09 16:23:34'),(80,'PRODUCTO','3','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"1/8 de Pollo\"}','2026-05-09 16:26:27'),(81,'PRODUCTO','4','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"1/4 de Pollo\"}','2026-05-09 16:29:06'),(82,'PRODUCTO','3','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"1/8 de Pollo\"}','2026-05-09 16:32:03'),(83,'PRODUCTO','5','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"1/2 de Pollo\"}','2026-05-09 21:31:17'),(84,'PRODUCTO','6','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"Mostro\"}','2026-05-09 21:35:15'),(85,'PRODUCTO','6','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"Mostro\"}','2026-05-09 21:38:46'),(86,'PRODUCTO','62','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"1 Pollo\"}','2026-05-09 21:42:47'),(87,'PRODUCTO','7','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"Mostrito\"}','2026-05-09 21:48:19'),(88,'PRODUCTO','8','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"Bambino a lo Pobre\"}','2026-05-09 21:52:50'),(89,'PRODUCTO','9','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"Combo 1/2 Pollo + 1/4 Pollo o Gaseosa 1.5L\"}','2026-05-09 21:56:04'),(90,'PRODUCTO','10','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"Combo 1 Pollo + 1/4 Pollo o Gaseosa 1.5L\"}','2026-05-09 21:58:54'),(91,'PRODUCTO','11','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"1 Pollo + 1 Pollo Solo\"}','2026-05-09 22:02:00'),(92,'PRODUCTO','10','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"Combo 1 Pollo + 1/4 Pollo o Gaseosa 1.5L\"}','2026-05-09 22:04:14'),(93,'PRODUCTO','11','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"1 Pollo + 1 Pollo Solo\"}','2026-05-09 22:07:38'),(94,'PRODUCTO','12','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"Aeropuerto\"}','2026-05-09 22:13:14'),(95,'EMPRESA','1','ACTUALIZAR_EMPRESA','ADMIN',7,'WEB','{\"ruc\": \"20123456789\"}','2026-05-09 22:29:25'),(96,'PRODUCTO','13','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"Chaufa Especial\"}','2026-05-09 22:30:30'),(97,'PRODUCTO','14','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"Chaufa Amazónico\"}','2026-05-09 22:35:13'),(98,'PRODUCTO','66','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"Aeropuerto Especial\"}','2026-05-09 22:38:41'),(99,'PRODUCTO','15','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"Lomo Saltado\"}','2026-05-09 22:40:19'),(100,'PRODUCTO','67','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"Arroz Chaufa\"}','2026-05-09 22:42:32'),(101,'ZONA_DELIVERY','1','ACTUALIZAR_ZONA_DELIVERY','ADMIN',7,'WEB','{\"nombre\": \"EMPRESA_PRINCIPAL_1\"}','2026-05-09 22:43:45'),(102,'PRODUCTO','16','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"Pechuga a lo Pobre\"}','2026-05-09 22:47:57'),(103,'PRODUCTO','17','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"Bistec a lo Pobre\"}','2026-05-09 22:51:09'),(104,'PRODUCTO','68','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"Chaufa Salvaje\"}','2026-05-09 22:53:56'),(105,'ZONA_DELIVERY','1','ACTUALIZAR_ZONA_DELIVERY','ADMIN',7,'WEB','{\"nombre\": \"EMPRESA_PRINCIPAL_1\"}','2026-05-09 22:54:58'),(106,'ZONA_DELIVERY','1','ACTUALIZAR_ZONA_DELIVERY','ADMIN',7,'WEB','{\"nombre\": \"EMPRESA_PRINCIPAL_1\"}','2026-05-09 22:55:56'),(107,'ZONA_DELIVERY','1','ACTUALIZAR_ZONA_DELIVERY','ADMIN',7,'WEB','{\"nombre\": \"EMPRESA_PRINCIPAL_1\"}','2026-05-09 22:59:13'),(108,'PRODUCTO','18','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"1 Pollo Familiar\"}','2026-05-09 23:00:46'),(109,'PRODUCTO','19','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"2 Pollos Familiares\"}','2026-05-09 23:03:01'),(110,'ZONA_DELIVERY','1','ACTUALIZAR_ZONA_DELIVERY','ADMIN',7,'WEB','{\"nombre\": \"EMPRESA_PRINCIPAL_1\"}','2026-05-09 23:05:53'),(111,'PRODUCTO','69','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"Tallarín Saltado de Pollo\"}','2026-05-09 23:07:30'),(112,'PRODUCTO','20','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"Pack Familiar Plus\"}','2026-05-09 23:10:12'),(113,'PRODUCTO','20','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"INACTIVO\", \"nombre\": \"Pack Familiar Plus\"}','2026-05-09 23:11:11'),(114,'PRODUCTO','70','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"Tallarín Saltado\"}','2026-05-09 23:14:41'),(115,'PRODUCTO','71','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"Pollo Saltado\"}','2026-05-09 23:18:48'),(116,'PRODUCTO','73','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"Pechuga a la Plancha\"}','2026-05-09 23:20:32'),(117,'PRODUCTO','74','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"Porción de Pollo 1 Pollo\"}','2026-05-09 23:23:42'),(118,'PRODUCTO','75','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"Porción de Pollo 1/2\"}','2026-05-09 23:31:18'),(119,'PRODUCTO','76','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"Porción de Pollo 1/4\"}','2026-05-09 23:34:57'),(120,'PRODUCTO','77','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"Porción de Pollo 1/8\"}','2026-05-09 23:37:11'),(121,'PRODUCTO','78','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"Porción de Ensalada Mediana\"}','2026-05-09 23:38:44'),(122,'EMPRESA','1','ACTUALIZAR_EMPRESA','ADMIN',7,'WEB','{\"ruc\": \"20512073787\"}','2026-05-09 23:42:29'),(123,'EMPRESA','1','ACTUALIZAR_EMPRESA','ADMIN',7,'WEB','{\"ruc\": \"20512073787\"}','2026-05-09 23:44:45'),(124,'EMPRESA','1','ACTUALIZAR_EMPRESA','ADMIN',7,'WEB','{\"ruc\": \"20512073787\"}','2026-05-09 23:49:50'),(125,'EMPRESA','1','ACTUALIZAR_EMPRESA','ADMIN',7,'WEB','{\"ruc\": \"20512073787\"}','2026-05-09 23:50:16'),(126,'PRODUCTO','79','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"Porción de Ensalada Grande\"}','2026-05-09 23:59:31'),(127,'CONFIGURACION_MEDIA','1','ACTUALIZAR_CONFIGURACION_MEDIA','ADMIN',7,'WEB','{\"tipo\": \"IMAGEN\", \"clave\": \"HOME_HERO_BANNER\"}','2026-05-10 00:00:29'),(128,'CONFIGURACION_MEDIA','1','ACTUALIZAR_CONFIGURACION_MEDIA','ADMIN',7,'WEB','{\"tipo\": \"IMAGEN\", \"clave\": \"HOME_HERO_BANNER\"}','2026-05-10 00:01:20'),(129,'CONFIGURACION_MEDIA','1','ACTUALIZAR_CONFIGURACION_MEDIA','ADMIN',7,'WEB','{\"tipo\": \"IMAGEN\", \"clave\": \"HOME_HERO_BANNER\"}','2026-05-10 00:01:26'),(130,'PRODUCTO','80','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"Porción de Papa Mediana\"}','2026-05-10 00:03:04'),(131,'PRODUCTO','81','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"Porción de Papa Grande\"}','2026-05-10 00:09:09'),(132,'PRODUCTO','82','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"Porción de Ensalada Cocida Mediana\"}','2026-05-10 00:11:16'),(133,'PRODUCTO','83','ACTUALIZAR_PRODUCTO','ADMIN',7,'WEB','{\"estado\": \"ACTIVO\", \"nombre\": \"Porción de Ensalada Cocida Grande\"}','2026-05-10 00:14:38'),(134,'SEGURIDAD','4','SOLICITAR_RECUPERACION_PASSWORD','CLIENTE',4,'WEB','{\"email\": \"kevinespiritu16@gmail.com\"}','2026-05-10 02:32:00'),(135,'SEGURIDAD','4','CONFIRMAR_RECUPERACION_PASSWORD','CLIENTE',4,'WEB','{\"email\": \"kevinespiritu16@gmail.com\"}','2026-05-10 02:32:23'),(136,'SEGURIDAD','4','CAMBIAR_PASSWORD_CLIENTE','CLIENTE',4,'WEB','{\"email\": \"kevinespiritu16@gmail.com\"}','2026-05-10 04:25:48'),(137,'SEGURIDAD','7','CAMBIAR_PASSWORD_CLIENTE','CLIENTE',7,'WEB','{\"email\": \"admin123@gmail.com\"}','2026-05-10 04:37:38'),(138,'USUARIO','8','REGISTRO_CLIENTE','CLIENTE',8,'WEB','{\"email\": \"kevin123@gmail.com\"}','2026-05-12 09:21:48'),(139,'USUARIO','9','REGISTRO_CLIENTE','CLIENTE',9,'WEB','{\"email\": \"jarensullcapuma1@gmail.com\"}','2026-05-12 09:23:33'),(140,'SEGURIDAD','9','SOLICITAR_RECUPERACION_PASSWORD','CLIENTE',9,'WEB','{\"email\": \"jarensullcapuma1@gmail.com\"}','2026-05-12 09:24:23'),(141,'USUARIO','10','REGISTRO_CLIENTE','CLIENTE',10,'WEB','{\"email\": \"sankef1617@hotmail.com\"}','2026-05-14 06:54:12'),(142,'SEGURIDAD','9','SOLICITAR_RECUPERACION_PASSWORD','CLIENTE',9,'WEB','{\"email\": \"jarensullcapuma1@gmail.com\"}','2026-05-14 11:30:22'),(143,'SEGURIDAD','9','CONFIRMAR_RECUPERACION_PASSWORD','CLIENTE',9,'WEB','{\"email\": \"jarensullcapuma1@gmail.com\"}','2026-05-14 11:31:20');
/*!40000 ALTER TABLE `auditoria_evento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carrito`
--

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carrito`
--

LOCK TABLES `carrito` WRITE;
/*!40000 ALTER TABLE `carrito` DISABLE KEYS */;
/*!40000 ALTER TABLE `carrito` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carrito_item`
--

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carrito_item`
--

LOCK TABLES `carrito_item` WRITE;
/*!40000 ALTER TABLE `carrito_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `carrito_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categoria_producto`
--

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

--
-- Dumping data for table `categoria_producto`
--

LOCK TABLES `categoria_producto` WRITE;
/*!40000 ALTER TABLE `categoria_producto` DISABLE KEYS */;
INSERT INTO `categoria_producto` VALUES (1,'Familiares','Porciones familiares para compartir entre varias personas.',2,1,NULL,NULL,'2026-05-09 11:14:02','2026-05-09 12:42:54'),(2,'Mostros','Platos tipo mostro/mostrito con pollo, arroz chaufa, papas y ensalada.',1,1,NULL,NULL,'2026-05-09 11:46:28','2026-05-09 12:42:54'),(3,'Combos','Promociones combinadas con pollo y adicionales (gaseosa o porciones extra).',3,1,NULL,NULL,'2026-05-09 12:12:36','2026-05-09 12:42:54'),(4,'Fusión Oriental','Platos orientales como chaufas, aeropuerto y tallarines salteados.',4,1,NULL,NULL,'2026-05-09 12:12:48','2026-05-09 12:42:54'),(5,'Platos a la Carta','Platos de fondo individuales como lomo saltado, bistec, pechuga, etc.',5,1,NULL,NULL,'2026-05-09 12:13:03','2026-05-09 12:42:54'),(6,'Adicionales','Porciones y acompañamientos',6,1,NULL,NULL,'2026-05-10 01:44:38','2026-05-10 01:44:38');
/*!40000 ALTER TABLE `categoria_producto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cliente_direccion`
--

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cliente_direccion`
--

LOCK TABLES `cliente_direccion` WRITE;
/*!40000 ALTER TABLE `cliente_direccion` DISABLE KEYS */;
INSERT INTO `cliente_direccion` VALUES (1,4,'Calle 18 de Diciembre, Sarita Colonia, Chorrillos, Lima, Lima Metropolitana, Lima, 15067, Perú','mi casa','Chorrillos','Lima',-12.1905428,-77.0051908,'OSM:3623473',NULL,0,1,'2026-05-10 04:19:36','2026-05-10 04:59:20'),(2,4,'Calle Libertad, Delicias de Villa, Chorrillos, Lima, Lima Metropolitana, Lima, 15067, Perú','mi casa','Lima Metropolitana','Lima',-12.2028331,-76.9894195,'OSM:3556841',NULL,1,1,'2026-05-10 04:20:31','2026-05-10 04:59:20'),(3,9,'Túpac Amaru de Villa, Delicias de Villa, Chorrillos, Lima, Lima Metropolitana, Lima, 15067, Perú',NULL,'Lima Metropolitana','Lima',-12.1924750,-76.9875612,'OSM:3679498',NULL,1,1,'2026-05-12 09:33:59','2026-05-12 09:33:59');
/*!40000 ALTER TABLE `cliente_direccion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cliente_documento`
--

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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cliente_documento`
--

LOCK TABLES `cliente_documento` WRITE;
/*!40000 ALTER TABLE `cliente_documento` DISABLE KEYS */;
INSERT INTO `cliente_documento` VALUES (1,1,'DNI','12345678',1,1,'2026-05-07 23:34:01','2026-05-07 23:34:01'),(2,3,'DNI','76185477',1,1,'2026-05-09 02:55:02','2026-05-09 02:55:02'),(3,4,'DNI','76185472',1,1,'2026-05-09 03:08:22','2026-05-09 03:08:22'),(4,5,'DNI','76185472',1,1,'2026-05-09 03:11:37','2026-05-09 03:11:37'),(5,6,'DNI','76185472',1,1,'2026-05-09 03:12:39','2026-05-09 03:12:39'),(8,8,'DNI','12331223',1,1,'2026-05-12 09:21:48','2026-05-12 09:21:48'),(9,9,'DNI','12356786',1,1,'2026-05-12 09:23:32','2026-05-12 09:23:32'),(10,10,'DNI','76185477',1,1,'2026-05-14 06:54:12','2026-05-14 06:54:12');
/*!40000 ALTER TABLE `cliente_documento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cliente_perfil`
--

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

--
-- Dumping data for table `cliente_perfil`
--

LOCK TABLES `cliente_perfil` WRITE;
/*!40000 ALTER TABLE `cliente_perfil` DISABLE KEYS */;
INSERT INTO `cliente_perfil` VALUES (1,'DNI','12345678','2026-05-07 23:34:01','2026-05-07 23:34:01'),(3,'DNI','76185477','2026-05-09 02:55:02','2026-05-09 02:55:02'),(4,'DNI','76185472','2026-05-09 03:08:22','2026-05-09 03:08:22'),(5,'DNI','76185472','2026-05-09 03:11:37','2026-05-09 03:11:37'),(6,'DNI','76185472','2026-05-09 03:12:39','2026-05-09 03:12:39'),(8,'DNI','12331223','2026-05-12 09:21:48','2026-05-12 09:21:48'),(9,'DNI','12356786','2026-05-12 09:23:32','2026-05-12 09:23:32'),(10,'DNI','76185477','2026-05-14 06:54:12','2026-05-14 06:54:12');
/*!40000 ALTER TABLE `cliente_perfil` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comprobante`
--

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

--
-- Dumping data for table `comprobante`
--

LOCK TABLES `comprobante` WRITE;
/*!40000 ALTER TABLE `comprobante` DISABLE KEYS */;
/*!40000 ALTER TABLE `comprobante` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comprobante_detalle`
--

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

--
-- Dumping data for table `comprobante_detalle`
--

LOCK TABLES `comprobante_detalle` WRITE;
/*!40000 ALTER TABLE `comprobante_detalle` DISABLE KEYS */;
/*!40000 ALTER TABLE `comprobante_detalle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `configuracion_global`
--

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

--
-- Dumping data for table `configuracion_global`
--

LOCK TABLES `configuracion_global` WRITE;
/*!40000 ALTER TABLE `configuracion_global` DISABLE KEYS */;
INSERT INTO `configuracion_global` VALUES (1,'PEN',18.00,0.00,20,60,'America/Lima','2026-05-07 21:21:43','2026-05-07 21:21:43');
/*!40000 ALTER TABLE `configuracion_global` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `configuracion_media`
--

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

--
-- Dumping data for table `configuracion_media`
--

LOCK TABLES `configuracion_media` WRITE;
/*!40000 ALTER TABLE `configuracion_media` DISABLE KEYS */;
INSERT INTO `configuracion_media` VALUES (1,'HOME_HERO_BANNER','Banner principal Inicio',NULL,'IMAGEN','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778356137/bambino/configuracion/home_hero_banner/0a740222-9e18-47b2-8be6-bd27398f8d68.webp','bambino/configuracion/home_hero_banner/0a740222-9e18-47b2-8be6-bd27398f8d68','1778356137',1,NULL,NULL,'2026-05-09 13:34:27','2026-05-10 00:01:26'),(2,'NOSOTROS_HERO_BANNER','Banner principal Nosotros',NULL,'IMAGEN','',NULL,NULL,1,NULL,NULL,'2026-05-09 13:34:27','2026-05-09 13:34:27'),(3,'CARTA_PDF','Carta PDF pública',NULL,'PDF','https://res.cloudinary.com/daf4vf0a2/raw/upload/v1778358242/bambino/configuracion/carta_pdf/9a8ea435-815c-4f29-b3d1-1a61c6bdbbc3','bambino/configuracion/carta_pdf/9a8ea435-815c-4f29-b3d1-1a61c6bdbbc3','1778358242',1,NULL,NULL,'2026-05-09 13:34:27','2026-05-09 15:24:03'),(4,'PROMOCIONES_HERO_BANNER','Banner principal Promociones',NULL,'IMAGEN','',NULL,NULL,1,NULL,NULL,'2026-05-09 13:34:27','2026-05-09 13:34:27'),(5,'LOGO_PRINCIPAL','Logo principal web',NULL,'IMAGEN','',NULL,NULL,1,NULL,NULL,'2026-05-09 13:34:27','2026-05-09 13:34:27');
/*!40000 ALTER TABLE `configuracion_media` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `empresa`
--

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

--
-- Dumping data for table `empresa`
--

LOCK TABLES `empresa` WRITE;
/*!40000 ALTER TABLE `empresa` DISABLE KEYS */;
INSERT INTO `empresa` VALUES (1,'20512073787','SIPCOM SOCIEDAD ANONIMA CERRADA - SIPCOM S.A.C.','SIPCOM SAC','CAL.SANTA ANITA NRO. 491 URB. VILLA MARINA (FRENTE AL MERCADO LOS PINOS) LIMA - LIMA - CHORRILLOS','999999999','bambinochicken2026@gmail.com',1,'2026-05-07 21:21:43','2026-05-09 23:50:15');
/*!40000 ALTER TABLE `empresa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `libro_reclamaciones`
--

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

--
-- Dumping data for table `libro_reclamaciones`
--

LOCK TABLES `libro_reclamaciones` WRITE;
/*!40000 ALTER TABLE `libro_reclamaciones` DISABLE KEYS */;
/*!40000 ALTER TABLE `libro_reclamaciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oferta`
--

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

--
-- Dumping data for table `oferta`
--

LOCK TABLES `oferta` WRITE;
/*!40000 ALTER TABLE `oferta` DISABLE KEYS */;
INSERT INTO `oferta` VALUES (1,'oferta test 20','PORCENTAJE',20.00,NULL,'ACTIVA','2026-05-08 07:26:48','2026-05-09 08:26:48',1,1,'2026-05-08 08:26:48','2026-05-08 08:26:48');
/*!40000 ALTER TABLE `oferta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oferta_producto`
--

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

--
-- Dumping data for table `oferta_producto`
--

LOCK TABLES `oferta_producto` WRITE;
/*!40000 ALTER TABLE `oferta_producto` DISABLE KEYS */;
/*!40000 ALTER TABLE `oferta_producto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pago`
--

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

--
-- Dumping data for table `pago`
--

LOCK TABLES `pago` WRITE;
/*!40000 ALTER TABLE `pago` DISABLE KEYS */;
/*!40000 ALTER TABLE `pago` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedido`
--

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedido`
--

LOCK TABLES `pedido` WRITE;
/*!40000 ALTER TABLE `pedido` DISABLE KEYS */;
/*!40000 ALTER TABLE `pedido` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017*/ /*!50003 TRIGGER `trg_pedido_validacion_fiscal_ins` BEFORE INSERT ON `pedido` FOR EACH ROW BEGIN
  IF NEW.tipo_comprobante = 'BOLETA' THEN
    IF NEW.cliente_doc_numero_snapshot IS NULL OR CHAR_LENGTH(TRIM(NEW.cliente_doc_numero_snapshot)) = 0 THEN
      SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Boleta requiere documento del cliente';
    END IF;
  ELSEIF NEW.tipo_comprobante = 'FACTURA' THEN
    IF NEW.cliente_doc_numero_snapshot IS NULL OR CHAR_LENGTH(TRIM(NEW.cliente_doc_numero_snapshot)) = 0
       OR NEW.razon_social_snapshot IS NULL OR CHAR_LENGTH(TRIM(NEW.razon_social_snapshot)) = 0
       OR NEW.direccion_fiscal_snapshot IS NULL OR CHAR_LENGTH(TRIM(NEW.direccion_fiscal_snapshot)) = 0 THEN
      SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Factura requiere RUC, razon social y direccion fiscal';
    END IF;
  END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017*/ /*!50003 TRIGGER `trg_pedido_transicion_estado` BEFORE UPDATE ON `pedido` FOR EACH ROW BEGIN
  DECLARE v_count INT DEFAULT 0;
  DECLARE v_rol_usuario VARCHAR(20);
  IF NEW.estado_actual <> OLD.estado_actual THEN
    SELECT COUNT(*) INTO v_count
    FROM pedido_estado_transicion_permitida t
    WHERE t.estado_origen = OLD.estado_actual
      AND t.estado_destino = NEW.estado_actual
      AND t.activa = 1;
    IF v_count = 0 THEN
      SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Transicion de estado no permitida';
    END IF;
    -- Regla clave: cuando cocina toma el pedido y lo pasa a EN_PREPARACION
    -- debe quedar guardado que usuario de cocina lo esta preparando.
    IF NEW.estado_actual = 'EN_PREPARACION' THEN
      IF NEW.usuario_actualizacion IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Debe enviar usuario_actualizacion (usuario de cocina) al iniciar preparacion';
      END IF;
      SELECT r.nombre INTO v_rol_usuario
      FROM usuario u
      JOIN rol r ON r.id_rol = u.id_rol
      WHERE u.id_usuario = NEW.usuario_actualizacion
      LIMIT 1;
      IF v_rol_usuario IS NULL OR v_rol_usuario <> 'COCINA' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Solo un usuario con rol COCINA puede iniciar preparacion';
      END IF;
      IF NEW.usuario_cocina_preparacion IS NULL THEN
        SET NEW.usuario_cocina_preparacion = NEW.usuario_actualizacion;
      END IF;
      IF NEW.fecha_inicio_preparacion IS NULL THEN
        SET NEW.fecha_inicio_preparacion = NOW();
      END IF;
    END IF;
    -- Cuando cocina deja el pedido listo, se marca fin de preparacion.
    IF NEW.estado_actual IN ('LISTO_RECOJO','LISTO_DESPACHO') AND OLD.estado_actual = 'EN_PREPARACION' THEN
      IF NEW.fecha_fin_preparacion IS NULL THEN
        SET NEW.fecha_fin_preparacion = NOW();
      END IF;
    END IF;
  END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017*/ /*!50003 TRIGGER `trg_pedido_historial_estado` AFTER UPDATE ON `pedido` FOR EACH ROW BEGIN
  DECLARE v_actor_tipo ENUM('CLIENTE','COCINA','ADMIN');
  IF NEW.estado_actual <> OLD.estado_actual THEN
    IF NEW.usuario_actualizacion IS NOT NULL THEN
      SELECT r.nombre
      INTO v_actor_tipo
      FROM usuario u
      JOIN rol r ON r.id_rol = u.id_rol
      WHERE u.id_usuario = NEW.usuario_actualizacion
      LIMIT 1;
    ELSE
      SET v_actor_tipo = NULL;
    END IF;
    INSERT INTO pedido_estado_historial
      (id_pedido, estado_anterior, estado_nuevo, actor_tipo, id_actor, canal, motivo, fecha_creacion)
    VALUES
      (NEW.id_pedido, OLD.estado_actual, NEW.estado_actual, v_actor_tipo, NEW.usuario_actualizacion, NEW.canal_origen, 'Cambio de estado', NOW());
  END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `pedido_asignacion_cocina`
--

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

--
-- Dumping data for table `pedido_asignacion_cocina`
--

LOCK TABLES `pedido_asignacion_cocina` WRITE;
/*!40000 ALTER TABLE `pedido_asignacion_cocina` DISABLE KEYS */;
/*!40000 ALTER TABLE `pedido_asignacion_cocina` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedido_cocina_incidencia`
--

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

--
-- Dumping data for table `pedido_cocina_incidencia`
--

LOCK TABLES `pedido_cocina_incidencia` WRITE;
/*!40000 ALTER TABLE `pedido_cocina_incidencia` DISABLE KEYS */;
/*!40000 ALTER TABLE `pedido_cocina_incidencia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedido_estado_historial`
--

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

--
-- Dumping data for table `pedido_estado_historial`
--

LOCK TABLES `pedido_estado_historial` WRITE;
/*!40000 ALTER TABLE `pedido_estado_historial` DISABLE KEYS */;
/*!40000 ALTER TABLE `pedido_estado_historial` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedido_estado_transicion_permitida`
--

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

--
-- Dumping data for table `pedido_estado_transicion_permitida`
--

LOCK TABLES `pedido_estado_transicion_permitida` WRITE;
/*!40000 ALTER TABLE `pedido_estado_transicion_permitida` DISABLE KEYS */;
INSERT INTO `pedido_estado_transicion_permitida` VALUES (1,'CREADO','PAGO_PENDIENTE','CLIENTE',1),(2,'CREADO','CANCELADO','CLIENTE',1),(3,'PAGO_PENDIENTE','PAGO_APROBADO','ADMIN',1),(4,'PAGO_PENDIENTE','PAGO_RECHAZADO','ADMIN',1),(5,'PAGO_PENDIENTE','CANCELADO','CLIENTE',1),(6,'PAGO_RECHAZADO','PAGO_PENDIENTE','CLIENTE',1),(7,'PAGO_RECHAZADO','CANCELADO','CLIENTE',1),(8,'PAGO_APROBADO','CONFIRMADO','ADMIN',1),(9,'CONFIRMADO','EN_PREPARACION','COCINA',1),(10,'EN_PREPARACION','LISTO_RECOJO','COCINA',1),(11,'EN_PREPARACION','LISTO_DESPACHO','COCINA',1),(12,'LISTO_RECOJO','ENTREGADO','COCINA',1),(13,'LISTO_DESPACHO','EN_CAMINO','COCINA',1),(14,'EN_CAMINO','ENTREGADO','COCINA',1),(15,'CONFIRMADO','CANCELADO','ADMIN',1),(16,'EN_PREPARACION','CANCELADO','ADMIN',1);
/*!40000 ALTER TABLE `pedido_estado_transicion_permitida` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedido_item`
--

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedido_item`
--

LOCK TABLES `pedido_item` WRITE;
/*!40000 ALTER TABLE `pedido_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `pedido_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `producto`
--

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

--
-- Dumping data for table `producto`
--

LOCK TABLES `producto` WRITE;
/*!40000 ALTER TABLE `producto` DISABLE KEYS */;
INSERT INTO `producto` VALUES (3,'1/8 de Pollo','1/8 de pollo + papas + ensalada + cremas.',1,10.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778362320/bambino/productos/b69c8574-f555-4503-9b9f-8b6118c8b4d6.png',1,NULL,NULL,'2026-05-09 12:25:06','2026-05-09 16:32:03'),(4,'1/4 de Pollo','1/4 de pollo + papas + ensalada + cremas.',1,16.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778362142/bambino/productos/4a9a5532-e1c0-418b-8f4f-ddf84d75f409.png',2,NULL,NULL,'2026-05-09 12:25:06','2026-05-10 02:14:58'),(5,'1/2 de Pollo','1/2 de pollo + papas + ensalada + cremas.',1,30.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778380270/bambino/productos/d07b911d-7f25-48be-b4f4-0a11d8552c5e.png',3,NULL,NULL,'2026-05-09 12:25:06','2026-05-09 21:31:17'),(6,'Mostro','1/4 de pollo + arroz chaufa + papas + ensalada.',1,17.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778380721/bambino/productos/b9b46de1-12f6-495b-9ad3-deba9494151b.png',4,NULL,NULL,'2026-05-09 12:25:06','2026-05-09 21:38:46'),(7,'Mostrito','1/8 de pollo + arroz chaufa + papas + ensalada + cremas.',2,12.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778381289/bambino/productos/5b3ab6b1-d32e-4fc2-a46d-8e284be7094f.png',6,NULL,NULL,'2026-05-09 12:25:06','2026-05-09 21:48:18'),(8,'Bambino a lo Pobre','1/4 de pollo + arroz + papas + ensalada + huevo + plátano + cremas.',2,20.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778381566/bambino/productos/25564566-e7b5-47c7-927c-b1680f4acf30.png',7,NULL,NULL,'2026-05-09 12:25:06','2026-05-09 21:52:50'),(9,'Combo 1/2 Pollo + 1/4 Pollo o Gaseosa 1.5L','1/2 pollo + 1/4 pollo o gaseosa 1.5L + papas + ensalada + cremas.',3,38.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778381758/bambino/productos/4b1156d0-0434-4d54-a602-94d01e62eb98.png',8,NULL,NULL,'2026-05-09 12:25:06','2026-05-09 21:56:03'),(10,'Combo 1 Pollo + 1/4 Pollo o Gaseosa 1.5L','1 pollo + 1/4 pollo o gaseosa 1.5L + papas + ensalada + cremas.',3,59.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778382249/bambino/productos/e13eb0f1-2875-4b4f-9036-be709976d70c.png',9,NULL,NULL,'2026-05-09 12:25:06','2026-05-09 22:04:14'),(11,'1 Pollo + 1 Pollo Solo','1 pollo + 1 pollo solo + papas + ensalada + cremas.',3,77.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778382455/bambino/productos/1e30bc54-597f-403e-9c3d-6a00b9defabf.png',10,NULL,NULL,'2026-05-09 12:25:06','2026-05-09 22:07:38'),(12,'Aeropuerto','Combinación de arroz chaufa y tallarines salteados.',4,13.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778382790/bambino/productos/5abcff1f-99b9-4d52-a09c-65c2bea8e487.png',11,NULL,NULL,'2026-05-09 12:25:06','2026-05-09 22:13:13'),(13,'Chaufa Especial','Arroz chaufa con pollo, cerdo, chorizo, huevo y cebollita china.',4,15.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778383827/bambino/productos/0a747a1d-ad00-4165-bc21-70d0c1beec73.png',12,NULL,NULL,'2026-05-09 12:25:06','2026-05-09 22:30:30'),(14,'Chaufa Amazónico','Arroz salteado al wok con toque amazónico.',4,18.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778384110/bambino/productos/59c8c0de-4c7f-451c-8506-b09f9918ef30.png',13,NULL,NULL,'2026-05-09 12:25:06','2026-05-09 22:35:13'),(15,'Lomo Saltado','Lomo salteado acompañado de papas fritas y arroz.',5,23.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778384415/bambino/productos/f906f4d4-cbb9-4522-aeeb-50814ba93727.png',15,NULL,NULL,'2026-05-09 12:25:06','2026-05-09 22:40:19'),(16,'Pechuga a lo Pobre','Pechuga con papas, arroz, huevo y plátano.',5,18.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778384873/bambino/productos/05cdc79d-1a31-4eb2-99b6-89339cd80167.png',17,NULL,NULL,'2026-05-09 12:25:06','2026-05-09 22:47:57'),(17,'Bistec a lo Pobre','Bisteck con papas, arroz, huevo y plátano.',5,23.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778385065/bambino/productos/9d6a0312-f11c-48ba-a544-ae6523ceacbb.png',18,NULL,NULL,'2026-05-09 12:25:06','2026-05-09 22:51:09'),(18,'1 Pollo Familiar','1 pollo entero + papas familiares + ensalada + cremas.',1,52.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778385642/bambino/productos/fc14f302-dcad-41cc-b661-0e3190fe1c2a.png',20,NULL,NULL,'2026-05-09 12:26:15','2026-05-09 23:00:46'),(19,'2 Pollos Familiares','2 pollos + 2 porciones de papa + 2 ensaladas + cremas.',1,122.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778385777/bambino/productos/96bdcc51-656a-4f97-9cc0-73834152438d.png',21,NULL,NULL,'2026-05-09 12:26:15','2026-05-09 23:03:01'),(20,'Pack Familiar Plus','1 pollo + 1/2 pollo + papas grandes + ensalada familiar + cremas.',1,67.00,0,1,'INACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778386206/bambino/productos/04194df8-d7f7-458a-aed3-7469a56082a8.png',23,NULL,NULL,'2026-05-09 12:26:15','2026-05-09 23:11:11'),(62,'1 Pollo','1 pollo + papas + ensalada + cremas.',1,52.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778380962/bambino/productos/a526b08a-d0cb-4c09-929d-a57c2a3ee03e.png',5,NULL,NULL,'2026-05-10 02:25:28','2026-05-09 21:42:47'),(66,'Aeropuerto Especial','Aeropuerto con pollo, cerdo y chorizo.',4,16.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778384317/bambino/productos/0f0a4e14-5f26-430c-80be-8aa7131cfce9.png',14,NULL,NULL,'2026-05-10 02:25:29','2026-05-09 22:38:41'),(67,'Arroz Chaufa','Arroz chaufa tradicional.',4,12.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778384549/bambino/productos/9f376609-4d10-4253-8a5c-e7339de3da07.png',16,NULL,NULL,'2026-05-10 02:25:29','2026-05-09 22:42:32'),(68,'Chaufa Salvaje','Chaufa salvaje con toque especial.',4,16.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778385232/bambino/productos/120022ac-d886-467d-a2bf-edce330741bd.png',19,NULL,NULL,'2026-05-10 02:25:30','2026-05-09 22:53:56'),(69,'Tallarín Saltado de Pollo','Tallarín saltado con pollo.',4,23.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778386046/bambino/productos/de93a53e-e874-4fe4-9492-f1fb8ebb9cae.png',22,NULL,NULL,'2026-05-10 02:25:30','2026-05-09 23:07:30'),(70,'Tallarín Saltado','Tallarín saltado.',4,23.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778386477/bambino/productos/e398afc1-6f6a-4fd1-afdf-28f2f9aa7a90.png',24,NULL,NULL,'2026-05-10 02:25:30','2026-05-09 23:14:41'),(71,'Pollo Saltado','Pollo saltado clásico.',5,23.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778386724/bambino/productos/a2ee1434-4088-46da-b05a-3d38fb2125fc.png',25,NULL,NULL,'2026-05-10 02:25:30','2026-05-09 23:18:48'),(73,'Pechuga a la Plancha','Pechuga a la plancha.',5,15.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778386827/bambino/productos/5bbd669f-f458-4d6b-bb81-495cb9b3669f.png',26,NULL,NULL,'2026-05-10 02:25:31','2026-05-09 23:20:32'),(74,'Porción de Pollo 1 Pollo','Porciones de Pollo',6,32.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778387014/bambino/productos/ed53f5fd-a223-40b4-b8fe-f4aa8221fb37.png',27,NULL,NULL,'2026-05-10 02:25:31','2026-05-09 23:23:41'),(75,'Porción de Pollo 1/2','Porciones de Pollo',6,17.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778387474/bambino/productos/4bb9bf06-3ab2-435b-89ad-7280f98517db.png',28,NULL,NULL,'2026-05-10 02:25:31','2026-05-09 23:31:18'),(76,'Porción de Pollo 1/4','Porciones de Pollo',6,9.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778387693/bambino/productos/1c035046-dc50-4a53-9992-9bc8dff12dae.png',29,NULL,NULL,'2026-05-10 02:25:31','2026-05-09 23:34:56'),(77,'Porción de Pollo 1/8','Porciones de Pollo',6,5.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778387820/bambino/productos/4c7bffce-e44e-4059-8bc8-1ca0565dd9ee.png',30,NULL,NULL,'2026-05-10 02:25:32','2026-05-09 23:37:10'),(78,'Porción de Ensalada Mediana','Porciones de Ensalada',6,5.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778387920/bambino/productos/3f14da3a-11a6-44e8-a2c2-04348b54cb7e.png',31,NULL,NULL,'2026-05-10 02:25:32','2026-05-09 23:38:43'),(79,'Porción de Ensalada Grande','Porciones de Ensalada',6,10.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778389160/bambino/productos/f7deafa3-b85b-4aaf-89b9-7db69da7abdc.png',32,NULL,NULL,'2026-05-10 02:25:32','2026-05-09 23:59:31'),(80,'Porción de Papa Mediana','Porciones de Papa',6,10.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778389381/bambino/productos/f879aa2e-a025-4004-835b-1bd5fcf74326.png',33,NULL,NULL,'2026-05-10 02:25:32','2026-05-10 00:03:04'),(81,'Porción de Papa Grande','Porciones de Papa',6,15.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778389744/bambino/productos/aa5ac973-7a91-4baa-a466-dac699b7515e.png',34,NULL,NULL,'2026-05-10 02:25:32','2026-05-10 00:09:09'),(82,'Porción de Ensalada Cocida Mediana','Porciones de Ensalada Cocida',6,5.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778389872/bambino/productos/ca61d8c4-2fc3-4205-aed7-d1a040b47d67.png',35,NULL,NULL,'2026-05-10 02:25:32','2026-05-10 00:11:15'),(83,'Porción de Ensalada Cocida Grande','Porciones de Ensalada Cocida',6,10.00,1,1,'ACTIVO','https://res.cloudinary.com/daf4vf0a2/image/upload/v1778390075/bambino/productos/6c0a2afe-d4f9-45d4-9cc2-ab4bc18208a1.png',36,NULL,NULL,'2026-05-10 02:25:32','2026-05-10 00:14:38');
/*!40000 ALTER TABLE `producto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recuperacion_password_codigo`
--

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
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recuperacion_password_codigo`
--

LOCK TABLES `recuperacion_password_codigo` WRITE;
/*!40000 ALTER TABLE `recuperacion_password_codigo` DISABLE KEYS */;
INSERT INTO `recuperacion_password_codigo` VALUES (1,1,'kevinespiritu16@gmail.com',NULL,'$2a$10$rTxkjTK7.HwCHX5PDwFPC.O.hWz5PG2zwTsP.mjM1QIm5OlIdz2y.','EXPIRADO','2026-05-07 23:50:18',0,'2026-05-07 23:35:18','2026-05-07 23:40:04'),(2,1,'kevinespiritu16@gmail.com',NULL,'$2a$10$8ubl.IVXI5n9NNEa9kzuKOyAS6RXBMfRGdc90WySqSmzzGDydstpK','EXPIRADO','2026-05-07 23:55:04',0,'2026-05-07 23:40:04','2026-05-09 03:25:03'),(3,1,'kevinespiritu_154@hotmail.com',NULL,'$2a$10$0WPIYXtm7THMEqQ6LTJK5ejsEwbc.GcmEWNEHCCVowam2qkmboUGq','PENDIENTE','2026-05-07 23:59:14',0,'2026-05-07 23:44:14','2026-05-07 23:44:14'),(4,6,'kevinespiritu154@gmail.com',NULL,'$2a$10$5e.o2dj8kCxRpoKrDXswnuBVbghEs1gLC72kAQikO5FSsQ27X1Rju','PENDIENTE','2026-05-09 03:39:20',0,'2026-05-09 03:24:20','2026-05-09 03:24:20'),(5,4,'kevinespiritu16@gmail.com',NULL,'$2a$10$wFbi5qA8332GJ12J/owKke4taTG0yy7gcN/iHUigSJyRRoVWL.wZ2','EXPIRADO','2026-05-09 03:40:03',0,'2026-05-09 03:25:03','2026-05-09 03:26:53'),(6,4,'kevinespiritu16@gmail.com',NULL,'$2a$10$yQDqBylw9TEB/G.XbJOFyeR/1ERmTO3c.1WoAsle5UZapl3PXVMY6','EXPIRADO','2026-05-09 03:41:53',0,'2026-05-09 03:26:53','2026-05-09 03:29:25'),(7,4,'kevinespiritu16@gmail.com',NULL,'$2a$10$BiIixEq3s/1xNITW3DK/XODB7ghPOsO1I1qieXzGGn7RLjVTJaQ3m','EXPIRADO','2026-05-09 03:44:25',0,'2026-05-09 03:29:25','2026-05-09 03:29:38'),(8,4,'kevinespiritu16@gmail.com',NULL,'$2a$10$Ex1q3vdtWcp..CBFcQmitOMfIBwNVwDheKlaIvmZoPYFeZF2cM5WK','EXPIRADO','2026-05-09 03:44:38',0,'2026-05-09 03:29:38','2026-05-09 03:33:38'),(9,4,'kevinespiritu16@gmail.com',NULL,'$2a$10$cqNXagt/Cdhq9gauhV7uR.BER2cmoP3BeYdSFF/gdYUDviaJ7Yscm','EXPIRADO','2026-05-09 03:48:38',0,'2026-05-09 03:33:38','2026-05-09 03:35:36'),(10,4,'kevinespiritu16@gmail.com',NULL,'$2a$10$m/XI/JV4bNkgMt/WBqHBzeCJAz6nbmfJ4KRI1t7mzm27CbXsYRpoC','EXPIRADO','2026-05-09 03:50:36',0,'2026-05-09 03:35:36','2026-05-09 03:40:22'),(11,4,'kevinespiritu16@gmail.com',NULL,'$2a$10$5psNSrk6TlG0rb/csR7GLukHemzVdK5DBvLumIoaMqWLONYinAJVe','EXPIRADO','2026-05-09 03:55:22',0,'2026-05-09 03:40:22','2026-05-09 09:18:57'),(12,4,'kevinespiritu16@gmail.com',NULL,'$2a$10$wxTph.7pX5kkEzE01tGQz.f5e1P1jzXZPvg876YGvWIOiOQQmqMC.','EXPIRADO','2026-05-09 09:33:57',0,'2026-05-09 09:18:57','2026-05-09 09:19:46'),(13,4,'kevinespiritu16@gmail.com',NULL,'$2a$10$SCuzpZOE7Oxt5cByztVqTe.tRjtOduUN7v5CKlLBkDq9H8ftAsjae','EXPIRADO','2026-05-09 09:34:46',0,'2026-05-09 09:19:46','2026-05-09 09:23:43'),(14,4,'kevinespiritu16@gmail.com',NULL,'$2a$10$3gCbugGsBZX6dIBAv3fqNus.pLc66jtMZ3V1MzVKBQiqPWxrjeMQ.','EXPIRADO','2026-05-09 09:38:43',0,'2026-05-09 09:23:43','2026-05-09 09:25:46'),(15,4,'kevinespiritu16@gmail.com',NULL,'$2a$10$WrXaRgpsrIxt7r4rxp1O8enP/7x739pui4qo6cYduRHHQmjTeM9Ge','USADO','2026-05-09 09:40:46',0,'2026-05-09 09:25:46','2026-05-09 09:27:27'),(16,4,'kevinespiritu16@gmail.com',NULL,'$2a$10$ERi8KQB3PIAALzQd2mcAWunr3HiuflRx5pb3jkkACCFInokBlPutW','USADO','2026-05-10 02:46:53',0,'2026-05-10 02:31:53','2026-05-10 02:32:23'),(17,9,'jarensullcapuma1@gmail.com',NULL,'$2a$10$Fb8MvMhah1GF7pHHAwzTqulXKdRLGaKCTEO./g9mx5bZi7sY0saG2','EXPIRADO','2026-05-12 09:39:19',0,'2026-05-12 09:24:19','2026-05-14 11:30:18'),(18,9,'jarensullcapuma1@gmail.com',NULL,'$2a$10$96i5vBIv2ADKTaoSpna4NOtPxx.LuIK9bcrqhH8BunBVoS0N2PtfC','USADO','2026-05-14 11:45:18',0,'2026-05-14 11:30:18','2026-05-14 11:31:19');
/*!40000 ALTER TABLE `recuperacion_password_codigo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rol`
--

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

--
-- Dumping data for table `rol`
--

LOCK TABLES `rol` WRITE;
/*!40000 ALTER TABLE `rol` DISABLE KEYS */;
INSERT INTO `rol` VALUES (1,'CLIENTE','Usuario final que compra','2026-05-07 21:21:43','2026-05-07 21:21:43'),(2,'COCINA','Usuario operativo de atencion','2026-05-07 21:21:43','2026-05-07 21:21:43'),(3,'ADMIN','Administrador del sistema','2026-05-07 21:21:43','2026-05-07 21:21:43');
/*!40000 ALTER TABLE `rol` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `serie_comprobante`
--

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

--
-- Dumping data for table `serie_comprobante`
--

LOCK TABLES `serie_comprobante` WRITE;
/*!40000 ALTER TABLE `serie_comprobante` DISABLE KEYS */;
/*!40000 ALTER TABLE `serie_comprobante` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,'be9193d2-abd6-4415-8f53-d0c73f77ccfb','kevinespiritu_154@hotmail.com','$2a$10$qnpIE8Hrdy2UDOp2QJ3M6eK4bzycALj51zE9xM35qSxXHgSG4nxZ6','Kevin','Espiritu','980005245','DOC00000001','ACTIVO',1,'2026-05-07 23:34:01','2026-05-10 06:07:43'),(3,'1d7b4e89-55f9-4c9c-83a5-e7199c27040a','kevinespiritu_154@gmail.com','$2a$10$7ioRzhYQKFYgzct0xuCSWOrc1FowGc8f7oN8Z5KBxMA3uHnc08k8S','kevin','kevin','980005245','DOC00000003','ACTIVO',1,'2026-05-09 02:55:02','2026-05-10 06:07:43'),(4,'6e8c5310-8c53-4fdb-a027-ebf9e71e93f8','kevinespiritu16@gmail.com','$2a$10$LJQ7sZgT8m0YBSrtTN5Kcu7ZDt1NZDHidt3rYDi57D9GZTavE9Eqy','kevin','espiritu','980005242','DOC00000004','ACTIVO',1,'2026-05-09 03:08:22','2026-05-10 04:25:48'),(5,'e6fadcec-76c4-416b-9f9c-cd8cd4c8e45d','kevinespiritu1617@gmail.com','$2a$10$3ZQBrPa2lK7N2CFaXlCGx.CJSb38eaagFV9S0JEptSdjjYhpe50jS','kevin','espiritu','980005242','DOC00000005','ACTIVO',1,'2026-05-09 03:11:37','2026-05-10 06:07:43'),(6,'b22fd215-45a0-485b-b634-8fc1f982edd4','kevinespiritu154@gmail.com','$2a$10$pCE1.luXhnaBstvRWI8dB.pYyklWCmuUq95OjgrO7YqEo/55ZxJi6','kevin','espiritu','980005242','DOC00000006','ACTIVO',1,'2026-05-09 03:12:39','2026-05-10 06:07:43'),(7,'6a2e459e-4bb8-11f1-8c9a-7a7bf86d37ca','admin123@gmail.com','$2a$10$ir3TWYmSASZA0.WX6GjDEeJK21F1AiIAR5vyPtQSLLKiSifvAYwAu','Admin','Principal',NULL,'DOC00000007','ACTIVO',3,'2026-05-09 10:04:45','2026-05-10 04:37:38'),(8,'d2c48451-2415-4be3-afae-01e9c7cb5835','kevin123@gmail.com','$2a$10$HW2mgITEammKT/j8HxSa8eXITFTdGdq9WdjSgkSxAeiUo3czIqDn.','brunela','acuña','980005242','12331223','ACTIVO',1,'2026-05-12 09:21:48','2026-05-12 09:21:48'),(9,'0df12a82-7aed-4ee6-bf7b-3fd5c68aa092','jarensullcapuma1@gmail.com','$2a$10$sdsfE/qxzgj9PWO8i4OBjuCO/OznToJ09iYev4sZ8xX9VDJu072Vi','brunelas','acuñas','980005242','12356786','ACTIVO',1,'2026-05-12 09:23:32','2026-05-14 11:31:19'),(10,'c301ae93-dad4-492b-b636-155de354a037','sankef1617@hotmail.com','$2a$10$mz2uHJAYhlBT.aoPHxX5KuYa0K0nJYDtqV0GhFYoB8/TfPJ/..UaK','kevin','espiritu','980005245','76185477','ACTIVO',1,'2026-05-14 06:54:12','2026-05-14 06:54:12');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zona_delivery`
--

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
-- Dumping data for table `zona_delivery`
--

LOCK TABLES `zona_delivery` WRITE;
/*!40000 ALTER TABLE `zona_delivery` DISABLE KEYS */;
INSERT INTO `zona_delivery` VALUES (1,'Bambino Chicken',1,0.00,0.00,35,'Ubicacion principal de Bambino Chicken (Chorrillos)',-12.1895260,-77.0143020,1.00,'https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d243.7417061606472!2d-77.01427112266029!3d-12.18943147197095!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x9105b72336f14f51%3A0xc38b81e3e4a7decf!2sBambino%20Chicken!5e0!3m2!1ses-419!2spe!4v1778384657844!5m2!1ses-419!2spe',NULL,NULL,'2026-05-10 03:42:42','2026-05-10 07:49:37',NULL,NULL);
/*!40000 ALTER TABLE `zona_delivery` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'bambino_db'
--

--
-- Dumping routines for database 'bambino_db'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-27 10:29:44
