package es.fiestasgranada.main.local;

import java.util.Date;

public class LocalConEntrada extends Local {

    //-----------------ATRIBUTOS NUEVOS---------------
    private double precioEntrada;
    private String entradaIncluye;

    //constructor

    public LocalConEntrada(int id, String titulo, String descripcion, String ubicacion, String fecha, String URLImagen, double precioEntrada, String entradaIncluye, String abierto) {
        super(id, titulo, descripcion, ubicacion, fecha, URLImagen, abierto);
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
        return "LocalConEntrada{" +
                "precioEntrada=" + precioEntrada +
                ", entradaIncluye='" + entradaIncluye + '\'' +
                '}';
    }
}