package com.example.cartehab.models;

import com.example.cartehab.outils.FabriqueNumero;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.BFSShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * La classe Habitation représente une habitation.
 * @author Claire Kurth
 */
public class Habitation implements Serializable {
    /**
     * L'idenfitiant de l'habitation.
     */
    protected String id;
    /**
     * La liste des pièces de l'habitation.
     */
    protected ArrayList<Piece> listePieces;
    /**
     * Le nom de l'habitation.
     */
    protected String name;
    /**
     * Le numéro de la dernière pièce de l'habitation.
     */
    protected int lastNumPiece;

    /**
     * Constructeur.
     */
    public Habitation(){
        listePieces = new ArrayList<>();
        id = "HAB" + FabriqueNumero.getInstance().getNumeroHabitation();
        name = id;
    }

    /**
     * Cette méthode permet de set le numéro de la dernière pièce créée.
     * @param i Le numéro de la dernière pièce créée.
     */
    public void setLastNumPiece(int i){
        lastNumPiece = i;
    }

    /**
     * Cette méthode permet de récupérer le numéro de la dernière pièce créée.
     * @return Le numéro de la denrière pièce créée.
     */
    public int getLastNumPiece(){
        return lastNumPiece;
    }

    /**
     * Cette méthode permet d'ajouter une Piece à l'habitation.
     * @param p La pièce à ajouter.
     */
    public void addPiece(Piece p){
        listePieces.add(p);
    }

    /**
     * Cette méthode permet d'enlever une Piece à l'habitation.
     * @param piece La pièce à enlever.
     */
    public void remove(Piece piece){
        listePieces.remove(piece);
    }

    /**
     * Cette méthode permet de récupérer l'identifiant de l'habitation.
     * @return L'identifiant de l'habitation.
     */
    public String getId(){
        return id;
    }

    /**
     * Cette méthode permet de récupérer le nom de l'habitation.
     * @return Le nom de l'habitation
     */
    public String getName(){
        return name;
    }

    /**
     * Cette méthode permet de récupérer la pièce portant le nom passé en paramètre.
     * @param nom Le nom de la pièce à récupérer.
     * @return La pièce portant le nom en passé en paramètre, null si elle n'existe pas.
     */
    public Piece getPiece(String nom){
        for (Piece p : listePieces){
            if (p.getNom().equals(nom)){
                return p;
            }
        }
        return null;
    }

    /**
     * Cette méthode permet de récupérer la liste des pièces de l'habitation.
     * @return La liste des pièces.
     */
    public ArrayList<Piece> getListePieces(){
        return listePieces;
    }

    /**
     * Cette méthode permet d'afficher les attributs de la classe pour le débugg.
     * @return Un string avec les attributs de la classe.
     */
    @Override
    public String toString() {
        return "Habitation{" +
                "id=" + id +
                "listePieces=" + listePieces +
                '}';
    }


    /**
     * Cette méthode permet de savoir si le nom passé en paramètre est déjà attribué à une pièce.
     * @param nom Le nom que l'on veux vérifier.
     * @return true si le nom existe déjà, false sinon.
     */
    public boolean nomPieceExisteDeja(String nom){
        for (Piece p : listePieces){
            if (p.getNom().equalsIgnoreCase(nom)){
                return true;
            }
        }
        return false;
    }

    /**
     * Cette méthode permet de set le nom de l'habitation.
     * @param name Le nom de l'habitation.
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Cette méthode permet de transformer la liste des pièces de l'habitation en graphe.
     * @return Le graphe créé à partir de la liste des pièces.
     */
    public Graph<Piece, DefaultEdge> listToGraph(){
        Graph<Piece, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        for (Piece p : listePieces){
            graph.addVertex(p);
            if (p.getMurEst() != null){
                for (Porte po : p.getMurEst().getListePortes()){
                    if (po.getPieceSuivante() != null){
                        if (!graph.containsVertex(po.getPieceSuivante())){
                            graph.addVertex(po.getPieceSuivante());
                        }
                        graph.addEdge(p,po.getPieceSuivante());
                    }
                }
            }
            if (p.getMurNord() != null){
                for (Porte po : p.getMurNord().getListePortes()){
                    if (po.getPieceSuivante() != null){
                        if (!graph.containsVertex(po.getPieceSuivante())) {
                            graph.addVertex(po.getPieceSuivante());
                        }
                        graph.addEdge(p,po.getPieceSuivante());

                    }
                }
            }
            if (p.getMurSud() != null){
                for (Porte po : p.getMurSud().getListePortes()){
                    if (po.getPieceSuivante() != null){
                        if (!graph.containsVertex(po.getPieceSuivante())){
                            graph.addVertex(po.getPieceSuivante());
                        }
                        graph.addEdge(p,po.getPieceSuivante());
                    }
                }
            }
            if (p.getMurOuest() != null){
                for (Porte po : p.getMurOuest().getListePortes()){
                    if (po.getPieceSuivante() != null){
                        if (!graph.containsVertex(po.getPieceSuivante())){
                            graph.addVertex(po.getPieceSuivante());
                        }
                        graph.addEdge(p,po.getPieceSuivante());
                    }
                }
            }
        }
        return graph;
    }

    /**
     * Cette méthode permet de récupérer la liste des pièces du plus court chemin allant d'une pièce source à une pièce de destination à partir d'un graphe.
     * @param graph Le graphe contenant toutes les pièces.
     * @param src La pièce de départ.
     * @param dest La pièce d'arrivée.
     * @return La liste des pièces du plus court chemin.
     */
    public List<Piece> getListSP(Graph<Piece,DefaultEdge> graph, Piece src, Piece dest){
        BFSShortestPath<Piece,DefaultEdge> graphSP = new BFSShortestPath<>(graph);
        GraphPath<Piece,DefaultEdge> sp = graphSP.getPath(src,dest);
        if (sp == null){
            return null;
        }
        return sp.getVertexList();

    }

    /**
     * Cette méthode permet de retourner l'indication pour aller d'une pièce source à une pièce destination.
     * @param src La pièce de départ.
     * @param dest La pièce d'arrivée.
     * @return L'indication à suivre.
     */
    public String indicationGPS(Piece src, Piece dest){
        StringBuilder bd = new StringBuilder();
        if (!verificationCheminPossible(src, dest)){
            bd.append("Pas de chemin possible vers la pièce de destination.");
            return bd.toString();
        }
        List<Piece> listPiece = this.getListSP(listToGraph(),src,dest);

        Piece pSuivante = listPiece.get(1);
        if ( src.getMurEst() != null && src.getMurEst().porteVers(pSuivante)){
            bd.append("Tournez vous vers le mur Est et prenez la porte vers : " + pSuivante.getNom());
        } else if (src.getMurNord() != null && src.getMurNord().porteVers(pSuivante)){
            bd.append("Tournez vous vers le mur Nord et prenez la porte vers : " + pSuivante.getNom());
        }else if (src.getMurSud() != null && src.getMurSud().porteVers(pSuivante)){
            bd.append("Tournez vous vers le mur Sud et prenez la porte vers : " + pSuivante.getNom());
        } else if (src.getMurOuest() != null && src.getMurOuest().porteVers(pSuivante)){
            bd.append("Tournez vous vers le mur Ouest et prenez la porte vers : " + pSuivante.getNom());
        }

        return bd.toString();

    }

    /**
     * Cette méthode permet de vérifier qu'un chemin existe entre deux pièces.
     * @param src La pièce de départ.
     * @param dest La pièce d'arrivée.
     * @return true si un chemin existe, false sinon.
     */
    public boolean verificationCheminPossible(Piece src, Piece dest){
        if (getListSP(listToGraph(),src,dest) == null){
            return false;
        }
        return true;
    }

}
