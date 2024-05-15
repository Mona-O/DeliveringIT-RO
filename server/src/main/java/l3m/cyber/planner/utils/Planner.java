package l3m.cyber.planner.utils;

import l3m.cyber.planner.requests.PlannerParameter;
import l3m.cyber.planner.responses.PlannerResult;
import java.util.ArrayList;

public class Planner {
    private Double[][] distances;
    private Integer k;
    private Integer debut;
    private Partition partition;
    private ArrayList<ArrayList<Integer>> tournees;
    private ArrayList<Double> longTournees;

    // Constructeur prenant en paramètre un objet PlannerParameter
    public Planner(PlannerParameter param) {
        this(param.matrix(), param.k(), param.start());
    }

    // Constructeur prenant en paramètre les distances, le nombre de tournées et l'indice de départ
    public Planner(Double[][] distances, int k, int debut) {
        // Vérification de la validité des paramètres
        if (distances == null || distances.length < k) {
            throw new IllegalArgumentException("Invalid matrix size or null matrix.");
        }
        if (debut < 0 || debut >= distances.length) {
            throw new IllegalArgumentException("Invalid start index.");
        }
        // Initialisation des attributs de la classe
        this.distances = distances;
        this.k = k;
        this.debut = debut;
        this.longTournees = new ArrayList<>();
    }

    // Constructeur par défaut
    public Planner() {
        this(null, 0, 0);
    }

    // Méthode pour diviser les points en tournées
    public void divise() {
        // Vérification de la validité du nombre de tournées
        if (k > distances.length) {
            throw new IllegalArgumentException("k cannot be greater than the number of elements.");
        }
        // Partitionnement des points en utilisant l'algorithme de partitionnement spécifié
        partition = new PartitionKCentres(distances.length, k);
        partition.partitionne(distances);
        // Initialisation de la liste des tournées
        tournees = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            tournees.add(new ArrayList<>(partition.getPartie(i)));
        }
    }

    // Méthode pour obtenir le résultat de la planification
    public PlannerResult result() {
        return new PlannerResult(tournees, longTournees);
    }

    // Méthode pour calculer les tournées en optimisant chacune d'entre elles
    public void calculeTournees() {
        longTournees.clear();
        for (ArrayList<Integer> tournee : tournees) {
            ArrayList<Integer> optimizedTournee = calculeUneTournee(tournee);
            double tourneeLength = calculeLongueurTournee(optimizedTournee);
            longTournees.add(tourneeLength);
        }
    }

    // Méthode pour calculer une tournée optimisée
    private ArrayList<Integer> calculeUneTournee(ArrayList<Integer> selec) {
        // Obtenir la sous-matrice des distances pour les points sélectionnés
        Double[][] sousMatrice = getSousMatrice(selec);
        // Créer un graphe à partir de la sous-matrice des distances
        Graphe graphe = new Graphe(sousMatrice, selec);
        // Appliquer l'algorithme TSP pour optimiser la tournée
        Graphe tsp = graphe.tsp2();
        // Retourner la liste des sommets visités dans l'ordre optimal
        return new ArrayList<>(tsp.getNomSommets());
    }

    // Méthode pour calculer les longueurs de toutes les tournées
    public void calculeLongTournees() {
        longTournees.clear();
        for (ArrayList<Integer> tournee : tournees) {
            double tourneeLength = calculeLongueurTournee(tournee);
            longTournees.add(tourneeLength);
        }
    }

    // Méthode pour calculer la longueur d'une tournée
    private double calculeLongueurTournee(ArrayList<Integer> tournee) {
        double length = 0.0;
        for (int i = 0; i < tournee.size() - 1; i++) {
            length += distances[tournee.get(i)][tournee.get(i + 1)];
        }
        length += distances[tournee.get(tournee.size() - 1)][tournee.get(0)];
        return length;
    }

    // Méthode pour obtenir la sous-matrice des distances pour les points sélectionnés
    private Double[][] getSousMatrice(ArrayList<Integer> select) {
        int n = select.size();
        Double[][] sousMatrice = new Double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sousMatrice[i][j] = distances[select.get(i)][select.get(j)];
            }
        }
        return sousMatrice;
    }
}