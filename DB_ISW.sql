CREATE TABLE `producto` (
  `id_producto` INT PRIMARY KEY AUTO_INCREMENT COMMENT 'Identificador único del producto',
  `sku` VARCHAR(10) UNIQUE NOT NULL COMMENT 'Código único para identificación interna del producto',
  `nombre` VARCHAR(100) NOT NULL COMMENT 'Nombre comercial y descriptivo del producto',
  `precio_compra` DECIMAL(10,2) NOT NULL COMMENT 'Costo de adquisición del producto',
  `precio_venta` DECIMAL(10,2) NOT NULL COMMENT 'Precio de venta al público final',
  `stock_actual` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT 'Existencia física actual en el inventario',
  `stock_minimo` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT 'Cantidad mínima permitida antes de reabastecer',
  `codigo_barras` VARCHAR(20) UNIQUE COMMENT 'Código de barras para lectura mediante escáner',
  `es_granel` BOOLEAN NOT NULL COMMENT 'Naturaleza de la venta del producto',
  `precio_por_kg` DECIMAL(10,2) COMMENT 'Precio aplicable por kilogramo (exclusivo para granel)',
  `contenido_neto` VARCHAR(50) COMMENT 'Peso, volumen o cantidad contenida en el empaque',
  `marca` VARCHAR(50) COMMENT 'Marca comercial fabricante del producto',
  `es_refrigerable` BOOLEAN COMMENT 'Indicador de si requiere almacenamiento en frío',
  `fecha_caducidad` DATE COMMENT 'Fecha máxima para el consumo o venta del producto',
  `activo` TINYINT(1) NOT NULL DEFAULT 1 COMMENT 'Bandera lógica para mostrar/ocultar el producto del catálogo sin borrar su historial de ventas'
);

CREATE TABLE `usuario` (
  `id_usuario` INT PRIMARY KEY AUTO_INCREMENT COMMENT 'Identificador interno del usuario',
  `nombre_usuario` VARCHAR(50) UNIQUE NOT NULL COMMENT 'Credencial de acceso (username) del empleado',
  `contrasena_hash` VARCHAR(255) NOT NULL COMMENT 'Hash de seguridad bcrypt (factor 10) de la contraseña',
  `rol` tinyint NOT NULL COMMENT 'Nivel de acceso y permisos dentro del sistema, 0: administrador, 1: cajero',
  `activo` BOOLEAN NOT NULL DEFAULT true COMMENT 'Bandera lógica para permitir o denegar el acceso'
);

CREATE TABLE `turno_caja` (
  `id_turno` INT PRIMARY KEY AUTO_INCREMENT COMMENT 'Identificador consecutivo del turno operativo',
  `id_cajero` INT NOT NULL COMMENT 'Identificador del cajero que operó este turno',
  `fecha_apertura` DATETIME NOT NULL COMMENT 'Estampa de tiempo de apertura de la caja',
  `fecha_cierre` DATETIME COMMENT 'Estampa de tiempo de cierre de la caja',
  `fondo_inicial` DECIMAL(10,2) NOT NULL COMMENT 'Dinero base con el que se aperturó la caja',
  `total_ventas` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT 'Acumulado de transacciones realizadas en el turno',
  `diferencia` DECIMAL(10,2) COMMENT 'Sobrante o faltante de efectivo al cierre (cuadre)',
  `estaAbierta` tinyint NOT NULL COMMENT 'Situación operativa actual del turno, 0: Cerrado, 1: Abierto'
);

CREATE TABLE `venta` (
  `id_venta` INT PRIMARY KEY AUTO_INCREMENT COMMENT 'Folio o identificador único de la transacción de venta',
  `id_usuario` INT NOT NULL COMMENT 'Cajero que procesó la venta',
  `id_turno` INT NOT NULL COMMENT 'Turno de caja durante el cual se emitió el ticket',
  `fecha_hora` DATETIME NOT NULL DEFAULT (CURRENT_TIMESTAMP) COMMENT 'Estampa de tiempo en que se concretó la venta',
  `total_sin_iva` DECIMAL(10,2) NOT NULL COMMENT 'Monto subtotal de la venta antes de impuestos',
  `iva_porcentaje` DECIMAL(5,2) NOT NULL COMMENT 'Tasa porcentual de impuesto aplicada',
  `iva_monto` DECIMAL(10,2) NOT NULL COMMENT 'Monto fiscal calculado (impuesto a retener)',
  `total` DECIMAL(10,2) NOT NULL COMMENT 'Gran total de la transacción cobrada al cliente',
  `monto_recibido` DECIMAL(10,2) NOT NULL COMMENT 'Efectivo o monto entregado por el cliente',
  `cambio` DECIMAL(10,2) NOT NULL COMMENT 'Monto devuelto o vuelto al cliente',
  `estado` tinyint NOT NULL COMMENT 'Situación legal de la venta, 0: Anulada, 1: Completada'
);

CREATE TABLE `detalle_venta` (
  `id_detalle` INT PRIMARY KEY AUTO_INCREMENT COMMENT 'Identificador de la partida en el ticket',
  `id_venta` INT NOT NULL COMMENT 'Identificador de la venta cabecera',
  `id_producto` INT NOT NULL COMMENT 'Producto o artículo facturado',
  `cantidad` DECIMAL(10,3) NOT NULL COMMENT 'Unidades o kilogramos vendidos',
  `precio_unitario` DECIMAL(10,2) NOT NULL COMMENT 'Costo unitario cobrado al momento del ticket',
  `subtotal` DECIMAL(10,2) NOT NULL COMMENT 'Resultado de multiplicar cantidad por precio unitario'
);

CREATE TABLE `bitacora_logs` (
  `id_log` INT PRIMARY KEY AUTO_INCREMENT COMMENT 'Folio de la bitácora de movimiento',
  `id_producto` INT NOT NULL COMMENT 'Identificador único del producto',
  `fecha_registro` DATETIME NOT NULL DEFAULT (CURRENT_TIMESTAMP) COMMENT 'Momento exacto en que ocurrió el movimiento',
  `tipo_evento` VARCHAR(30) COMMENT 'Valor que establece que tipo de evento sucedió',
  `descripcion` VARCHAR(255) COMMENT 'Especificación de lo que sucedió'
);

CREATE TABLE `configuracion` (
  `clave` VARCHAR(50) PRIMARY KEY COMMENT 'Identificador nominal de la variable de sistema',
  `valor` VARCHAR(255) NOT NULL COMMENT 'Valor asignado a la configuración paramétrica'
);

CREATE TABLE `reporte` (
  `id_reporte` INT PRIMARY KEY AUTO_INCREMENT COMMENT 'Identificador de la partida en el ticket',
  `fecha_generacion` DATETIME COMMENT 'Fecha en el que se generó el reporte',
  `total_ventas_brutas` DECIMAL(10,2) COMMENT 'Cuántas ventas se hicieron en total',
  `ganancia_neta` DECIMAL(10,2) COMMENT 'Cuánto se ganó en total',
  `tipo_reporte` VARCHAR(20) COMMENT 'Qué tipo de reporte se está expidiendo'
);

ALTER TABLE `producto` COMMENT = 'Catálogo principal de productos y su inventario';

ALTER TABLE `usuario` COMMENT = 'Empleados y administradores con acceso al POS';

ALTER TABLE `turno_caja` COMMENT = 'Registro y cuadre de turnos operativos de caja';

ALTER TABLE `venta` COMMENT = 'Cabeceras de tickets de transacciones de venta';

ALTER TABLE `detalle_venta` COMMENT = 'Líneas o partidas que componen el detalle de la venta';

ALTER TABLE `bitacora_logs` COMMENT = 'Bitácora inmutable de entradas y salidas de inventario';

ALTER TABLE `configuracion` COMMENT = 'Diccionario de variables del entorno y configuración';

ALTER TABLE `reporte` COMMENT = 'Tabla donde se almacenan los datos para generar el archivo "reporte.pdf"';

ALTER TABLE `turno_caja` ADD CONSTRAINT `fk_turno_usuario` FOREIGN KEY (`id_cajero`) REFERENCES `usuario` (`id_usuario`);

ALTER TABLE `venta` ADD CONSTRAINT `fk_venta_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`);

ALTER TABLE `venta` ADD CONSTRAINT `fk_venta_turno` FOREIGN KEY (`id_turno`) REFERENCES `turno_caja` (`id_turno`);

ALTER TABLE `detalle_venta` ADD CONSTRAINT `fk_detalle_venta` FOREIGN KEY (`id_venta`) REFERENCES `venta` (`id_venta`);

ALTER TABLE `detalle_venta` ADD CONSTRAINT `fk_detalle_producto` FOREIGN KEY (`id_producto`) REFERENCES `producto` (`id_producto`);

ALTER TABLE `bitacora_logs` ADD CONSTRAINT `fk_log_producto` FOREIGN KEY (`id_producto`) REFERENCES `producto` (`id_producto`);

-- Disable foreign key checks for INSERT
SET FOREIGN_KEY_CHECKS = 0;

INSERT INTO `configuracion` (`clave`, `valor`)
VALUES
  ('IVA_PORCENTAJE', '16');

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

USE db_isw;
INSERT INTO usuario (nombre_usuario, contrasena_hash, rol, activo) 
VALUES ('admin', SHA2('admin123', 256), 0, true); -- 0 administrador, 1 cajero
INSERT INTO configuracion (clave, valor) VALUES ('IVA_PORCENTAJE', '16');