package es.fiestasgranada.main.local;

import java.util.Date;

public class Local {



    //---------ATRIBUTOS------------------------

    private int id;
    private String titulo;
    private String descripcion;
    private String ubicacion;
    private Date fecha;
    private String URLImagen;
    private boolean abierto;



    //constructores

    public Local(int id, String titulo, String descripcion, String ubicacion, Date fecha, String URLImagen, boolean abierto) {
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

    public boolean isAbierto() {
        return abierto;
    }

    public void setAbierto(boolean abierto) {
        this.abierto = abierto;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
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
