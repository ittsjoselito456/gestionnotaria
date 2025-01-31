package jortega.proyecto.gestionnotaria.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jortega.proyecto.gestionnotaria.R;
import jortega.proyecto.gestionnotaria.modelos.Cita;

public class CitasAdapter extends RecyclerView.Adapter<CitasAdapter.ViewHolder> {

    private List<Cita> citasList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Cita cita);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public CitasAdapter(List<Cita> citasList) {
        this.citasList = citasList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cita, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cita cita = citasList.get(position);
        holder.citaId.setText("Cita ID: " + cita.getId());
        holder.citaFecha.setText("Fecha: " + cita.getFecha_cita());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(cita);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return citasList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView citaId;
        public TextView citaFecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            citaId = itemView.findViewById(R.id.citaId);
            citaFecha = itemView.findViewById(R.id.citaFecha);
        }
    }
}
