package com.example.cartehab.models;

import junit.framework.TestCase;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.BFSShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class HabitationTest {
    Habitation h;
    Piece p1;
    Piece p2;
    Piece p3;
    Piece p4;
    Piece p5;

    @Before
    public void setUp(){
        /*----
         _________
        |        |              N
        |    1   |         O       E
        |_*___*__|              S
        |   | 2  |
        | 4 |__*_|
        |   |  3 |
        |_*_|__*_|
        |    5   |
        |________|
        ----*/

        h = new Habitation();
        p1 = new Piece(h.getId());
        p2 = new Piece(h.getId());
        p3 = new Piece(h.getId());
        p4 = new Piece(h.getId());
        p5 = new Piece(h.getId());

        //-----------------------------
        Mur mS = new Mur(p1,"S","MUR_SUD");
        Mur mN = new Mur(p1, "N", "MUR_NORD");
        Mur mO = new Mur(p1, "O", "MUR_OUEST");
        Mur mE = new Mur(p1, "E", "MUR_EST");

        Porte p = new Porte(mS,0,0,0,0);
        p.setPieceSuivante(p2);
        mS.addPorte(p);
        p = new Porte(mS,0,0,0,0);
        p.setPieceSuivante(p4);
        mS.addPorte(p);

        p1.setMur(mS);
        p1.setMur(mN);
        p1.setMur(mO);
        p1.setMur(mE);

        //--------------------------
        mS = new Mur(p2,"S","MUR_SUD");
        mN = new Mur(p2, "N", "MUR_NORD");
        mO = new Mur(p2, "O", "MUR_OUEST");
        mE = new Mur(p2, "E", "MUR_EST");

        p = new Porte(mN,0,0,0,0);
        p.setPieceSuivante(p1);
        mN.addPorte(p);
        p = new Porte(mS,0,0,0,0);
        p.setPieceSuivante(p3);
        mS.addPorte(p);

        p2.setMur(mS);
        p2.setMur(mN);
        p2.setMur(mO);
        p2.setMur(mE);

        //---------------------
        mS = new Mur(p3,"S","MUR_SUD");
        mN = new Mur(p3, "N", "MUR_NORD");
        mO = new Mur(p3, "O", "MUR_OUEST");
        mE = new Mur(p3, "E", "MUR_EST");

        p = new Porte(mN,0,0,0,0);
        p.setPieceSuivante(p2);
        mN.addPorte(p);
        p = new Porte(mS,0,0,0,0);
        p.setPieceSuivante(p5);
        mS.addPorte(p);

        p3.setMur(mS);
        p3.setMur(mN);
        p3.setMur(mO);
        p3.setMur(mE);

        //----------------------
        mS = new Mur(p4,"S","MUR_SUD");
        mN = new Mur(p4, "N", "MUR_NORD");
        mO = new Mur(p4, "O", "MUR_OUEST");
        mE = new Mur(p4, "E", "MUR_EST");

        p = new Porte(mN,0,0,0,0);
        p.setPieceSuivante(p1);
        mN.addPorte(p);
        p = new Porte(mS,0,0,0,0);
        p.setPieceSuivante(p5);
        mS.addPorte(p);

        p4.setMur(mS);
        p4.setMur(mN);
        p4.setMur(mO);
        p4.setMur(mE);

        //-------------
        mS = new Mur(p5,"S","MUR_SUD");
        mN = new Mur(p5, "N", "MUR_NORD");
        mO = new Mur(p5, "O", "MUR_OUEST");
        mE = new Mur(p5, "E", "MUR_EST");

        p = new Porte(mN,0,0,0,0);
        p.setPieceSuivante(p3);
        mN.addPorte(p);
        p = new Porte(mN,0,0,0,0);
        p.setPieceSuivante(p4);
        mN.addPorte(p);

        p5.setMur(mS);
        p5.setMur(mN);
        p5.setMur(mO);
        p5.setMur(mE);


        h.addPiece(p1);
        h.addPiece(p2);
        h.addPiece(p3);
        h.addPiece(p4);
        h.addPiece(p5);

    }

    @Test
    public void listToGraphTest(){
        Graph<Piece,DefaultEdge> graph = h.listToGraph();
        System.out.println(graph.toString());

    }

    @Test
    public void getListSPTest(){
        Graph<Piece,DefaultEdge> graph = h.listToGraph();
        System.out.println(graph.toString());
        System.out.println("-----------------");

        List<Piece> list = h.getListSP(graph,p1,p5);
        System.out.println(list.toString());
        System.out.println("-----------------");
        list = h.getListSP(graph,p4,p3);
        System.out.println(list.toString());
        System.out.println("-----------------");
        list = h.getListSP(graph,p3,p1);
        System.out.println(list.toString());

    }
}