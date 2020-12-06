package es.fiestasgranada.main.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import es.fiestasgranada.main.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    Context mContext;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mContext = getApplicationContext();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        progressBar = binding.cargabarra;

        progressBar.setMax(100);
        progressBar.setScaleY(3f);

        ProgressBarAnimation anim = new ProgressBarAnimation(this, progressBar, 0, 100);
        //default 5000, debug 2000,
        anim.setDuration(1000);
        progressBar.setAnimation(anim);
    }

    public class ProgressBarAnimation extends Animation {

        private final Context context;
        private final ProgressBar progressBar;
        private final float from;
        private final float to;

        public ProgressBarAnimation(Context context, ProgressBar progressBar, float from, float to) {
            this.context = context;
            this.progressBar = progressBar;
            this.from = from;
            this.to = to;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            float value = from + (to - from) * interpolatedTime;
            progressBar.setProgress((int) value);

            if (value == to) {
                MainActivity.this.startActivity(new Intent(context, HomeActivity.class));
            }
        }
    }
}