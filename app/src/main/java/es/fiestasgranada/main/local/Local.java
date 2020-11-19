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
    private String URLIcono;
    private String abierto;
    private String direccion;
    private String horario;


    //constructores

    public Local(int id, String titulo, String descripcion, String fecha, String URLImagen, String URLIcono, double latitud, double longitud, String abierto, String direccion, String horario) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.URLImagen = URLImagen;
        this.URLIcono = URLIcono;
        this.latitud = latitud;
        this.longitud = longitud;
        this.abierto = abierto;
        this.direccion = direccion;
        this.horario = horario;

    }

    //setters y getters

    public String getURLImagen() {
        return URLImagen;
    }

    public void setURLImagen(String URLImagen) {
        this.URLImagen = URLImagen;
    }

    public String getURLIcono() {
        return URLIcono;
    }

    public void setURLIcono(String URLIcono) {
        this.URLIcono = URLIcono;
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

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
