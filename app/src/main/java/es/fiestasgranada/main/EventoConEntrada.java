package es.fiestasgranada.main;

import java.util.Date;

public class EventoConEntrada extends Evento{

    //-----------------ATRIBUTOS NUEVOS---------------
    private double precioEntrada;
    private String entradaIncluye;

    //constructor

    public EventoConEntrada(String titulo, String descripcion, Local ubicacion, Date fecha, double precioEntrada, String entradaIncluye) {
        super(titulo, descripcion, ubicacion, fecha);
        this.precioEntrada = precioEntrada;
        this.entradaIncluye = entradaIncluye;
    }


    //getters y setters


    public double getPrecioEntrada() {
        return precioEntrada;
    }

    public void setPrecioEntrada(double precioEntrada) {
        this.precioEntrada = precioEntrada;
    }

    public String getEntradaIncluye() {
        return entradaIncluye;
    }

    public void setEntradaIncluye(String entradaIncluye) {
        this.entradaIncluye = entradaIncluye;
    }

}