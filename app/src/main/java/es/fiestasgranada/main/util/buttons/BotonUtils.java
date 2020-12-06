package es.fiestasgranada.main.util.buttons;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import java.util.ArrayList;
import java.util.List;

import es.fiestasgranada.main.R;

public class BotonUtils {
    public static double mapValueFromRangeToRange(double value, double fromLow, double fromHigh, double toLow, double toHigh) {
        return toLow + ((value - fromLow) / (fromHigh - fromLow) * (toHigh - toLow));
    }

    public static double clamp(double value, double low, double high) {
        return Math.min(Math.max(value, low), high);
    }

    public static List<es.fiestasgranada.main.util.buttons.Icon> getIcons() {
        List<es.fiestasgranada.main.util.buttons.Icon> icons = new ArrayList<>();
        icons.add(new es.fiestasgranada.main.util.buttons.Icon(R.drawable.heart_on, R.drawable.heart_off, Icon.IconType.Heart));
        return icons;
    }

    public static Drawable resizeDrawable(Context context, Drawable drawable, int width, int height) {
        Bitmap bitmap = getBitmap(drawable, width, height);
        return new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, width, height, true));
    }

    public static Bitmap getBitmap(Drawable drawable, int width, int height) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawableCompat) {
            return getBitmap((VectorDrawableCompat) drawable, width, height);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (drawable instanceof VectorDrawable) {
                return getBitmap((VectorDrawable) drawable, width, height);
            } else {
                throw new IllegalArgumentException("Unsupported drawable type");
            }
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Bitmap getBitmap(VectorDrawable vectorDrawable, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    private static Bitmap getBitmap(VectorDrawableCompat vectorDrawable, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }
}

