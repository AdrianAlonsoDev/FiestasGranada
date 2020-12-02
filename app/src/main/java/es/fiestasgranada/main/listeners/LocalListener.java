package es.fiestasgranada.main.listeners;

import android.annotation.SuppressLint;
import android.content.Context;

import es.fiestasgranada.main.local.Local;

public interface LocalListener {

    @SuppressLint("StaticFieldLeak")
    Context context = null;

    void onListFragmentInteraction(Local evento);

    void onClickLocal(Local evento);


}
