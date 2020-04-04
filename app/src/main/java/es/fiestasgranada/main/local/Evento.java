package es.fiestasgranada.main.local;

import java.util.Date;

public class Evento {

    //---------ATRIBUTOS------------------------

    private int id;
    private String titulo;
    private String descripcion;
    private Date fecha;
    private String URLImagen;
    private boolean activo;



    //constructores

    public Evento(int id, String titulo, String descripcion , Date fecha, String URLImagen, boolean abierto) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.URLImagen = URLImagen;
        this.activo =  abierto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getURLImagen() {
        return URLImagen;
    }

    public void setURLImagen(String URLImagen) {
        this.URLImagen = URLImagen;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Evento{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fecha=" + fecha +
                ", URLImagen='" + URLImagen + '\'' +
                ", activo=" + activo +
                '}';
    }
}
