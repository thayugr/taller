CREATE SCHEMA IF NOT EXISTS public;
ALTER SCHEMA public OWNER TO postgres;

CREATE SCHEMA IF NOT EXISTS academico;
ALTER SCHEMA academico OWNER TO postgres;

-- SCHEMA northwind
CREATE SCHEMA IF NOT EXISTS northwind;
ALTER SCHEMA northwind OWNER TO postgres;

-- FUNCIONES
CREATE OR REPLACE FUNCTION northwind.ccur(value numeric)
RETURNS numeric
LANGUAGE plpgsql
AS E'BEGIN RETURN ROUND(value::numeric, 2); END;'
;
ALTER FUNCTION northwind.ccur(value numeric) OWNER TO postgres;

CREATE OR REPLACE FUNCTION northwind.iif(condition boolean, true_result text, false_result text)
RETURNS text
LANGUAGE plpgsql
AS E'BEGIN
    IF condition THEN
        RETURN true_result;
    ELSE
        RETURN false_result;
    END IF;
END;'
;
ALTER FUNCTION northwind.iif(condition boolean, true_result text, false_result text) OWNER TO postgres;


CREATE OR REPLACE FUNCTION northwind."isnull"(value text)
RETURNS boolean
LANGUAGE plpgsql
AS E'BEGIN
    RETURN value IS NULL;
END;'
;
ALTER FUNCTION northwind."isnull"(value text) OWNER TO postgres;

-- TABLA CLIENTES
CREATE TABLE IF NOT EXISTS northwind."Clientes" (
    "Id" integer NOT NULL,
    "Compañía" character varying(50),
    "Apellidos" character varying(50),
    "Nombre" character varying(50),
    "Dirección de correo electrónico" character varying(50),
    "Cargo" character varying(50),
    "Teléfono del trabajo" character varying(25),
    "Teléfono particular" character varying(25),
    "Teléfono móvil" character varying(25),
    "Número de fax" character varying(25),
    "Dirección" text,
    "Ciudad" character varying(50),
    "Estado o provincia" character varying(50),
    "C Postal" character varying(15),
    "País o región" character varying(50),
    "Página Web" text,
    "Notas" text,
    "Datos adjuntos" text
);
ALTER TABLE northwind."Clientes" OWNER TO postgres;

-- VISTA CLIENTES AMPLIADOS
CREATE OR REPLACE VIEW northwind."Clientes ampliados" AS
SELECT 
    northwind.iif(northwind."isnull"(("Apellidos")::text),
        northwind.iif(northwind."isnull"(("Nombre")::text), ("Compañía")::text, ("Nombre")::text),
        northwind.iif(northwind."isnull"(("Nombre")::text), ("Apellidos")::text, (("Nombre")::text || ' ' || ("Apellidos")::text))
    ) AS nombredelcontacto,
    northwind.iif(northwind."isnull"(("Apellidos")::text),
        northwind.iif(northwind."isnull"(("Nombre")::text), ("Compañía")::text, ("Nombre")::text),
        northwind.iif(northwind."isnull"(("Nombre")::text), ("Apellidos")::text, (("Apellidos")::text || ', ' || ("Nombre")::text))
    ) AS archivarcomo,
    "Id" AS idcliente,
    "Compañía" AS compania,
    "Apellidos" AS apellidos,
    "Nombre" AS nombre,
    "Dirección de correo electrónico" AS correo,
    "Cargo" AS cargo,
    "Teléfono del trabajo" AS telefonotrabajo,
    "Teléfono particular" AS telefonoparticular,
    "Teléfono móvil" AS telefonomovil,
    "Número de fax" AS numerofax,
    "Dirección" AS direccion,
    "Ciudad" AS ciudad,
    "Estado o provincia" AS estadoprovincia,
    "C Postal" AS cpostal,
    "País o región" AS pais,
    "Página Web" AS web,
    "Notas" AS notas,
    "Datos adjuntos" AS datosabjuntos
FROM northwind."Clientes"
ORDER BY 
    northwind.iif(northwind."isnull"(("Apellidos")::text),
        northwind.iif(northwind."isnull"(("Nombre")::text), ("Compañía")::text, ("Nombre")::text),
        northwind.iif(northwind."isnull"(("Nombre")::text), ("Apellidos")::text, (("Apellidos")::text || ', ' || ("Nombre")::text))
    ),
    northwind.iif(northwind."isnull"(("Apellidos")::text),
        northwind.iif(northwind."isnull"(("Nombre")::text), ("Compañía")::text, ("Nombre")::text),
        northwind.iif(northwind."isnull"(("Nombre")::text), ("Apellidos")::text, (("Nombre")::text || ' ' || ("Apellidos")::text))
    )
;
ALTER VIEW northwind."Clientes ampliados" OWNER TO postgres;

-- TABLA DETALLES DE PEDIDO

CREATE TABLE IF NOT EXISTS northwind."Detalles de pedido" (
    "Id" integer,
    "Id de pedido" integer,
    "Id de producto" integer,
    "Cantidad" numeric(18,4),
    "Precio" character varying(255),
    "Descuento" double precision,
    "Id de situación" integer,
    "Fecha de asignación" timestamp with time zone,
    "Id de pedido de compra" integer,
    "Id de inventario" integer
);
ALTER TABLE northwind."Detalles de pedido" OWNER TO postgres;

-- TABLA ESTADO DETALLES DE PEDIDOS
CREATE TABLE IF NOT EXISTS northwind."Estado de detalles de pedidos" (
    "Id de situación" integer,
    "Nombre del estado" character varying(50)
);
ALTER TABLE northwind."Estado de detalles de pedidos" OWNER TO postgres;

-- VISTA DETALLES DE PEDIDOS AMPLIADOS
CREATE OR REPLACE VIEW northwind."Detalles de pedidos ampliados" AS
SELECT 
    dp."Id",
    dp."Id de pedido",
    dp."Id de producto",
    dp."Cantidad",
    dp."Precio",
    dp."Descuento",
    dp."Id de situación",
    dp."Fecha de asignación",
    dp."Id de pedido de compra",
    dp."Id de inventario",
    northwind.ccur(((dp."Cantidad" * dp."Precio"::numeric) * (1 - dp."Descuento"::numeric))) AS "Precio total",
    edp."Nombre del estado"
FROM northwind."Detalles de pedido" dp
LEFT JOIN northwind."Estado de detalles de pedidos" edp 
    ON dp."Id de situación" = edp."Id de situación";
ALTER VIEW northwind."Detalles de pedidos ampliados" OWNER TO postgres;

-- TABLA PEDIDOS
CREATE TABLE IF NOT EXISTS northwind."Pedidos" (
    "Id de pedido" integer,
    "Id de empleado" integer,
    "Id de cliente" integer,
    "Fecha de pedido" timestamp with time zone,
    "Fecha de envío" timestamp with time zone,
    "Id de transportista" integer,
    "Nombre de envío" character varying(50),
    "Dirección de envío" text,
    "Ciudad dest" character varying(50),
    "Edo/Prov dest" character varying(50),
    "Código postal de envío" character varying(50),
    "País o región de envío" character varying(50),
    "Gastos de envío" double precision,
    "Impuestos" double precision,
    "Tipo de pago" character varying(50),
    "Fecha de pago" timestamp with time zone,
    "Notas" text,
    "Tipo impositivo" double precision,
    "Estado de impuestos" smallint,
    "Id de situación" smallint
);
ALTER TABLE northwind."Pedidos" OWNER TO postgres;

-- VISTA SUBTOTALES DE PEDIDOS
CREATE OR REPLACE VIEW northwind."Subtotales de pedidos" AS
SELECT 
    "Id de pedido",
    SUM(northwind.ccur(((("Precio")::numeric * "Cantidad") * (1 - "Descuento"::numeric)))) AS subtotal
FROM northwind."Detalles de pedido"
GROUP BY "Id de pedido";
ALTER VIEW northwind."Subtotales de pedidos" OWNER TO postgres;

-- VISTA DIEZ MEJORES PEDIDOS
CREATE OR REPLACE VIEW northwind."Diez mejores pedidos por importe de ventas" AS
SELECT DISTINCT sp.subtotal AS "ImporteVentas",
    p."Id de pedido" AS idpedido,
    (to_char(p."Fecha de pedido", 'dd/mm/YYYY'))::date AS fechapedido,
    ca.compania AS nombrecompania,
    (to_char(p."Fecha de envío", 'dd/mm/YYYY'))::date AS fechaenvio
FROM northwind."Clientes ampliados" ca
JOIN northwind."Pedidos" p ON ca.idcliente = p."Id de cliente"
JOIN northwind."Subtotales de pedidos" sp ON p."Id de pedido" = sp."Id de pedido"
ORDER BY sp.subtotal DESC
LIMIT 10;
ALTER VIEW northwind."Diez mejores pedidos por importe de ventas" OWNER TO postgres;

-- TABLA ESTADO DE PEDIDOS
CREATE TABLE IF NOT EXISTS northwind."Estado de pedidos" (
    "Id de situación" smallint,
    "Nombre del estado" character varying(50)
);
ALTER TABLE northwind."Estado de pedidos" OWNER TO postgres;

-- VISTA MIVISTA
CREATE OR REPLACE VIEW northwind."MIVISTA" AS
SELECT 
    "Id",
    "Compañía",
    "Apellidos",
    "Nombre",
    "Dirección de correo electrónico",
    "Cargo",
    "Teléfono del trabajo",
    "Teléfono particular",
    "Teléfono móvil",
    "Número de fax",
    "Dirección",
    "Ciudad",
    "Estado o provincia",
    "C Postal",
    "País o región",
    "Página Web",
    "Notas",
    "Datos adjuntos"
FROM northwind."Clientes" c
WHERE "Id" IN (1,2,3);
ALTER VIEW northwind."MIVISTA" OWNER TO postgres;

-- VISTA TOTALES DE PRECIOS DE PEDIDOS
CREATE OR REPLACE VIEW northwind."Totales de precios de pedidos" AS
SELECT 
    "Id de pedido" AS idpedido,
    SUM("Precio total") AS "Precio total"
FROM northwind."Detalles de pedidos ampliados"
GROUP BY "Id de pedido";
ALTER VIEW northwind."Totales de precios de pedidos" OWNER TO postgres;

-- VISTA RESUMEN DE PEDIDOS
CREATE OR REPLACE VIEW northwind."Resumen de pedidos" AS
SELECT 
    p."Id de pedido" AS iddepedido,
    p."Id de empleado" AS iddeempleado,
    p."Id de cliente" AS iddecliente,
    p."Fecha de pedido" AS fechadepedido,
    p."Fecha de envío" AS fechadeenvio,
    tp."Precio total" AS subtotal,
    p."Gastos de envío" AS gastosdeenvio,
    p."Impuestos" AS impuestos,
    (tp."Precio total"::double precision + p."Gastos de envío" + p."Impuestos") AS totaldepedido,
    p."Nombre de envío" AS nombredeenvio,
    p."Dirección de envío" AS direcciondeenvio,
    p."Fecha de pago" AS fechadepago,
    ep."Nombre del estado" AS estado
FROM northwind."Pedidos" p
LEFT JOIN northwind."Totales de precios de pedidos" tp ON p."Id de pedido" = tp.idpedido
LEFT JOIN northwind."Estado de pedidos" ep ON p."Id de situación" = ep."Id de situación"
ORDER BY p."Id de pedido" DESC;
ALTER VIEW northwind."Resumen de pedidos" OWNER TO postgres;

CREATE TABLE IF NOT EXISTS academico.datos (
  cod    int4 NOT NULL, 
  cedula int4 NOT NULL, 
  CONSTRAINT datos_pkey 
    PRIMARY KEY (cod));

CREATE TABLE IF NOT EXISTS   academico.dicta (
  codpar  int4 NOT NULL, 
  codp    int4 NOT NULL, 
  codmat  varchar(15) NOT NULL, 
  gestion int4 NOT NULL, 
  ids     int4 NOT NULL, 
  CONSTRAINT dicta_pkey 
    PRIMARY KEY (codpar, 
  codp, 
  codmat, 
  gestion));

CREATE TABLE IF NOT EXISTS academico.dmodalidad (
  coddm  SERIAL NOT NULL, 
  nombre text NOT NULL UNIQUE, 
  estado int4, 
  codm   int4 NOT NULL UNIQUE, 
  CONSTRAINT dmodalidad_pkey 
    PRIMARY KEY (coddm));

CREATE TABLE IF NOT EXISTS  academico.general (
  ids         int4 NOT NULL, 
  gestion     int4, 
  create_time timestamp(6), 
  update_time timestamp(6), 
  CONSTRAINT general_pkey 
    PRIMARY KEY (ids));

CREATE TABLE IF NOT EXISTS academico.itemat (
  ponderacion int4, 
  codmat      varchar(15) NOT NULL, 
  codi        int4 NOT NULL, 
  estado      int4, 
  gestion     int4 NOT NULL, 
  CONSTRAINT itemat_pkey 
    PRIMARY KEY (codmat, 
  codi, 
  gestion));

CREATE TABLE IF NOT EXISTS academico.items (
  codi   SERIAL NOT NULL, 
  nombre varchar(15), 
  estado int4, 
  CONSTRAINT items_pkey 
    PRIMARY KEY (codi));

CREATE TABLE IF NOT EXISTS academico.mapa (
  codmat  varchar(15) NOT NULL, 
  codpar  int4 NOT NULL, 
  estado  int4, 
  gestion int4 NOT NULL, 
  CONSTRAINT mapa_pkey 
    PRIMARY KEY (codmat, 
  codpar, 
  gestion));

CREATE TABLE IF NOT EXISTS academico.materia (
  codmat varchar(15) NOT NULL, 
  nombre varchar(30), 
  codn   int4 NOT NULL, 
  estado int4, 
  CONSTRAINT materia_pkey 
    PRIMARY KEY (codmat));

CREATE TABLE IF NOT EXISTS academico.modalidad (
  codm   SERIAL NOT NULL, 
  nombre text, 
  estado int4, 
  CONSTRAINT modalidad_pkey 
    PRIMARY KEY (codm));

CREATE TABLE IF NOT EXISTS academico.notas (
  codmat    varchar(15) NOT NULL, 
  codi      int4 NOT NULL, 
  coda      int4 NOT NULL, 
  codp      int4 NOT NULL, 
  gestion   int4 NOT NULL, 
  idusuario int4 NOT NULL, 
  nota      int4 NOT NULL, 
  coddm     int4, 
  CONSTRAINT notas_pkey 
    PRIMARY KEY (codmat, 
  codi, 
  codp, 
  gestion, 
  idusuario));

CREATE TABLE IF NOT EXISTS academico.paralelo (
  codp   SERIAL NOT NULL, 
  nombre varchar(50), 
  estado int4, 
  CONSTRAINT paralelo_pkey 
    PRIMARY KEY (codp));

CREATE TABLE IF NOT EXISTS academico.personal (
  idusuario   int4 NOT NULL, 
  nombre      varchar(100) NOT NULL, 
  ap          varchar(100), 
  am          varchar(100), 
  estado      int2, 
  fnac        date, 
  ecivil      varchar(30), 
  genero      varchar(10), 
  dir         varchar(200), 
  telf        varchar(30), 
  tipo        varchar(30), 
  foto        varchar(255), 
  create_time timestamp(6), 
  update_time timestamp(6), 
  CONSTRAINT personal_pkey 
    PRIMARY KEY (idusuario));

CREATE TABLE IF NOT EXISTS academico.progra (
  codpar  int4 NOT NULL, 
  codp    int4 NOT NULL, 
  codmat  varchar(15) NOT NULL, 
  gestion int4 NOT NULL, 
  ids     int4 NOT NULL, 
  CONSTRAINT progra_pkey 
    PRIMARY KEY (codpar, 
  codp, 
  codmat, 
  gestion));

CREATE TABLE IF NOT EXISTS "public".sys_log (
  id               serial NOT NULL, 
  log_type         varchar(2), 
  method           varchar(100), 
  params           varchar(255), 
  time             int8, 
  ip               varchar(20), 
  username         varchar(100), 
  exception_detail text, 
  create_time      timestamp(6), 
  description      varchar(255), 
  CONSTRAINT sys_log_pkey 
    PRIMARY KEY (id));
COMMENT ON COLUMN "public".sys_log.id IS 'Clave Primaria';
COMMENT ON COLUMN "public".sys_log.log_type IS 'Tipo de registro (1 normal 2 error)';
COMMENT ON COLUMN "public".sys_log.method IS 'Método de solicitud';
COMMENT ON COLUMN "public".sys_log.params IS 'Solicitar parámetros';
COMMENT ON COLUMN "public".sys_log.time IS 'Tiempo necesario (milisegundos)';
COMMENT ON COLUMN "public".sys_log.ip IS 'IP';
COMMENT ON COLUMN "public".sys_log.username IS 'Solicitar nombre de usuario';
COMMENT ON COLUMN "public".sys_log.exception_detail IS 'Detalles del mensaje de error';
COMMENT ON COLUMN "public".sys_log.create_time IS 'tiempo de creación';
COMMENT ON COLUMN "public".sys_log.description IS 'describir';

CREATE TABLE IF NOT EXISTS "public".sys_menu (
  id          serial NOT NULL, 
  name        varchar(40), 
  parent_id   int8, 
  path        varchar(100), 
  type        varchar(2), 
  sort        int4, 
  create_time timestamp(6), 
  update_time timestamp(6), 
  component   varchar(100), 
  permission  varchar(100), 
  icon        varchar(100), 
  CONSTRAINT sys_menu_pkey 
    PRIMARY KEY (id));
COMMENT ON COLUMN "public".sys_menu.id IS 'Clave Primaria ID';
COMMENT ON COLUMN "public".sys_menu.name IS 'nombre ';
COMMENT ON COLUMN "public".sys_menu.parent_id IS 'padre ID';
COMMENT ON COLUMN "public".sys_menu.path IS 'url';
COMMENT ON COLUMN "public".sys_menu.type IS 'Tipo (1 menú 2 páginas 3 botones)';
COMMENT ON COLUMN "public".sys_menu.sort IS 'clasificar';
COMMENT ON COLUMN "public".sys_menu.create_time IS 'tiempo de creacion';
COMMENT ON COLUMN "public".sys_menu.update_time IS 'hora de modificacion';
COMMENT ON COLUMN "public".sys_menu.component IS 'componentes';
COMMENT ON COLUMN "public".sys_menu.permission IS 'ID de permiso';
COMMENT ON COLUMN "public".sys_menu.icon IS 'icono';

CREATE TABLE IF NOT EXISTS "public".sys_role (
  id          serial NOT NULL, 
  role_code   varchar(20), 
  role_name   varchar(20), 
  create_time timestamp(6), 
  description varchar(200), 
  update_time timestamp(6), 
  CONSTRAINT sys_role_pkey 
    PRIMARY KEY (id));
COMMENT ON COLUMN "public".sys_role.id IS 'clave primaria';
COMMENT ON COLUMN "public".sys_role.role_code IS 'código de rol';
COMMENT ON COLUMN "public".sys_role.role_name IS 'Nombre del personaje';
COMMENT ON COLUMN "public".sys_role.create_time IS 'tiempo de creación';
COMMENT ON COLUMN "public".sys_role.description IS 'Observación';
COMMENT ON COLUMN "public".sys_role.update_time IS 'hora de modificacion';

CREATE TABLE IF NOT EXISTS "public".sys_role_menu (
  id          serial NOT NULL, 
  role_id     int4 NOT NULL, 
  menu_id     int4 NOT NULL, 
  create_time timestamp(6), 
  update_time timestamp(6), 
  CONSTRAINT sys_role_menu_pkey 
    PRIMARY KEY (id));
COMMENT ON COLUMN "public".sys_role_menu.id IS 'clave primaria';
COMMENT ON COLUMN "public".sys_role_menu.role_id IS 'Role ID';
COMMENT ON COLUMN "public".sys_role_menu.menu_id IS 'Menu ID';
COMMENT ON COLUMN "public".sys_role_menu.create_time IS 'tiempo de creación';
COMMENT ON COLUMN "public".sys_role_menu.update_time IS 'hora de modificación';

CREATE TABLE IF NOT EXISTS   "public".sys_role_user (
  id          serial NOT NULL, 
  user_id     int4 NOT NULL, 
  role_id     int4 NOT NULL, 
  create_time timestamp(6), 
  update_time timestamp(6), 
  CONSTRAINT sys_role_user_pkey 
    PRIMARY KEY (id));
COMMENT ON COLUMN "public".sys_role_user.id IS 'clave primaria';
COMMENT ON COLUMN "public".sys_role_user.user_id IS 'ID de usuario';
COMMENT ON COLUMN "public".sys_role_user.role_id IS 'ID de rol';
COMMENT ON COLUMN "public".sys_role_user.create_time IS 'tiempo de creacion';
COMMENT ON COLUMN "public".sys_role_user.update_time IS 'hora de modificación';

CREATE TABLE IF NOT EXISTS "public".sys_user (
  id          serial NOT NULL, 
  email       varchar(100), 
  username    varchar(50), 
  password    varchar(100), 
  create_time timestamp(6), 
  enabled     bool, 
  nick_name   varchar(50), 
  update_time timestamp(6), 
  CONSTRAINT sys_user_pkey 
    PRIMARY KEY (id));
COMMENT ON COLUMN "public".sys_user.id IS 'clave primaria';
COMMENT ON COLUMN "public".sys_user.email IS 'Correo';
COMMENT ON COLUMN "public".sys_user.username IS 'nombre de usuario';
COMMENT ON COLUMN "public".sys_user.password IS 'contraseña';
COMMENT ON COLUMN "public".sys_user.create_time IS 'tiempo de creación';
COMMENT ON COLUMN "public".sys_user.enabled IS 'Estado (0 deshabilitado 1 habilitado)';
COMMENT ON COLUMN "public".sys_user.nick_name IS 'apodo';
COMMENT ON COLUMN "public".sys_user.update_time IS 'hora de modificacion';
CREATE TABLE IF NOT EXISTS academico.niveles (
  codn   SERIAL NOT NULL, 
  nombre varchar(10), 
  estado int4 NOT NULL, 
  PRIMARY KEY (codn));
  
COMMENT ON TABLE academico.niveles IS 'niveles en los que se encuentra una materia dentro de una gestion
Ejemplo 1er año, 2do año,etc';


ALTER TABLE academico.datos ADD CONSTRAINT  FKdatos961161 FOREIGN KEY (cod) REFERENCES academico.personal (idusuario) ON UPDATE No action ON DELETE No action;
ALTER TABLE academico.dmodalidad ADD CONSTRAINT  FKdmodalidad495087 FOREIGN KEY (codm) REFERENCES academico.modalidad (codm) ON UPDATE No action ON DELETE No action;
ALTER TABLE academico.itemat ADD CONSTRAINT  FKitemat71514 FOREIGN KEY (codmat) REFERENCES academico.materia (codmat) ON UPDATE No action ON DELETE No action;
ALTER TABLE academico.itemat ADD CONSTRAINT  FKitemat17928 FOREIGN KEY (codi) REFERENCES academico.items (codi) ON UPDATE No action ON DELETE No action;
ALTER TABLE academico.mapa ADD CONSTRAINT  FKmapa893647 FOREIGN KEY (codmat) REFERENCES academico.materia (codmat) ON UPDATE No action ON DELETE No action;
ALTER TABLE academico.mapa ADD CONSTRAINT  FKmapa281903 FOREIGN KEY (codpar) REFERENCES academico.paralelo (codp) ON UPDATE No action ON DELETE No action;
ALTER TABLE academico.notas ADD CONSTRAINT  FKnotas375160 FOREIGN KEY (coddm) REFERENCES academico.dmodalidad (coddm) ON UPDATE No action ON DELETE No action;
ALTER TABLE "public".sys_role_menu ADD CONSTRAINT  FKsys_role_m751229 FOREIGN KEY (menu_id) REFERENCES "public".sys_menu (id);
ALTER TABLE "public".sys_role_menu ADD CONSTRAINT  FKsys_role_m429064 FOREIGN KEY (role_id) REFERENCES "public".sys_role (id);
ALTER TABLE "public".sys_role_user ADD CONSTRAINT  FKsys_role_u680564 FOREIGN KEY (role_id) REFERENCES "public".sys_role (id);
ALTER TABLE "public".sys_role_user ADD CONSTRAINT  FKsys_role_u244959 FOREIGN KEY (user_id) REFERENCES "public".sys_user (id);
ALTER TABLE academico.personal ADD CONSTRAINT  FKpersonal285970 FOREIGN KEY (idusuario) REFERENCES "public".sys_user (id);
ALTER TABLE academico.progra ADD CONSTRAINT  FKprogra797092 FOREIGN KEY (ids) REFERENCES academico.personal (idusuario);
ALTER TABLE academico.progra ADD CONSTRAINT  FKprogra471658 FOREIGN KEY (ids) REFERENCES "public".sys_user (id);
ALTER TABLE academico.progra ADD CONSTRAINT  FKprogra956668 FOREIGN KEY (codmat, codpar, gestion) REFERENCES academico.mapa (codmat, codpar, gestion);
ALTER TABLE academico.notas ADD CONSTRAINT  FKnotas90528 FOREIGN KEY (coda, codp, codmat, gestion) REFERENCES academico.progra (codpar, codp, codmat, gestion);
ALTER TABLE academico.general ADD CONSTRAINT  FKgeneral135885 FOREIGN KEY (ids) REFERENCES "public".sys_user (id);
ALTER TABLE academico.dicta ADD CONSTRAINT  FKdicta623284 FOREIGN KEY (codmat, codpar, gestion) REFERENCES academico.mapa (codmat, codpar, gestion);
ALTER TABLE academico.dicta ADD CONSTRAINT  FKdicta863295 FOREIGN KEY (ids) REFERENCES "public".sys_user (id);
ALTER TABLE academico.materia ADD CONSTRAINT  FKmateria609743 FOREIGN KEY (codn) REFERENCES academico.niveles (codn);
