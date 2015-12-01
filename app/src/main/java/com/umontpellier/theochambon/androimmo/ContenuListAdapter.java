package com.umontpellier.theochambon.androimmo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by theo on 10/11/2015.
 */
public class ContenuListAdapter extends RecyclerView.Adapter<ContenuListAdapter.ContenuListViewHolder>{

    private LayoutInflater inflater;
    private List<ContenuListe> contenu;

    public ContenuListAdapter(LayoutInflater inflater, List<ContenuListe> liste){
        this.inflater = inflater;
        this.contenu = liste;
    }

    @Override
    public ContenuListAdapter.ContenuListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemList = inflater.inflate(R.layout.listetextview, parent, false);
        ContenuListViewHolder contenuViewHolder = new ContenuListViewHolder(itemList);
        contenuViewHolder.view = itemList;
        contenuViewHolder.nom = (TextView) itemList.findViewById(R.id.tvNom);
        contenuViewHolder.ville = (TextView) itemList.findViewById(R.id.tvVille);
        contenuViewHolder.id = (TextView) itemList.findViewById(R.id.tvID);
        contenuViewHolder.piece = (TextView) itemList.findViewById(R.id.tvPieces);
        contenuViewHolder.surface = (TextView) itemList.findViewById(R.id.tvSurface);

        return contenuViewHolder;
    }

    @Override
    public void onBindViewHolder(ContenuListAdapter.ContenuListViewHolder holder, int position) {
        ContenuListe contenuliste = contenu.get(position);
        holder.nom.setText(contenuliste.getNom());
        holder.ville.setText(contenuliste.getVille());
        holder.id.setText(contenuliste.getId());

        if(!contenuliste.getPieces().equals("0")){
        holder.piece.setText(", " + contenuliste.getPieces() + " pièces");
        }

        if(!contenuliste.getSurface().equals("0")){
        holder.surface.setText(", " + contenuliste.getSurface() + "m²");
        }
        holder.position = position;


    }

    @Override
    public int getItemCount() {
        return contenu.size();
    }



    public static class ContenuListViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView nom, ville, id, surface, piece;
        int position;

        public ContenuListViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
        }

    }

    public ContenuListe removeItem(int position) {
        final ContenuListe model = contenu.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, ContenuListe model) {
        contenu.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final ContenuListe model = contenu.remove(fromPosition);
        contenu.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<ContenuListe> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<ContenuListe> newModels) {
        for (int i = contenu.size() - 1; i >= 0; i--) {
            final ContenuListe model = contenu.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<ContenuListe> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final ContenuListe model = newModels.get(i);
            if (!contenu.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<ContenuListe> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final ContenuListe model = newModels.get(toPosition);
            final int fromPosition = contenu.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

}
