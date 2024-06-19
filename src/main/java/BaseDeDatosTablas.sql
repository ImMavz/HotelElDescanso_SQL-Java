/*
CREATE TABLE SERVICIO (
    Id_servicio SERIAL PRIMARY KEY,
	nombre_servicio varchar (100),
    Costo INT NOT NULL

);
*/

/*
create table factura (
	N_Factura serial primary key,
	F_Chekin timestamp,
	F_CheckOut timestamp,
	Pago_total int	
)
*/
/*
create table detalle_factura (
	Servicio_Fac INT NOT NULL,
    Factura_Fac INT NOT NULL,
    FOREIGN KEY (Servicio_Fac) REFERENCES SERVICIO(Id_servicio),
    FOREIGN KEY (Factura_Fac) REFERENCES FACTURA(N_factura),
    PRIMARY KEY (Servicio_Fac, Factura_Fac)
)
*/
/*
create table tipo_habitacion (
	Tipo VARCHAR(50) PRIMARY KEY,
    Precio INT NOT NULL
)
*/
/*
create table habitacion (
	N_Habitacion INT PRIMARY KEY,
    Habitacion_Tipo VARCHAR(50) NOT NULL,
    Estado VARCHAR(50) NOT NULL,
    FOREIGN KEY (Habitacion_Tipo) REFERENCES TIPO_HABITACION(Tipo)
)
*/

/*
create table cliente (
	Cedula int primary key,
	Nombre varchar(100)
) 
*/

/*
create table clientes_habitauales (
	Cliente INT NOT NULL,
    Tipo_habitual VARCHAR(50) NOT NULL,
    FOREIGN KEY (Cliente) REFERENCES CLIENTE(Cedula)
)
*/
/*
create table esporadicos (
	Cliente_Espo int primary key,
	foreign key (Cliente_Espo) references Cliente(Cedula)
)
*/

/*
create table servicio_habitacion (
	Servicio_Hab INT NOT NULL,
    Habitacion_Hab INT NOT NULL,
    FOREIGN KEY (Servicio_Hab) REFERENCES SERVICIO(Id_servicio),
    FOREIGN KEY (Habitacion_Hab) REFERENCES HABITACION(N_Habitacion),
    PRIMARY KEY (Servicio_Hab, Habitacion_Hab)
)
*/

/*
create table reserva (
	N_Reserva serial primary key,
	Cliente_Reserva INT NOT NULL,
    Habitacion_Reserva INT NOT NULL,
    Fecha_entrada_Reserva DATE NOT NULL,
    N_Dias INT NOT NULL,
    FOREIGN KEY (Cliente_Reserva) REFERENCES CLIENTE(Cedula),
    FOREIGN KEY (Habitacion_Reserva) REFERENCES HABITACION(N_Habitacion)
)
*/
