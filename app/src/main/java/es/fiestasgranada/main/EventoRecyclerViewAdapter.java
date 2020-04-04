package es.fiestasgranada.main;

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

import es.fiestasgranada.listeners.EventosListener;


public class EventoRecyclerViewAdapter extends RecyclerView.Adapter<EventoRecyclerViewAdapter.ViewHolder> {

    private final List<Evento> mValues;
    private final EventosListener mListener;


    public EventoRecyclerViewAdapter(List<Evento> listado, EventosListener listener) {
        mValues = listado;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_evento, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


        holder.mItem = mValues.get(position);
        holder.mTituloView.setText(mValues.get(position).getTitulo());
        holder.mDescripccionView.setText(mValues.get(position).getDescripcion());
        Picasso.get().load(mValues.get(position).getURLImagen()).into(holder.mImagenEvento);


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onClickEvento(holder.mItem);
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
        public final ImageView mImagenEvento;
        public Evento mItem;

        ConstraintLayout expandableView;
        Button arrowBtn;
        CardView cardView;


        public ViewHolder(View view) {
            super(view);
             mView = view;

            mTituloView = (TextView) view.findViewById(R.id.titulo);
            mDescripccionView = (TextView) view.findViewById(R.id.textView);
            mImagenEvento = (ImageView) view.findViewById(R.id.imagenEvento);

            expandableView = view.findViewById(R.id.expandableView);
            arrowBtn = view.findViewById(R.id.arrowBtn);
            cardView = view.findViewById(R.id.cardView);

            arrowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expandableView.getVisibility()==View.GONE){
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        expandableView.setVisibility(View.VISIBLE);
                        arrowBtn.setBackgroundResource(R.drawable.ic_arrow_drop_down_black_24dp);
                    } else {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        expandableView.setVisibility(View.GONE);
                        arrowBtn.setBackgroundResource(R.drawable.ic_arrow_drop_down_black_24dp);
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
