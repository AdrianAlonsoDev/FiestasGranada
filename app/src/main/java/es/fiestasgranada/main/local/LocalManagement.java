package es.fiestasgranada.main.local;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import es.fiestasgranada.main.R;
import es.fiestasgranada.main.listeners.LocalListener;


public class LocalManagement extends RecyclerView.Adapter<LocalManagement.ViewHolder> {

    private final List<Local> mValues;
    private final LocalListener mListener;


    public LocalManagement(List<Local> listado, LocalListener listener) {
        mValues = listado;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_local, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


        holder.mItem = mValues.get(position);
        holder.mTituloView.setText(mValues.get(position).getTitulo());
        holder.mDescripccionView.setText(mValues.get(position).getDescripcion());
        Picasso.get().load(mValues.get(position).getURLImagen()).into(holder.mImagenEvento);
        if(mValues.get(position).isAbierto() == true) {

        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onClickLocal(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTituloView;
        public final TextView mDescripccionView;
        public final TextView disponibilidadEvento;
        public final TextView noDisponibilidadEvento;
        public final ImageView mImagenEvento;
        public final CardView listenerEventoView;
        public Local mItem;

        ConstraintLayout expandableView;
        Button botonOfertas;
        CardView cardView;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTituloView = (TextView) view.findViewById(R.id.tituloLocal);
            mDescripccionView = (TextView) view.findViewById(R.id.descOculta);
            mImagenEvento = (ImageView) view.findViewById(R.id.imagenLocal);
            disponibilidadEvento = (TextView) view.findViewById(R.id.disponibilidadLocal);
            noDisponibilidadEvento = (TextView) view.findViewById(R.id.noDisponibilidadLocal);


            expandableView = view.findViewById(R.id.descripcionLocal);
            botonOfertas = view.findViewById(R.id.botonOfertas);
            cardView = view.findViewById(R.id.cardView);
            listenerEventoView = (CardView) view.findViewById(R.id.cardView);



            botonOfertas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expandableView.getVisibility()==View.GONE){
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        expandableView.setVisibility(View.VISIBLE);
                        botonOfertas.setBackgroundResource(R.drawable.oferta);
                    } else {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        expandableView.setVisibility(View.GONE);
                        botonOfertas.setBackgroundResource(R.drawable.oferta);
                    }
                }
            });

            listenerEventoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expandableView.getVisibility()==View.GONE){
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        expandableView.setVisibility(View.VISIBLE);
                        botonOfertas.setBackgroundResource(R.drawable.oferta);
                    } else {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        expandableView.setVisibility(View.GONE);
                        botonOfertas.setBackgroundResource(R.drawable.oferta);
                    }
                }
            });


        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTituloView.getText() + "'";
        }
    }
}
