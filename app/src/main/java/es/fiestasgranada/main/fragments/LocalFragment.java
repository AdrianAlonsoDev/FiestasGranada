package es.fiestasgranada.main.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

import es.fiestasgranada.main.R;
import es.fiestasgranada.main.activities.HomeActivity;
import es.fiestasgranada.main.databinding.FragmentCuentaBinding;
import es.fiestasgranada.main.databinding.FragmentLocalBinding;
import es.fiestasgranada.main.listeners.LocalListener;
import es.fiestasgranada.main.local.Local;
import es.fiestasgranada.main.local.LocalManagement;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link LocalListener}
 * interface.
 */
public class LocalFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    public static final List<Local> listado = new ArrayList<>();
    private static final String URL_API = "https://michiochi.synology.me/api.php";
    private Context context = null;
    private int mColumnCount = 1;
    private LocalListener mListener;
    FragmentLocalBinding binding;

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
        context = getContext();

        //  View drawer = inflater.inflate(R.layout.fragment_local_list, container, false);

        //RecyclerView recyclerView = (RecyclerView) view;
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.listCardList);

        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }


        /*try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        recyclerView.setAdapter(new LocalManagement(listado, mListener));
        return view;

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

    public static class DownloadJSON extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {

                URL url = new URL(URL_API);
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
                                obj.getString("ultimaFecha"),
                                obj.getString("URLImagen"),
                                obj.getString("URLIcono"),
                                obj.getDouble("latitud"),
                                obj.getDouble("longitud"),
                                obj.getString("abierto"),
                                obj.getString("direccion"),
                                obj.getString("horario")));
                    }
                }
            } catch (Exception e) {

                Log.d("DEBUG", "doInBackground: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }
}