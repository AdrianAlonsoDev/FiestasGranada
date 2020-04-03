package es.fiestasgranada.main;

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


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link fragmentEventosListener}
 * interface.
 */
public class EventoFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    private fragmentEventosListener mListener;
    private List<Evento> listado = new ArrayList<>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventoFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static EventoFragment newInstance(int columnCount) {
        EventoFragment fragment = new EventoFragment();
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
        View view = inflater.inflate(R.layout.fragment_evento_list, container, false);

        // Set the adapter
        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

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
            recyclerView.setAdapter(new MyEventoRecyclerViewAdapter(DummyContent.ITEMS, mListener));
        }*/
        //----------------------------------------------------------------------------------------------------------------------------



        //LA LISTA - creo un local de ejemplo para poder crear los eventos
        Local laVidaEsBella = new Local(1,"La vida es bella", "Pub de tranquis...", true, "C/einstein nº30");

        listado.add(new Evento(0,"La vida es bella", "Descripcion larga del local, descripcion larga del local Descripcion larga del local, 1descripcion larga del local Descripcion larga del local, 1descripcion larga del local Descripcion larga del local, 1", laVidaEsBella, new Date(2020,5,5), "data:image/png;base64,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"));
        listado.add(new Evento(1,"Mae West", "Descripcion larga del local, descripcion larga del local Descripcion larga del local, 2", laVidaEsBella, new Date(2020,5,5), "https://www.conciertosengranada.es/doc/l/l_MaeWest.jpg"));
        listado.add(new Evento(2,"Playmobil", "Descripcion larga del local, descripcion larga del local Descripcion larga del local, 3", laVidaEsBella, new Date(2020,5,5), "https://granadaon.com/wp-content/uploads/2017/05/playmobilclub-granada-3.jpg"));
        listado.add(new Evento(3,"Wall Street", "Descripcion larga del local, descripcion larga del local Descripcion larga del local, 4", laVidaEsBella, new Date(2020,5,5), "https://s3-media0.fl.yelpcdn.com/bphoto/Ye1zdQAZI_zELyQZw5ehfw/o.jpg"));


        recyclerView.setAdapter(new MyEventoRecyclerViewAdapter(listado, mListener));
        return view;


    }




    @Override
    public void onAttach(Context context) {//en el contexto recibe el activity home
        super.onAttach(context);
        if (context instanceof fragmentEventosListener) {
            mListener = (fragmentEventosListener) context;
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
