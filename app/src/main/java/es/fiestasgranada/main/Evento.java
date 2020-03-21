package es.fiestasgranada.main;

import java.util.Date;

public class Evento {



    //---------ATRIBUTOS------------------------

    private int id;
    private String titulo;
    private String descripcion;
    private Local ubicacion;
    private Date fecha;



    //constructores

    public Evento(int id, String titulo, String descripcion, Local ubicacion, Date fecha) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
    }


    //setters y getters


    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Local getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Local ubicacion) {
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
}
