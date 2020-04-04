package es.fiestasgranada.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import es.fiestasgranada.listeners.EventosListener;


public class HomeActivity extends AppCompatActivity implements EventosListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //inicia el fragment

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.contenedor, new EventoFragment()) //Para poner tipo grid, usar EventoFragment().newInstance(2);
                .commit();


        //Initialize and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.mapa:
                        startActivity(new Intent(getApplicationContext(), MapaActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.cuenta:
                        startActivity(new Intent(getApplicationContext(), CuentaActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.home:
                        return true;
                }
                return false;
            }
        });

    }


    //metodos de la interfaz para que todo funcione
    @Override
    public void onListFragmentInteraction(Evento evento) {

    }

    @Override
    public void onClickEvento(Evento evento) {

    }
}
