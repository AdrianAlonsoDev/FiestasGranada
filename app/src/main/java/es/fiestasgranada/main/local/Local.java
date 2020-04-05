package es.fiestasgranada.main.local;

public class Local {



    //---------ATRIBUTOS------------------------

    private int id;
    private String titulo;
    private String descripcion;
    private String ubicacion;
    private String fecha;
    private String URLImagen;
    private String abierto;



    //constructores

    public Local(int id, String titulo, String descripcion, String ubicacion, String fecha, String URLImagen, String abierto) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.URLImagen = URLImagen;
        this.abierto =  abierto;
        this.ubicacion = ubicacion;
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

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
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
                ", ubicacion=" + ubicacion +
                ", fecha=" + fecha +
                ", URLImagen='" + URLImagen + '\'' +
                ", abierto=" + abierto +
                '}';
    }
}
