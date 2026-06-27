-- Amplía configuracion.valor a VARCHAR(500) para poder guardar la lista de
-- "tipos de producto" del combo de NuevoProductoDialog como un solo valor
-- delimitado por '|', incluso cuando el usuario agrega 20+ tipos.
-- MODIFY es idempotente (re-ejecutarlo deja la misma definición) y no toca
-- las filas existentes; la fila IVA_PORCENTAJE queda intacta.
USE db_isw;

ALTER TABLE `configuracion`
  MODIFY COLUMN `valor` VARCHAR(500) NOT NULL
  COMMENT 'Valor asignado a la configuración paramétrica';
