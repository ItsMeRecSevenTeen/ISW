-- Agrega baja lógica de productos (RF-05: "dar de baja lógica los productos del catálogo").
-- Todos los productos existentes quedan activos por defecto (sin pérdida de datos).
-- No se usa IF NOT EXISTS: no está soportado por la sintaxis de ADD COLUMN en MySQL 9.1.
USE db_isw;

SET @col_exists = (
  SELECT COUNT(*) FROM information_schema.columns
  WHERE table_schema = 'db_isw' AND table_name = 'producto' AND column_name = 'activo'
);

SET @sql = IF(@col_exists = 0,
  'ALTER TABLE `producto` ADD COLUMN `activo` TINYINT(1) NOT NULL DEFAULT 1 COMMENT ''Bandera lógica para mostrar/ocultar el producto del catálogo sin borrar su historial de ventas'' AFTER `fecha_caducidad`',
  'SELECT ''activo ya existe, no se hace nada'''
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
