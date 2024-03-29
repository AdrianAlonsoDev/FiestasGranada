package es.fiestasgranada.main.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import es.fiestasgranada.main.R;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ImageConverter {

    public void download(Context context, String url, ImageView imageview) {
        Glide.with(context)
                .load(url)
                .transition(withCrossFade())//Config.URL_IMAGES_PUBLIC + url__img+".jpg")
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageview);
    }


}

