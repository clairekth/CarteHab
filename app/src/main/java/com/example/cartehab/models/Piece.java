package com.example.cartehab.models;

import com.example.cartehab.outils.FabriqueNumero;

import java.io.Serializable;
/**
 * La classe Piece représente une pièce dans une habitation.
 * @author Claire Kurth
 */
public class Piece implements Serializable {
    /**
     * L'identifiant de la pièce.
     */
    protected String id;
    /**
     * Le nom de la pièce.
     */
    protected String nom;
    /**
     * L'habitation à laquelle appartient la pièce.
     */
    protected Habitation habitation;
    /**
     * Le mur nord de la pièce.
     */
    protected Mur murNord;
    /**
     * Le mur sud de la pièce.
     */
    protected Mur murSud;
    /**
     * Le mur est de la pièce.
     */
    protected Mur murEst;
    /**
     * Le mur ouest de la pièce.
     */
    protected Mur murOuest;

    /**
     * Constructeur.
     * @param idHab L'idenfiant de l'habitation contenant la pièce.
     */
    public Piece(String idHab){
        id = idHab + "PI" + FabriqueNumero.getInstance().getNumeroPiece();
    }

    /**
     * Cette méthode permet de retourner l'identifiant de la pièce.
     * @return l'identifiant.
     */
    public String getId(){
        return id;
    }

    /**
     * Cette méthode permet de set le mur passé en paramètre.
     * @param mur Le mur à set.
     */
    public void setMur(Mur mur){
        if (mur.getOrientation().equals("N")){
            murNord = mur;
        } else if (mur.getOrientation().equals("S")){
            murSud = mur;
        } else if (mur.getOrientation().equals("E")){
            murEst = mur;
        } else {
            murOuest = mur;
        }
    }

    /**
     * Cette méthode permet de set le nom de la pièce.
     * @param nom Le nom.
     */
    public void setNom(String nom){
        this.nom = nom;

    }

    /**
     * Cette méthode permet de récupérer le mur nord de la pièce.
     * @return Le mur nord.
     */
    public Mur getMurNord(){
        return murNord;
    }

    /**
     * Cette méthode permet de récupérer le mur sud de la pièce.
     * @return Le mur sud.
     */
    public Mur getMurSud() {
        return murSud;
    }

    /**
     * Cette méthode permet de récupérer le mur est de la pièce.
     * @return le mur est.
     */
    public Mur getMurEst() {
        return murEst;
    }

    /**
     * Cette méthode permet de récupérer le mur ouest de la pièce.
     * @return le mur ouest.
     */
    public Mur getMurOuest() {
        return murOuest;
    }

    /**
     * Cette méthode permet de récupérer le nom de la pièce.
     * @return le nom.
     */
    public String getNom(){
        return nom;
    }

    /**
     * Cette méthode permet de savoir si la pièce a été construite correctement, c'est à dire que tous les murs sont set et
     * construit correctement et qu'il y a au moins une porte dans la pièce.
     * @return true si la pièce a été construite correctement, false sinon.
     */
    public boolean pieceEstOK() {
        if (murEst == null || !murEst.murEstOK()){
            return false;
        }
        if (murNord == null || !murNord.murEstOK()) {
            return false;
        }
        if (murOuest == null || !murOuest.murEstOK()){
            return false;
        }
        if (murSud == null || !murSud.murEstOK()) {
            return false;
        }

        return murSud.murADesPortes() || murOuest.murADesPortes() || murEst.murADesPortes() || murNord.murADesPortes(); //Au moins un des murs à une porte
    }

    /**
     * Cette méthode permet de récupérer sous la forme d'un string toutes les erreurs présentent dans la pièce.
     * @return Un string de toutes les erreurs de la pièce.
     */
    public String erreurs(){
        boolean portesPresentes = false;
        StringBuilder sb = new StringBuilder();
        if (murOuest == null){
            sb.append("Il manque le mur Ouest.\n");
        } else {
            if (!murOuest.murEstOK()){
                for (Porte p : murOuest.getListePortes()){
                    if (p.porteEstOK() == 1){
                        sb.append("Au moins un des portes du mur Ouest n'a pas de pièces suivantes.\n");
                        break;
                    } else if (p.porteEstOK() == 2){
                        sb.append("Le mur Est de " + p.getPieceSuivante().getNom() + "n'a pas de portes. " + this.nom + " et " +p.getPieceSuivante().getNom() + " ne ont donc pas reliées.\n");
                        break;
                    } else if (p.porteEstOK() == 3){
                        sb.append("Aucune portes du mur Est de " + p.getPieceSuivante().getNom() + " ne relie " + this.nom + "\n");
                        break;
                    } else if (p.porteEstOK() == 4){
                        sb.append("Il n'y a pas de mur Est dans " + p.getPieceSuivante().getNom() +".\n");
                        break;
                    }
                }
            }
            if (murOuest.murADesPortes()){
                portesPresentes = true;
            }
        }
        if (murEst == null){
            sb.append("Il manque le mur Est.\n");
        } else {
            if (!murEst.murEstOK()){
                for (Porte p : murEst.getListePortes()){
                    if (p.porteEstOK() == 1){
                        sb.append("Au moins un des portes du mur Est n'a pas de pièces suivantes.\n");
                        break;
                    } else if (p.porteEstOK() == 2){
                        sb.append("Le mur Ouest de " + p.getPieceSuivante().getNom() + " n'a pas de portes. " + this.nom + " et " +p.getPieceSuivante().getNom() + " ne ont donc pas reliées.\n");
                        break;
                    } else if (p.porteEstOK() == 3){
                        sb.append("Aucune portes du mur Ouest de " + p.getPieceSuivante().getNom() + " ne relie " + this.nom +"\n");
                        break;
                    }else if (p.porteEstOK() == 4){
                        sb.append("Il n'y a pas de mur Ouest dans " + p.getPieceSuivante().getNom() +".\n");
                        break;
                    }
                }
            }
            if (murEst.murADesPortes()){
                portesPresentes = true;
            }
        }
        if (murNord == null){
            sb.append("Il manque le mur Nord.\n");
        }else {
            if (!murNord.murEstOK()){
                for (Porte p : murNord.getListePortes()){
                    if (p.porteEstOK() == 1){
                        sb.append("Au moins un des portes du mur Nord n'a pas de pièces suivantes.\n");
                        break;
                    } else if (p.porteEstOK() == 2){
                        sb.append("Le mur Sud de " + p.getPieceSuivante().getNom() + " n'a pas de portes. " + this.nom + " et " +p.getPieceSuivante().getNom() + " ne ont donc pas reliées.\n");
                        break;
                    } else if (p.porteEstOK() == 3){
                        sb.append("Aucune portes du mur Sud de " + p.getPieceSuivante().getNom() + " ne relie " + this.nom + "\n");
                        break;
                    }else if (p.porteEstOK() == 4){
                        sb.append("Il n'y a pas de mur Sud dans " + p.getPieceSuivante().getNom() +".\n");
                        break;
                    }
                }              }
            if (murNord.murADesPortes()){
                portesPresentes = true;
            }
        }
        if (murSud == null){
            sb.append("Il manque le mur Sud.\n");
        } else {
            if (!murSud.murEstOK()){
                for (Porte p : murSud.getListePortes()){
                    if (p.porteEstOK() == 1){
                        sb.append("Au moins un des portes du mur Sud n'a pas de pièces suivantes.\n");
                        break;
                    } else if (p.porteEstOK() == 2){
                        sb.append("Le mur Nord de " + p.getPieceSuivante().getNom() + " n'a pas de portes. " + this.nom + " et " +p.getPieceSuivante().getNom() + " ne ont donc pas reliées.\n");
                        break;
                    } else if (p.porteEstOK() == 3){
                        sb.append("Aucune portes du mur Nord de " + p.getPieceSuivante().getNom() + " ne relie " + this.nom +"\n");
                        break;
                    }else if (p.porteEstOK() == 4){
                        sb.append("Il n'y a pas de mur Nord dans " + p.getPieceSuivante().getNom() +".\n");
                        break;
                    }
                }
            }
            if (murSud.murADesPortes()){
                portesPresentes = true;
            }
        }

        if (!portesPresentes){
            sb.append("Il n'y a pas de portes dans la pièce.\n");
        }

        return sb.toString();
    }

    /**
     * Cette méthode permet de supprimer cette pièce d'être une pièce suivante lors de sa suppression.
     */
    public void suppressionSiEstUnePieceSuivante(){
        if (murSud != null){
            for (Porte p : murSud.getListePortes()){
                if (p.pieceSuivante != null){
                    for (Porte p2 : p.pieceSuivante.getMurNord().getListePortes()){
                        if (p2.pieceSuivante.getId().equals(id)){
                            p2.setPieceSuivante(null);
                        }
                    }
                }
            }
        }
        if (murNord != null){
            for (Porte p : murNord.getListePortes()){
                if (p.pieceSuivante != null){
                    for (Porte p2 : p.pieceSuivante.getMurSud().getListePortes()){
                        if (p2.pieceSuivante.getId().equals(id)){
                            p2.setPieceSuivante(null);
                        }
                    }
                }
            }
        }
        if (murEst != null){
            for (Porte p : murEst.getListePortes()){
                if (p.pieceSuivante != null){
                    for (Porte p2 : p.pieceSuivante.getMurOuest().getListePortes()){
                        if (p2.pieceSuivante.getId().equals(id)){
                            p2.setPieceSuivante(null);
                        }
                    }
                }
            }
        }
        if (murOuest != null){
            for (Porte p : murOuest.getListePortes()){
                if (p.pieceSuivante != null){
                    for (Porte p2 : p.pieceSuivante.getMurEst().getListePortes()){
                        if (p2.pieceSuivante.getId().equals(id)){
                            p2.setPieceSuivante(null);
                        }
                    }
                }
            }
        }
    }

    /**
     * Cette méthode permet d'afficher les attributs de la classe pour le débugg.
     * @return Un string avec les attributs de la classe.
     */
    @Override
    public String toString() {
        return "Piece{" +
                "nom='" + id +
                "}\n";
    }


}
