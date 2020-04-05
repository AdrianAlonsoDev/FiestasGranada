package es.fiestasgranada.main.local;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.fiestasgranada.main.R;
import es.fiestasgranada.main.listeners.LocalListener;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link LocalListener}
 * interface.
 */
public class LocalFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    private LocalListener mListener;
    static private List<Local> listado = new ArrayList<>();
    private static Context context = null;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LocalFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static LocalFragment newInstance(int columnCount) {
        LocalFragment fragment = new LocalFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
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

        // Set the adapter
       // Context context = view.getContext();
        context=getActivity();
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        /*listado.add(new Local(0,
                "La vida es bella",
                "Descripcion larga del local, descripcion larga del local Descripcion larga del local 1",
                "C/einstein nº30",
                "15/05/2020",
                "https://festgra.com/wp-content/uploads/2019/10/la-vida-es-vella-galeria.jpg",
                true));

        //----------------------------------------------------------------------------------------------------------------------------

        listado.add(new Local(1,
                "Mae West",
                "Descripcion larga del local, descripcion larga del local Descripcion larga del local, 2",
                "C/einstein nº30",
                "15/05/2020",
                "https://www.conciertosengranada.es/doc/l/l_MaeWest.jpg",
                false));

        //----------------------------------------------------------------------------------------------------------------------------

        listado.add(new Local(2,
                "Playmobil",
                "Descripcion larga del local, descripcion larga del local Descripcion larga del local, 3",
                "C/einstein nº30",
                "15/05/2020",
                "https://granadaon.com/wp-content/uploads/2017/05/playmobilclub-granada-3.jpg",
                true));

        //----------------------------------------------------------------------------------------------------------------------------

        listado.add(new Local(3,
                "Wall Street",
                "Descripcion larga del local, descripcion larga del local Descripcion larga del local, 4",
                "C/einstein nº30",
                "15/05/2020",
                "https://s3-media0.fl.yelpcdn.com/bphoto/Ye1zdQAZI_zELyQZw5ehfw/o.jpg",
                false));*/
        //Elimina la restriccion de que solo se pueda usar en un AsyncTask, para pruebas.
        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);
/*
                    try {
                        URL url = new URL("http://192.168.1.10/FiestasGranada/api.php");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        StringBuilder sb = new StringBuilder();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        String json;
                        while ((json = bufferedReader.readLine()) != null) {
                            sb.append(json + "\n");
                            JSONArray jsonArray = new JSONArray(json);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                listado.add(new Local(obj.getInt("id"),
                                        obj.getString("titulo"),
                                        obj.getString("descripcion"),
                                        obj.getString("ubicacion"),
                                        obj.getString("fecha"),
                                        obj.getString("URLImagen"),
                                        obj.getString("abierto")));
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                        return null;
                    }*/
        listado.clear();
        new DownloadJSON().execute();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        recyclerView.setAdapter(new LocalManagement(listado, mListener));
        return view;


    }


        static class DownloadJSON extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {

                URL url = new URL("http://192.168.1.10/FiestasGranada/api.php");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                    JSONArray jsonArray = new JSONArray(json);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);

                            listado.add(new Local(obj.getInt("id"),
                                    obj.getString("titulo"),
                                    obj.getString("descripcion"),
                                    obj.getString("ubicacion"),
                                    obj.getString("fecha"),
                                    obj.getString("URLImagen"),
                                    obj.getString("abierto")));

                    }
                }
            } catch (Exception e) {

                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }return null;
        }
    }



    @Override
    public void onAttach(Context context) {//en el contexto recibe el activity home
        super.onAttach(context);
        if (context instanceof LocalListener) {
            mListener = (LocalListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}

//----------------------------------------------------------------------------------------------------------------------------
//si quisieramos hacerlo en GRID podriamos usar este código, para que no moleste lo he apartado para verlo mas sencillo
        /*if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new LocalManagement(DummyContent.ITEMS, mListener));
        }*/
//----------------------------------------------------------------------------------------------------------------------------