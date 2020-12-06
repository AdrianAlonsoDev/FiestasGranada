package es.fiestasgranada.main.util.buttons.listener;


import es.fiestasgranada.main.util.buttons.Boton;

public interface OnLikeListener {
    void liked(Boton likeButton);

    void unLiked(Boton likeButton);
}
