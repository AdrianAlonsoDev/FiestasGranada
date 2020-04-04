package es.fiestasgranada.main.local;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private List<Local> listado = new ArrayList<>();

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
        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        listado.add(new Local(0,
                        "La vida es bella",
                    "Descripcion larga del local, descripcion larga del local Descripcion larga del local 1",
                     "C/einstein nº30",
                               new Date(2020,5,5),
                   "https://festgra.com/wp-content/uploads/2019/10/la-vida-es-vella-galeria.jpg",
                       true));

        //----------------------------------------------------------------------------------------------------------------------------

        listado.add(new Local(1,
                        "Mae West",
                   "Descripcion larga del local, descripcion larga del local Descripcion larga del local, 2",
                    "C/einstein nº30",
                              new Date(2020,5,5),
                  "https://www.conciertosengranada.es/doc/l/l_MaeWest.jpg",
                      false));

        //----------------------------------------------------------------------------------------------------------------------------

        listado.add(new Local(2,
                        "Playmobil",
                   "Descripcion larga del local, descripcion larga del local Descripcion larga del local, 3",
                    "C/einstein nº30",
                              new Date(2020,5,5),
                   "https://granadaon.com/wp-content/uploads/2017/05/playmobilclub-granada-3.jpg",
                       false));

        //----------------------------------------------------------------------------------------------------------------------------

        listado.add(new Local(3,
                        "Wall Street",
                   "Descripcion larga del local, descripcion larga del local Descripcion larga del local, 4",
                    "C/einstein nº30",
                              new Date(2020,5,5),
                   "https://s3-media0.fl.yelpcdn.com/bphoto/Ye1zdQAZI_zELyQZw5ehfw/o.jpg",
                       false));


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