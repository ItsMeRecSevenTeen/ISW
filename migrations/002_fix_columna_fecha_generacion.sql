-- La columna 2 de `reporte` se creó con bytes UTF-8 mal doblados ("fecha_generación"
-- quedó almacenada como mojibake), lo que provoca "Unknown column" al insertar desde
-- una conexión JDBC correctamente codificada en UTF-8. Se renombra a un nombre ASCII
-- (consistente con el resto de columnas del esquema, ninguna otra usa acentos) usando
-- la posición ordinal para no depender de poder escribir el nombre corrupto exacto.
USE db_isw;

SET @old_col := (
  SELECT column_name FROM information_schema.columns
  WHERE table_schema = 'db_isw' AND table_name = 'reporte' AND ordinal_position = 2
);

SET @needs_rename := (@old_col <> 'fecha_generacion');

SET @sql := IF(@needs_rename,
  CONCAT('ALTER TABLE `reporte` CHANGE COLUMN `', @old_col, '` `fecha_generacion` DATETIME DEFAULT NULL COMMENT ''Fecha en el que se genero el reporte''') ,
  'SELECT ''fecha_generacion ya esta correcta, no se hace nada'' AS resultado'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
