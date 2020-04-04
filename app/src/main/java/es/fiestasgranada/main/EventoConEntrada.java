package es.fiestasgranada.main;

import java.util.Date;

public class EventoConEntrada extends Evento{

    //-----------------ATRIBUTOS NUEVOS---------------
    private double precioEntrada;
    private String entradaIncluye;

    //constructor

    public EventoConEntrada(int id, String titulo, String descripcion, Local ubicacion, Date fecha, String URLImagen, double precioEntrada, String entradaIncluye) {
        super(id, titulo, descripcion, ubicacion, fecha, URLImagen);
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

    @Override
    public String toString() {
        return "EventoConEntrada{" +
                "precioEntrada=" + precioEntrada +
                ", entradaIncluye='" + entradaIncluye + '\'' +
                '}';
    }
}