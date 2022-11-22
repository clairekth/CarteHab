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

public class Habitation implements Serializable {
    protected String id;
    protected ArrayList<Piece> listePieces;
    protected String name;
    protected int lastNumPiece;

    public Habitation(){
        listePieces = new ArrayList<>();
        id = "HAB" + FabriqueNumero.getInstance().getNumeroHabitation();
        name = id;
    }

    public void setLastNumPiece(int i){
        lastNumPiece = i;
    }
    public int getLastNumPiece(){
        return lastNumPiece;
    }
    public void addPiece(Piece p){
        listePieces.add(p);
    }

    public void remove(Piece piece){
        listePieces.remove(piece);
    }

    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public Piece getPiece(String nom){
        for (Piece p : listePieces){
            if (p.getNom().equals(nom)){
                return p;
            }
        }
        return null;
    }

    public ArrayList<Piece> getListePieces(){
        return listePieces;
    }
    @Override
    public String toString() {
        return "Habitation{" +
                "id=" + id +
                "listePieces=" + listePieces +
                '}';
    }


    public boolean nomPieceExisteDeja(String nom){
        for (Piece p : listePieces){
            if (p.getNom().equalsIgnoreCase(nom)){
                return true;
            }
        }
        return false;
    }

    public void setName(String name){
        this.name = name;
    }

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

    public List<Piece> getListSP(Graph<Piece,DefaultEdge> graph, Piece src, Piece dest){
        BFSShortestPath<Piece,DefaultEdge> graphSP = new BFSShortestPath<>(graph);
        GraphPath<Piece,DefaultEdge> sp = graphSP.getPath(src,dest);
        if (sp == null){
            return null;
        }
        return sp.getVertexList();

    }

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

    public boolean verificationCheminPossible(Piece src, Piece dest){
        if (getListSP(listToGraph(),src,dest) == null){
            return false;
        }
        return true;
    }

    public Graph<String, DefaultEdge> nomPieceToGraph(){
        Graph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        for (Piece p : listePieces){
            graph.addVertex(p.getNom());
            if (p.getMurEst() != null){
                for (Porte po : p.getMurEst().getListePortes()){
                    if (po.getPieceSuivante() != null){
                        if (!graph.containsVertex(po.getPieceSuivante().getNom())){
                            graph.addVertex(po.getPieceSuivante().getNom());
                        }
                        graph.addEdge(p.getNom(),po.getPieceSuivante().getNom());
                    }
                }
            }
            if (p.getMurNord() != null){
                for (Porte po : p.getMurNord().getListePortes()){
                    if (po.getPieceSuivante() != null){
                        if (!graph.containsVertex(po.getPieceSuivante().getNom())) {
                            graph.addVertex(po.getPieceSuivante().getNom());
                        }
                        graph.addEdge(p.getNom(),po.getPieceSuivante().getNom());

                    }
                }
            }
            if (p.getMurSud() != null){
                for (Porte po : p.getMurSud().getListePortes()){
                    if (po.getPieceSuivante() != null){
                        if (!graph.containsVertex(po.getPieceSuivante().getNom())){
                            graph.addVertex(po.getPieceSuivante().getNom());
                        }
                        graph.addEdge(p.getNom(),po.getPieceSuivante().getNom());
                    }
                }
            }
            if (p.getMurOuest() != null){
                for (Porte po : p.getMurOuest().getListePortes()){
                    if (po.getPieceSuivante() != null){
                        if (!graph.containsVertex(po.getPieceSuivante().getNom())){
                            graph.addVertex(po.getPieceSuivante().getNom());
                        }
                        graph.addEdge(p.getNom(),po.getPieceSuivante().getNom());
                    }
                }
            }
        }
        return graph;
    }


}
