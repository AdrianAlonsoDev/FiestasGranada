package es.fiestasgranada.main.local;

import android.widget.RatingBar;

public abstract class Local {


    //Atributos
    public int uid;
    public String titulo;
    public String descripcion;
    public String ultimaFecha;
    public double latitud;
    public double longitud;
    public String URLImagen;
    public String URLIcono;
    public String abierto;
    public String direccion;
    public String horario;


    //Constructores
    public Local(int uid, String titulo, String descripcion, String ultimaFecha, String URLImagen, String URLIcono, double latitud, double longitud, String abierto, String direccion, String horario) {
        this.uid = uid;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.ultimaFecha = ultimaFecha;
        this.URLImagen = URLImagen;
        this.URLIcono = URLIcono;
        this.latitud = latitud;
        this.longitud = longitud;
        this.abierto = abierto;
        this.direccion = direccion;
        this.horario = horario;
    }

    public Local() {

    }

    //Setters y Getters

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

    public String getUltimaFecha() {
        return ultimaFecha;
    }

    public void setUltimaFecha(String ultimaFecha) {
        this.ultimaFecha = ultimaFecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId() {
        return uid;
    }

    public void setId(int id) {
        this.uid = uid;
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

    public abstract RatingBar getRating();
}
