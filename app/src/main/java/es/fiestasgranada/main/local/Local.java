package es.fiestasgranada.main.local;

public class Local {


    //---------ATRIBUTOS------------------------

    private int id;
    private String titulo;
    private String descripcion;
    private String fecha;
    private double latitud;
    private double longitud;
    private String URLImagen;
    private String abierto;


    //constructores

    public Local(int id, String titulo, String descripcion, String fecha, String URLImagen, double latitud, double longitud, String abierto) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.URLImagen = URLImagen;
        this.latitud = latitud;
        this.longitud = longitud;
        this.abierto = abierto;

    }

    //setters y getters

    public String getURLImagen() {
        return URLImagen;
    }

    public void setURLImagen(String URLImagen) {
        this.URLImagen = URLImagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String isAbierto() {
        return abierto;
    }

    public void setAbierto(String abierto) {
        this.abierto = abierto;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void getLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Local{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fecha=" + fecha +
                ", URLImagen='" + URLImagen + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", abierto=" + abierto +
                '}';
    }
}
