package l3m.cyber.planner.utils;

import java.util.ArrayList;

public class Auxiliaire {
    

    public static ArrayList<Integer> integerList(int n){
        ArrayList<Integer> nomS=new ArrayList<>();
        for (int i = 0; i < n; i++) {
                nomS.add(i);
        }
        return nomS;
    }
    public static <T> boolean estCarree(T[][] matrice) {
        int lignes = matrice.length;
        if (lignes == 0) {
            return false; // La matrice est vide
        }
        for (T[] ligne : matrice) {
            if (ligne.length != lignes) {
                return false; // La matrice n'est pas carrée
            }
        }
        return true;
    }
    

    public static <T extends Number> boolean estCarreeSym(T[][] matrice) {
        if (!estCarree(matrice)) {
            return false; // La matrice n'est pas carrée
        }
        int lignes = matrice.length;
        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < lignes; j++) {
                if (!matrice[i][j].equals(matrice[j][i])) {
                    return false; // La matrice n'est pas symétrique
                }
            }
        }
        return true;
    }
    public static int[][] poidsToAdjacence(Double[][] poids) {
        int nbSommets = poids.length;
        int[][] adjacence = new int[nbSommets][nbSommets];
        
        for (int i = 0; i < nbSommets; i++) {
            for (int j = 0; j < nbSommets; j++) {
                // Si le poids est différent de zéro, il y a une arête entre les sommets i et j
                if (poids[i][j] != 0.0) {
                    adjacence[i][j] = 1;
                }
            }
        }
        
        return adjacence;
    }

}
