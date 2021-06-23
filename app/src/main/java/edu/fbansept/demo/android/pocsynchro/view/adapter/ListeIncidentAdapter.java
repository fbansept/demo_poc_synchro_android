package edu.fbansept.demo.android.pocsynchro.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.fbansept.demo.android.pocsynchro.R;
import edu.fbansept.demo.android.pocsynchro.model.Incident;

public class ListeIncidentAdapter extends RecyclerView.Adapter
        <RecyclerView.ViewHolder> {

    private final EcouteurClicIncident ecouteur;

    public void changeData(List<Incident> nouvelleListeIncident) {
        listeIncident.clear();
        listeIncident.addAll(nouvelleListeIncident);
        notifyDataSetChanged();
    }

    public interface EcouteurClicIncident {
        void onClicIncident(Incident item);
    }

    private List<Incident> listeIncident;

    public ListeIncidentAdapter(List<Incident> listeIncident, EcouteurClicIncident ecouteur) {
        this.ecouteur = ecouteur;
        this.listeIncident = listeIncident;
    }

    static class IncidentViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layoutItem;
        TextView textViewTexte;
        CardView cardView;

        public IncidentViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutItem = itemView.findViewById(R.id.layout_item_incident);
            textViewTexte = itemView.findViewById(R.id.textView_titre);
            cardView = itemView.findViewById(R.id.cardView_incident);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

            view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_incident,parent,false);
            return new IncidentViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Incident incident = listeIncident.get(position);
        IncidentViewHolder incidentViewHolder = (IncidentViewHolder)holder;
        incidentViewHolder.textViewTexte.setText(incident.getTexte());

        incidentViewHolder.layoutItem.setOnClickListener( v ->
                ecouteur.onClicIncident(incident)
        );

    }

    @Override
    public int getItemCount() {
        return listeIncident.size();
    }
}
