package es.fiestasgranada.main.util;

import android.content.Context;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;

import es.fiestasgranada.main.activities.HomeActivity;

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
            context.startActivity(new Intent(context, HomeActivity.class));
        }
    }

}
