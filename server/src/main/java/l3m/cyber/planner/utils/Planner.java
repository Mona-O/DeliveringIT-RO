package l3m.cyber.planner.utils;

import l3m.cyber.planner.requests.PlannerParameter;
import l3m.cyber.planner.responses.PlannerResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;


// Classe Planner : planifie les tournées de livraison pour un ensemble de lieux donnés
public class Planner {

    private Double[][] distances; // distances entre chaque paire de lieux
    private Integer k; // nombre de livreurs disponibles
    private Integer debut; // point de départ des tournées
    private Partition partition; // partitionne les lieux entre les livreurs

    private ArrayList<ArrayList<Integer>> tournees; // tournées de chaque livreur
    private ArrayList<Double> longTournees; // longueurs des tournées

    // Constructeur prenant un objet PlannerParameter en paramètre
    public Planner(PlannerParameter param) {
        this(param.matrix(), param.k(), param.start());
    }

    // Constructeur prenant un tableau 2D de doubles et deux entiers en paramètre
    public Planner(Double[][] distances, int k, int debut) {
        this.distances = distances;
        this.k = k;
        this.debut = debut;
    }

    // Constructeur par défaut
    public Planner() {
        this(null, 0, 0);
    }

    // Méthode divise : divise les lieux entre les livreurs et calcule les tournées
    public void divise() {
        if (k > distances.length) {
            throw new IllegalArgumentException("k cannot be greater than the number of elements to partition");
        }
    
        // Create a partition object and partition the elements
        partition = new PartitionKCentres(distances.length, k);
        partition.partitionne(distances);

        tournees = new ArrayList<ArrayList<Integer>>();

        for(int i=0; i<k; i++) {
            tournees.add(partition.getPartie(i));
        }
    
        // Calculate the tournees and longTournees
       
    }

    // Méthode result : retourne un objet PlannerResult contenant les tournées et leurs longueurs
    public PlannerResult result() {
        return new PlannerResult(tournees, longTournees);
    }

    // Méthode calculeTournees : calcule les tournées pour chaque groupe de livreurs
    public void calculeTournees() {
       /*  HashSet<Integer> livreursInclus = new HashSet<>(); // ensemble de livreurs déjà inclus dans une tournée
        for (int i = 0; i < tournees.size(); i++) {
            ArrayList<Integer> livreurs = partition.getPartie(i);
            if (livreursInclus.containsAll(livreurs)) {
                // Si tous les livreurs d'une partie sont déjà inclus, passez à la partie suivante
                continue;
            }
            LinkedHashSet<Integer> tournee = calculeUneTournee(livreurs);
            tournees.add(new ArrayList<>(tournee));
            livreursInclus.addAll(tournee);
        } */ 

        for (int i = 0; i < tournees.size(); i++) {
            ArrayList<Integer> listElem = tournees.get(i);
            ArrayList<Integer> tournee = calculeUneTournee(listElem);
            tournees.set(i, tournee);
        }
    }

    // Méthode calculeUneTournee : calcule une tournée pour un ensemble donné de livreurs
    public ArrayList<Integer> calculeUneTournee(ArrayList<Integer> selec) {
        // Crée une sous-matrice à partir des distances entre les lieux sélectionnés
        Double[][] sousMatrice = extraireSousMatrice(selec);
        // Crée un graphe à partir de la sous-matrice
        Graphe graphe = new Graphe(sousMatrice, selec);
        // Résout le problème du voyageur de commerce pour le graphe
        Graphe tsp = graphe.tsp2();

        // Convertit le résultat du TSP en un ensemble d'entiers sans doublons
        ArrayList<Integer> tournee = new ArrayList<>(tsp.getNomSommets());

        return tournee;
    }

    // Méthode calculeLongTournees : calcule la longueur de chaque tournée
    public void calculeLongTournees() {
        longTournees = new ArrayList<Double>();
        for (ArrayList<Integer> listElem : tournees) {
            Double longueur = 0.0;
            for (int j = 0; j < listElem.size() - 1; j++) {
                longueur += distances[listElem.get(j)][listElem.get(j + 1)];
            }
            longueur += distances[listElem.get(0)][listElem.get(listElem.size() - 1)];
            longTournees.add(longueur);
        }
    }


    // Méthode extraireSousMatrice : extrait une sous-matrice du tableau distances
    private Double[][] extraireSousMatrice(ArrayList<Integer> livreurs) {
        int n = livreurs.size();
        Double[][] sousMatrice = new Double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sousMatrice[i][j] = distances[livreurs.get(i)][livreurs.get(j)];
            }
        }
        return sousMatrice;
    }

}