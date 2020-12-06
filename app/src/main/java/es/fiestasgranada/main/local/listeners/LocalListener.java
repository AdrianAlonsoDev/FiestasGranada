package es.fiestasgranada.main.local.listeners;

import android.content.Context;

import es.fiestasgranada.main.local.Local;

public interface LocalListener {

    Context context = null;

    void onListFragmentInteraction(Local evento);

    void onClickLocal(Local evento);


}
