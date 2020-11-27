package es.fiestasgranada.main.local;

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

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.fiestasgranada.main.R;
import es.fiestasgranada.main.fragments.MapaFragment;
import es.fiestasgranada.main.listeners.LocalListener;


public class LocalManagement extends RecyclerView.Adapter<LocalManagement.ViewHolder> {

    public static List<Local> mValues;
    private Context context;


    public LocalManagement(List<Local> listado, LocalListener listener) {
        mValues = listado;
    }

    @NotNull
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
        holder.rutaBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // new MapaFragment.TaskDirectionRequest().execute(buildRequestUrl(mOrigin, mDestination));
                MapaFragment.routaNecesitada = true;
                MapaFragment.idDest = mValues.get(position).getId();

                AppCompatActivity activity = (AppCompatActivity) context;

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.cuentaFragment, new MapaFragment()).commit();

                // Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show();
            }
        });


        //Carga las imágenes mediante URL
        Picasso.get().load(mValues.get(position).getURLImagen()).into(holder.mImagenEvento);

        //Comprueba si el local está abierto llamando a la boolean, si es así ocultará el
        // botón de cerrado y mostrará el de abierto y viceversa.
        if (mValues.get(position).isAbierto().equals("si")) {
            holder.noDisponibilidadEvento.setVisibility(View.GONE);
            holder.disponibilidadEvento.setVisibility(View.VISIBLE);
        } else {
            holder.noDisponibilidadEvento.setVisibility(View.VISIBLE);
            holder.disponibilidadEvento.setVisibility(View.GONE);
        }

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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView mTituloView;
        public final TextView mDescripccionView;
        public final TextView disponibilidadEvento;
        public final TextView noDisponibilidadEvento;
        public final TextView Horario;
        public final TextView Direccion;
        public final ImageView mImagenEvento;
        public Local mItem;

        ConstraintLayout expandableView;
        TextView rutaBoton;
        CardView cardView;


        public ViewHolder(final View view) {
            super(view);
            mView = view;

            mTituloView = view.findViewById(R.id.tituloLocal);

            mDescripccionView = view.findViewById(R.id.descOculta);

            Horario = view.findViewById(R.id.horariotextview);

            Direccion = view.findViewById(R.id.direccionTextCard);

            rutaBoton = view.findViewById(R.id.rutaBoton);

            mImagenEvento = view.findViewById(R.id.imagenLocal);

            disponibilidadEvento = view.findViewById(R.id.disponibilidadLocal);

            noDisponibilidadEvento = view.findViewById(R.id.noDisponibilidadLocal);

            expandableView = view.findViewById(R.id.descripcionLocal);

            cardView = view.findViewById(R.id.cardView3);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTituloView.getText() + "'";
        }
    }
}
