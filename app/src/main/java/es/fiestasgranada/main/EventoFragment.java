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

        listado.add(new Evento(0,"primer elemento", "", laVidaEsBella, new Date(2020,5,5)));
        listado.add(new Evento(1,"segundo elemento", "", laVidaEsBella, new Date(2020,5,5)));
        listado.add(new Evento(2,"tercer evento", "", laVidaEsBella, new Date(2020,5,5)));
        listado.add(new Evento(3,"cuarto elemento", "", laVidaEsBella, new Date(2020,5,5)));


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
