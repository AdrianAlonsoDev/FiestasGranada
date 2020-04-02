package es.fiestasgranada.main;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;



public class MyEventoRecyclerViewAdapter extends RecyclerView.Adapter<MyEventoRecyclerViewAdapter.ViewHolder> {

    private final List<Evento> mValues;
    private final fragmentEventosListener mListener;

    public MyEventoRecyclerViewAdapter(List<Evento> listado, fragmentEventosListener listener) {
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
        holder.mIdView.setText(String.valueOf(mValues.get(position).getId()));
        holder.mTituloView.setText(mValues.get(position).getTitulo());
        holder.mNombreLocalView.setText(mValues.get(position).getUbicacion().getNombre());

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
        public final TextView mIdView;
        public final TextView mTituloView;
        public final TextView mNombreLocalView;
        public Evento mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.evento_id);
            mTituloView = (TextView) view.findViewById(R.id.titulo);
            mNombreLocalView = (TextView) view.findViewById(R.id.nombre_local);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTituloView.getText() + "'";
        }
    }
}
