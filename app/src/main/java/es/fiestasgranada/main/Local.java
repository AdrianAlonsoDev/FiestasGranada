package es.fiestasgranada.main;

public class Local {

    //---------------ATRIBUTOS------------------

    private int id;
    private String nombre;
    private String descripcion;
    private boolean abierto;
    private String ubicacion;


    //constructores

    public Local(int id, String nombre, String descripcion, boolean abierto, String ubicacion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.abierto = abierto;
        this.ubicacion = ubicacion;
    }


    //getters y setters


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
