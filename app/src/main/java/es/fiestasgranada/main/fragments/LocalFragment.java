package es.fiestasgranada.main.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.fiestasgranada.main.R;
import es.fiestasgranada.main.local.Local;
import es.fiestasgranada.main.local.listeners.LocalListener;
import es.fiestasgranada.main.management.LocalManagement;

/**
 * Un fragmento representando un contenedor con una lista.
 * <p/>
 * Actividades que contienen este fragmento deben implementar la interfaz {@link LocalListener}
 * .
 */
public class LocalFragment extends Fragment {

    public static final List<Local> listado = new ArrayList<>();
    private final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;


    /**
     * Constructor vacio obligatorio para instanciar el fragmento
     * (e.j. cambio de rotacion de pantalla)
     */
    public LocalFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_local_list, container, false);

        // binding = FragmentLocalBinding.inflate(inflater, container, false);
        //view = binding.getRoot();

        // Set the adapter
        Context context = getContext();

        //  View drawer = inflater.inflate(R.layout.fragment_local_list, container, false);

        //RecyclerView recyclerView = (RecyclerView) view;
        RecyclerView recyclerView = view.findViewById(R.id.listCardList);

        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }


        recyclerView.setAdapter(new LocalManagement(listado));
        return view;

    }

    public static void DownloadTaskAsync() {
        String URL_API = "https://michiochi.synology.me/api.php";
        ExecutorService executors = Executors.newFixedThreadPool(1);
        Runnable runnable = () -> {
            try {
                URL url = new URL(URL_API);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json).append("\n");
                    JSONArray jsonArray = new JSONArray(json);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        listado.add(new Local(obj.getInt("uid"),
                                obj.getString("titulo"),
                                obj.getString("descripcion"),
                                obj.getString("ultimaFecha"),
                                obj.getString("URLImagen"),
                                obj.getString("URLIcono"),
                                obj.getDouble("latitud"),
                                obj.getDouble("longitud"),
                                obj.getString("abierto"),
                                obj.getString("direccion"),
                                obj.getString("horario")) {
                            @Override
                            public RatingBar getRating() {
                                return null;
                            }
                        });

                        Log.d("DEBUG LocalFragment", " -> DownloadTaskAsync -> Locals added successfully.");

                    }
                }
            } catch (Exception e) {
                Log.d("DEBUG LocalFragment", " -> ERR DownloadTaskAsync %s -> " + e.getStackTrace());
            }
        };
        executors.submit(runnable);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAttach(Context context) {//en el contexto recibe el activity home
        super.onAttach(context);

    }

}