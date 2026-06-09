-- Migracion manual Cloudinary -> almacenamiento local
-- NO ejecutar automáticamente en despliegue sin validar previamente
START TRANSACTION;

UPDATE producto
SET imagen_url = CASE id_producto
  WHEN 10 THEN '/uploads/productos/producto_10_combo_1_pollo___1_4_pollo_o_gaseosa_1.5l.png'
  WHEN 11 THEN '/uploads/productos/producto_11_1_pollo___1_pollo_solo.png'
  WHEN 12 THEN '/uploads/productos/producto_12_aeropuerto.png'
  WHEN 13 THEN '/uploads/productos/producto_13_chaufa_especial.png'
  WHEN 14 THEN '/uploads/productos/producto_14_chaufa_amaz_nico.png'
  WHEN 15 THEN '/uploads/productos/producto_15_lomo_saltado.png'
  WHEN 16 THEN '/uploads/productos/producto_16_pechuga_a_lo_pobre.png'
  WHEN 17 THEN '/uploads/productos/producto_17_bistec_a_lo_pobre.png'
  WHEN 18 THEN '/uploads/productos/producto_18_1_pollo_familiar.png'
  WHEN 19 THEN '/uploads/productos/producto_19_2_pollos_familiares.png'
  WHEN 20 THEN '/uploads/productos/producto_20_pack_familiar_plus.png'
  WHEN 3 THEN '/uploads/productos/producto_3_1_8_de_pollo.png'
  WHEN 4 THEN '/uploads/productos/producto_4_1_4_de_pollo.png'
  WHEN 5 THEN '/uploads/productos/producto_5_1_2_de_pollo.png'
  WHEN 62 THEN '/uploads/productos/producto_62_1_pollo.png'
  WHEN 66 THEN '/uploads/productos/producto_66_aeropuerto_especial.png'
  WHEN 67 THEN '/uploads/productos/producto_67_arroz_chaufa.png'
  WHEN 68 THEN '/uploads/productos/producto_68_chaufa_salvaje.png'
  WHEN 69 THEN '/uploads/productos/producto_69_tallar_n_saltado_de_pollo.png'
  WHEN 6 THEN '/uploads/productos/producto_6_mostro.png'
  WHEN 70 THEN '/uploads/productos/producto_70_tallar_n_saltado.png'
  WHEN 71 THEN '/uploads/productos/producto_71_pollo_saltado.png'
  WHEN 73 THEN '/uploads/productos/producto_73_pechuga_a_la_plancha.png'
  WHEN 74 THEN '/uploads/productos/producto_74_porci_n_de_pollo_1_pollo.png'
  WHEN 75 THEN '/uploads/productos/producto_75_porci_n_de_pollo_1_2.png'
  WHEN 76 THEN '/uploads/productos/producto_76_porci_n_de_pollo_1_4.png'
  WHEN 77 THEN '/uploads/productos/producto_77_porci_n_de_pollo_1_8.png'
  WHEN 78 THEN '/uploads/productos/producto_78_porci_n_de_ensalada_mediana.png'
  WHEN 79 THEN '/uploads/productos/producto_79_porci_n_de_ensalada_grande.png'
  WHEN 7 THEN '/uploads/productos/producto_7_mostrito.png'
  WHEN 80 THEN '/uploads/productos/producto_80_porci_n_de_papa_mediana.png'
  WHEN 81 THEN '/uploads/productos/producto_81_porci_n_de_papa_grande.png'
  WHEN 82 THEN '/uploads/productos/producto_82_porci_n_de_ensalada_cocida_mediana.png'
  WHEN 83 THEN '/uploads/productos/producto_83_porci_n_de_ensalada_cocida_grande.png'
  WHEN 8 THEN '/uploads/productos/producto_8_bambino_a_lo_pobre.png'
  WHEN 9 THEN '/uploads/productos/producto_9_combo_1_2_pollo___1_4_pollo_o_gaseosa_1.5l.png'
  ELSE imagen_url
END
WHERE id_producto IN (10,11,12,13,14,15,16,17,18,19,20,3,4,5,62,66,67,68,69,6,70,71,73,74,75,76,77,78,79,7,80,81,82,83,8,9);

UPDATE configuracion_media
SET url = CASE clave
  WHEN 'home_hero_banner' THEN '/uploads/configuracion/home_hero_banner/configuracion_imagen_1_home_hero_banner.webp'
  WHEN 'carta_pdf' THEN '/uploads/configuracion/carta_pdf/configuracion_pdf_3_carta_pdf.pdf'
  ELSE url
END,
public_id = NULL,
version_tag = NULL
WHERE clave IN ('home_hero_banner', 'carta_pdf');

COMMIT;
