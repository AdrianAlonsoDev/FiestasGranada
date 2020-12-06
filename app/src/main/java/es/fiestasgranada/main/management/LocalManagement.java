package es.fiestasgranada.main.management;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import es.fiestasgranada.main.R;
import es.fiestasgranada.main.fragments.MapaFragment;
import es.fiestasgranada.main.local.Local;
import es.fiestasgranada.main.util.buttons.Boton;
import es.fiestasgranada.main.util.buttons.listener.OnAnimationEndListener;
import es.fiestasgranada.main.util.buttons.listener.OnLikeListener;


public class LocalManagement extends RecyclerView.Adapter<LocalManagement.ViewHolder> implements OnLikeListener {

    public static List<Local> mValues;
    private Context context;

    public LocalManagement(List<Local> listado) {
        mValues = listado;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_local, parent, false);
        context = parent.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.mItem = mValues.get(position);

        //Pone el título y la descripción.
        holder.mTituloView.setText(mValues.get(position).getTitulo());
        holder.mDescripccionView.setText(mValues.get(position).getDescripcion());
        holder.Horario.setText(mValues.get(position).getHorario());
        holder.Direccion.setText(mValues.get(position).getDireccion());
        MapaFragment.TaskDirectionRequest.routaNecesitada = false;

        holder.rutaBoton.setOnClickListener(v -> {
            // new MapaFragment.TaskDirectionRequest().execute(buildRequestUrl(mOrigin, mDestination));
            MapaFragment.TaskDirectionRequest.routaNecesitada = true;
            MapaFragment.idDest = mValues.get(position).getId();

            AppCompatActivity activity = (AppCompatActivity) context;

            activity.getSupportFragmentManager().beginTransaction().replace(R.id.cuentaFragment, new MapaFragment()).commit();

            // Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show();
        });


        //Carga las imágenes mediante URL
        Picasso.get().load(mValues.get(position).getURLImagen()).into(holder.mImagenEvento);

        /**Comprueba si el local está abierto llamando a la boolean, si es así ocultará el
         botón de cerrado y mostrará el de abierto y viceversa.*/
        if (mValues.get(position).isAbierto().equals("si")) {
            holder.noDisponibilidadEvento.setVisibility(View.GONE);
            holder.disponibilidadEvento.setVisibility(View.VISIBLE);
        } else {
            holder.noDisponibilidadEvento.setVisibility(View.VISIBLE);
            holder.disponibilidadEvento.setVisibility(View.GONE);
        }

        holder.like.setOnAnimationEndListener(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(Boton likeButton) {

            }
        });

       /* holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onClickLocal(holder.mItem);
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public void liked(Boton likeButton) {
        likeButton.setLiked(true);
    }

    @Override
    public void unLiked(Boton likeButton) {
        likeButton.setLiked(false);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTituloView;
        private final TextView mDescripccionView;
        private final TextView disponibilidadEvento;
        private final TextView noDisponibilidadEvento;
        private final TextView Horario;
        private final TextView Direccion;
        private final ImageView mImagenEvento;
        private final Boton like;
        Local mItem;

        ConstraintLayout expandableView;
        TextView rutaBoton;
        CardView cardView;

        private ViewHolder(final View view) {
            super(view);

            this.mTituloView = view.findViewById(R.id.tituloLocal);

            this.mDescripccionView = view.findViewById(R.id.descOculta);

            this.Horario = view.findViewById(R.id.horariotextview);

            this.Direccion = view.findViewById(R.id.direccionTextCard);

            this.rutaBoton = view.findViewById(R.id.rutaBoton);

            this.mImagenEvento = view.findViewById(R.id.imagenLocal);

            this.disponibilidadEvento = view.findViewById(R.id.disponibilidadLocal);

            this.noDisponibilidadEvento = view.findViewById(R.id.noDisponibilidadLocal);

            this.expandableView = view.findViewById(R.id.descripcionLocal);

            this.cardView = view.findViewById(R.id.cardView3);

            this.like = view.findViewById(R.id.like_but);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTituloView.getText() + "'";
        }
    }
}
