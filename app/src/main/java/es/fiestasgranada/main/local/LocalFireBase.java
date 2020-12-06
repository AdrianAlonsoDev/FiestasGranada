package es.fiestasgranada.main.local;

import android.widget.RatingBar;

import java.util.HashMap;
import java.util.Map;

public class LocalFireBase extends Local {

    private RatingBar rating;

    public LocalFireBase() {
        super();
    }

    public LocalFireBase(int uid, String titulo, String descripcion, String ultimaFecha, String URLImagen, String URLIcono, double latitud, double longitud, String abierto, String direccion, String horario, double rating) {
        super(uid, titulo, descripcion, ultimaFecha, URLImagen, URLIcono, latitud, longitud, abierto, direccion, horario);
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("rating", rating);
        return result;
    }

    @Override
    public RatingBar getRating() {
        return rating;
    }

    public void setRating(RatingBar rating) {
        this.rating = rating;
    }
}
