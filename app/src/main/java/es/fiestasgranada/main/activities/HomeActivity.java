package es.fiestasgranada.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import es.fiestasgranada.main.R;
import es.fiestasgranada.main.fragments.CuentaFragment;
import es.fiestasgranada.main.fragments.LocalFragment;
import es.fiestasgranada.main.listeners.LocalListener;
import es.fiestasgranada.main.local.Local;


public class HomeActivity extends AppCompatActivity implements LocalListener {

    FragmentTransaction transaccion;
    Fragment fragmentHome, fragmentCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fragmentHome = new LocalFragment();
        fragmentCuenta = new CuentaFragment();

        //Inicia el fragment
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.cuentaFragment, new LocalFragment()) //Para poner tipo grid, usar LocalFragment().newInstance(2);
                .commit();


        //Initialize and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                transaccion = getSupportFragmentManager().beginTransaction();
                int itemId = menuItem.getItemId();

                boolean returnBool = false;
                if (itemId == R.id.mapa) {
                    startActivity(new Intent(getApplicationContext(), MapaActivity.class));
                    overridePendingTransition(0, 0);
                    returnBool = true;
                } else if (itemId == R.id.cuenta) {
                    //startActivity(new Intent(getApplicationContext(), CuentaActivity.class));
                    //Llamar a fragment
                    transaccion.replace(R.id.cuentaFragment, new CuentaFragment()).commit();

                    // bottomNavigationView.setSelectedItemId(R.id.cuenta);
                    overridePendingTransition(0, 0);
                    returnBool = true;
                } else if (itemId == R.id.home) {
                    transaccion.replace(R.id.cuentaFragment, new LocalFragment()).commit();
                    // bottomNavigationView.setSelectedItemId(R.id.home);
                    overridePendingTransition(0, 0);
                    returnBool = true;
                }
                return returnBool;
            }
        });

    }


    //metodos de la interfaz para que todo funcione
    @Override
    public void onListFragmentInteraction(Local evento) {

    }

    @Override
    public void onClickLocal(Local evento) {

    }
}
