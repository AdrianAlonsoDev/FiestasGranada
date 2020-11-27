package es.fiestasgranada.main.activities;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import es.fiestasgranada.main.databinding.ActivityMainBinding;
import es.fiestasgranada.main.fragments.LocalFragment;
import es.fiestasgranada.main.util.ProgressBarAnimation;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;


    @Override
    protected void onStart() {
        super.onStart();
        LocalFragment.listado.clear();
        new LocalFragment.DownloadJSON().execute();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        progressBar = binding.cargabarra;

        progressBar.setMax(100);
        progressBar.setScaleY(3f);

        progressAnimation();


    }


    public void progressAnimation() {
        ProgressBarAnimation anim = new ProgressBarAnimation(this, progressBar, 0, 100);
        //default 2000, debug 7500
        anim.setDuration(1000);
        progressBar.setAnimation(anim);
    }

}
//Test branches