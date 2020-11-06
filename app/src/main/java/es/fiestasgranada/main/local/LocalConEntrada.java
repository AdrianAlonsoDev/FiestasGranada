package es.fiestasgranada.main.local;

public class LocalConEntrada extends Local {

    //-----------------ATRIBUTOS NUEVOS---------------
    private double precioEntrada;
    private final String entradaIncluye;

    //constructor

    public LocalConEntrada(int id, String titulo, String descripcion, String fecha, String URLImagen, double latitud, double longitud, String abierto, double precioEntrada, String entradaIncluye) {
        super(id, titulo, descripcion, fecha, URLImagen, latitud, longitud, abierto);
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