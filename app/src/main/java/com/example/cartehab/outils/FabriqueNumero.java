package com.example.cartehab.outils;

/**
 * Singleton permettant de compter les habitations et les pièces pour les identifiants.
 * @author Claire Kurth
 */
public class FabriqueNumero {
    /**
     * L'instance.
     */
    private static final FabriqueNumero instance = new FabriqueNumero();

    /**
     * Le compteur d'habitation.
     */
    private int cptHabitation;

    /**
     * Le compteur de pièces.
     */
    private int cptPiece;

    /**
     * Constructeur.
     */
    private FabriqueNumero() {}

    /**
     * Cette méthode permet de récupérer l'instance.
     * @return l'instance.
     */
    public static FabriqueNumero getInstance() {
        return instance;
    }

    /**
     * Cette méthode permet de récupérer le numéro d'habitation et de l'incrémenter.
     * @return le numéro d'habitation.
     */
    public int getNumeroHabitation() {
        cptHabitation++;
        return cptHabitation ;
    }

    /**
     * Cette méthode permet de récupérer le numéro de la pièce et de l'incrémenter.
     * @return le numéro de la pièce.
     */
    public int getNumeroPiece() {
        cptPiece++;
        return cptPiece ;
    }

    /**
     * Cette méthode permet de reset le compteur de pièces.
     */
    public void resetCompteurPiece(){
        cptPiece = 0;
    }

    /**
     * Cette méthode permet de récupérer le numéro d'habitation sans l'incrémenter.
     * @return le numéro d'habitation.
     */
    public int getNumeroHabitationSansIncre(){
        return cptHabitation;
    }
    /**
     * Cette méthode permet de récupérer le numéro de la pièce sans l'incrémenter.
     * @return le numéro de la pièce.
     */
    public int getNumeroPieceSansIncre(){
        return cptPiece;
    }

    /**
     * Cette méthode permet de set le compteur d'habitation.
     * @param i le nouveau numéro du compteur.
     */
    public void setCptHabitation(int i ){
        cptHabitation = i;
    }

    /**
     * Cette méthode permet de set le compteur de pièces.
     * @param i le nouveau numéro du compteur.
     */
    public void setCptPiece(int i ){
        cptPiece = i;

    }
}
