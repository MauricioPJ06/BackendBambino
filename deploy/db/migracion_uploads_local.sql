-- Migracion de URLs Cloudinary a rutas locales
-- Generado: 2026-05-27 10:30:26 -05
START TRANSACTION;

-- 1) producto.imagen_url
UPDATE producto p
JOIN (
  SELECT 10 AS id_producto, '/uploads/productos/producto_10_combo_1_pollo___1_4_pollo_o_gaseosa_1.5l.png' AS nueva_url
  UNION ALL
  SELECT 11 AS id_producto, '/uploads/productos/producto_11_1_pollo___1_pollo_solo.png' AS nueva_url
  UNION ALL
  SELECT 12 AS id_producto, '/uploads/productos/producto_12_aeropuerto.png' AS nueva_url
  UNION ALL
  SELECT 13 AS id_producto, '/uploads/productos/producto_13_chaufa_especial.png' AS nueva_url
  UNION ALL
  SELECT 14 AS id_producto, '/uploads/productos/producto_14_chaufa_amaz_nico.png' AS nueva_url
  UNION ALL
  SELECT 15 AS id_producto, '/uploads/productos/producto_15_lomo_saltado.png' AS nueva_url
  UNION ALL
  SELECT 16 AS id_producto, '/uploads/productos/producto_16_pechuga_a_lo_pobre.png' AS nueva_url
  UNION ALL
  SELECT 17 AS id_producto, '/uploads/productos/producto_17_bistec_a_lo_pobre.png' AS nueva_url
  UNION ALL
  SELECT 18 AS id_producto, '/uploads/productos/producto_18_1_pollo_familiar.png' AS nueva_url
  UNION ALL
  SELECT 19 AS id_producto, '/uploads/productos/producto_19_2_pollos_familiares.png' AS nueva_url
  UNION ALL
  SELECT 20 AS id_producto, '/uploads/productos/producto_20_pack_familiar_plus.png' AS nueva_url
  UNION ALL
  SELECT 3 AS id_producto, '/uploads/productos/producto_3_1_8_de_pollo.png' AS nueva_url
  UNION ALL
  SELECT 4 AS id_producto, '/uploads/productos/producto_4_1_4_de_pollo.png' AS nueva_url
  UNION ALL
  SELECT 5 AS id_producto, '/uploads/productos/producto_5_1_2_de_pollo.png' AS nueva_url
  UNION ALL
  SELECT 62 AS id_producto, '/uploads/productos/producto_62_1_pollo.png' AS nueva_url
  UNION ALL
  SELECT 66 AS id_producto, '/uploads/productos/producto_66_aeropuerto_especial.png' AS nueva_url
  UNION ALL
  SELECT 67 AS id_producto, '/uploads/productos/producto_67_arroz_chaufa.png' AS nueva_url
  UNION ALL
  SELECT 68 AS id_producto, '/uploads/productos/producto_68_chaufa_salvaje.png' AS nueva_url
  UNION ALL
  SELECT 69 AS id_producto, '/uploads/productos/producto_69_tallar_n_saltado_de_pollo.png' AS nueva_url
  UNION ALL
  SELECT 6 AS id_producto, '/uploads/productos/producto_6_mostro.png' AS nueva_url
  UNION ALL
  SELECT 70 AS id_producto, '/uploads/productos/producto_70_tallar_n_saltado.png' AS nueva_url
  UNION ALL
  SELECT 71 AS id_producto, '/uploads/productos/producto_71_pollo_saltado.png' AS nueva_url
  UNION ALL
  SELECT 73 AS id_producto, '/uploads/productos/producto_73_pechuga_a_la_plancha.png' AS nueva_url
  UNION ALL
  SELECT 74 AS id_producto, '/uploads/productos/producto_74_porci_n_de_pollo_1_pollo.png' AS nueva_url
  UNION ALL
  SELECT 75 AS id_producto, '/uploads/productos/producto_75_porci_n_de_pollo_1_2.png' AS nueva_url
  UNION ALL
  SELECT 76 AS id_producto, '/uploads/productos/producto_76_porci_n_de_pollo_1_4.png' AS nueva_url
  UNION ALL
  SELECT 77 AS id_producto, '/uploads/productos/producto_77_porci_n_de_pollo_1_8.png' AS nueva_url
  UNION ALL
  SELECT 78 AS id_producto, '/uploads/productos/producto_78_porci_n_de_ensalada_mediana.png' AS nueva_url
  UNION ALL
  SELECT 79 AS id_producto, '/uploads/productos/producto_79_porci_n_de_ensalada_grande.png' AS nueva_url
  UNION ALL
  SELECT 7 AS id_producto, '/uploads/productos/producto_7_mostrito.png' AS nueva_url
  UNION ALL
  SELECT 80 AS id_producto, '/uploads/productos/producto_80_porci_n_de_papa_mediana.png' AS nueva_url
  UNION ALL
  SELECT 81 AS id_producto, '/uploads/productos/producto_81_porci_n_de_papa_grande.png' AS nueva_url
  UNION ALL
  SELECT 82 AS id_producto, '/uploads/productos/producto_82_porci_n_de_ensalada_cocida_mediana.png' AS nueva_url
  UNION ALL
  SELECT 83 AS id_producto, '/uploads/productos/producto_83_porci_n_de_ensalada_cocida_grande.png' AS nueva_url
  UNION ALL
  SELECT 8 AS id_producto, '/uploads/productos/producto_8_bambino_a_lo_pobre.png' AS nueva_url
  UNION ALL
  SELECT 9 AS id_producto, '/uploads/productos/producto_9_combo_1_2_pollo___1_4_pollo_o_gaseosa_1.5l.png' AS nueva_url
) m ON m.id_producto = p.id_producto
SET p.imagen_url = m.nueva_url
WHERE p.imagen_url LIKE '%cloudinary.com%';

-- 2) configuracion_media.url, public_id, version_tag
UPDATE configuracion_media
SET url = CASE id_media
    WHEN 1 THEN '/uploads/configuracion/home_hero_banner/configuracion_imagen_1_home_hero_banner.webp'
    WHEN 3 THEN '/uploads/configuracion/carta_pdf/configuracion_pdf_3_carta_pdf.pdf'
    ELSE url
END,
public_id = CASE WHEN id_media IN (1,3) THEN NULL ELSE public_id END,
version_tag = CASE WHEN id_media IN (1,3) THEN NULL ELSE version_tag END
WHERE id_media IN (1,3) OR url LIKE '%cloudinary.com%' OR public_id IS NOT NULL OR version_tag IS NOT NULL;

COMMIT;
