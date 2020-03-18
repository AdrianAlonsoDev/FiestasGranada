package es.fiestasgranada.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    //defino listadoEventos y Listado a nivel de clase para poder acceder a ellos desde el metodo de abajo, para los click vaya

    private ListView listadoEventos;
    private List<String> listado;

    //-------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


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

        //Lista de eventos en el home
        listadoEventos = (ListView) findViewById(R.id.ListadoEventos);

        //He hecho un array de string, pero haremos un objeto que sea evento,
        //con hijos que sean Fiesta, Promocion, ETC y el array sera de eventos
        listado = new ArrayList<>();
        listado.add("FIESTA PRIMAVERA EN BABI");
        listado.add("JUERNES EN MAE");
        listado.add("ADRI ES GEI");

        //un adaptador para unir la lista de eventos y el array
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                listado
        );

        listadoEventos.setAdapter(adaptador);

        //evento click con cada elemento de la lista

        listadoEventos.setOnItemClickListener(this);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Esto es lo que se ejecuta cuando el elemento de la lista es clickeado
        Toast.makeText(this, "Adri es gei", Toast.LENGTH_SHORT).show();

        //mas tarde haremos distinciones entre los objetos que hemos clickeado
    }
}
