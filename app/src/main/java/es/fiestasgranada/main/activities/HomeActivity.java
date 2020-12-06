package es.fiestasgranada.main.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.Slide;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import es.fiestasgranada.main.R;
import es.fiestasgranada.main.databinding.ActivityHomeBinding;
import es.fiestasgranada.main.fragments.CuentaFragment;
import es.fiestasgranada.main.fragments.LocalFragment;
import es.fiestasgranada.main.fragments.MapaFragment;

public class HomeActivity extends AppCompatActivity {

    FragmentTransaction transaccion;
    Fragment fragmentHome, fragmentCuenta, fragmentMapa;
    String TAG = "DEBUG";
    String TAGLOC = " HomeActivity";

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);

        ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);

        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.LEFT);

        fragmentHome = new LocalFragment();
        fragmentCuenta = new CuentaFragment();
        fragmentMapa = new MapaFragment();

        LocalFragment lf = new LocalFragment();
        LocalFragment.listado.clear();
        LocalFragment.DownloadTaskAsync();

        Log.d(TAG + TAGLOC, " -> onCreate -> lf.DownloadTaskAsync(): l:43");

        /**Inicia el fragment
         *
         * Para poner tipo grid, usar LocalFragment().newInstance(2);.commit();
         *
         * */
        getSupportFragmentManager().beginTransaction().add(R.id.cuentaFragment, new LocalFragment()).commit();

        Log.d(TAG + TAGLOC, " -> onCreate -> Iniciando fragment l:47");

        //Inicializa y asigna la variable
        BottomNavigationView bottomNavigationView = binding.bottomNav;

        //Realiza el -> ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {

            transaccion = getSupportFragmentManager().beginTransaction();
            int itemId = menuItem.getItemId();

            boolean returnBool = false;

            if (itemId == R.id.mapa) {

                //Llamar a fragment
                overridePendingTransition(0, 0);
                transaccion.replace(R.id.cuentaFragment, new MapaFragment()).commit();
                returnBool = true;

            } else if (itemId == R.id.cuenta) {

                overridePendingTransition(0, 0);
                transaccion.replace(R.id.cuentaFragment, new CuentaFragment()).commit();

                returnBool = true;

            } else if (itemId == R.id.home) {

                overridePendingTransition(0, 0);
                transaccion.replace(R.id.cuentaFragment, new LocalFragment()).commit();

                returnBool = true;
            }
            return returnBool;
        });

    }

}