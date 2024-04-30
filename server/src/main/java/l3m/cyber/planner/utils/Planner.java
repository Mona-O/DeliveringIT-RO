package l3m.cyber.planner.utils;

import l3m.cyber.planner.requests.PlannerParameter;
import l3m.cyber.planner.responses.PlannerResult;

import java.util.ArrayList;
import java.util.List;


public class Planner {

    private Double[][] distances;
    private Integer k;
    private Integer debut;
    private Partition partition;

    private ArrayList<ArrayList<Integer>> tournees;
    private ArrayList<Double> longTournees;

    public Planner(PlannerParameter param) {
        this(param.matrix(), param.k(), param.start());
    }

    public Planner(Double[][] distances, int k, int debut) {
        this.distances = distances;
        this.k = k;
        this.debut = debut;
    }

    public Planner() {
        this(null, 0, 0);
    }

    public void divise() {
        // Utilisez la classe Partition pour diviser les livreurs en groupes
        partition = new PartitionAlea(distances.length, k);
        partition.partitionne(distances);

        tournees = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < k; i++) {
            tournees.add(partition.getPartie(i));
        }

        // Calculer les longueurs des tournées
        calculeLongTournees();
    }

    public PlannerResult result() {
        return new PlannerResult(tournees, longTournees);
    }

    public ArrayList<Integer> calculeUneTournee(ArrayList<Integer> selec) {
        // Calcule une tournée à partir d'une sélection de sommets
        Double[][] sousMatrice = extraireSousMatrice(selec);
        Graphe graphe = new Graphe(sousMatrice, selec);
        Graphe tsp = graphe.tsp2();
        return tsp.getNomSommets();
    }

    public void calculeTournees() {
        // Calcule toutes les tournées pour chaque groupe de livreurs
        for (int i = 0; i < tournees.size(); i++) {
            ArrayList<Integer> livreurs = partition.getPartie(i);
            tournees.add(calculeUneTournee(livreurs));
        }
    }

    public void calculeLongTournees() {
        longTournees = new ArrayList<Double>();
        for (ArrayList<Integer> listElem : tournees) {
            Double longueur = 0.0;
            for (int j = 0; j < listElem.size() - 1; j++) {
                longueur += distances[listElem.get(j)][listElem.get(j + 1)];
            }
            longueur += distances[listElem.getLast()][listElem.getFirst()];
        longTournees.add(longueur);
        }
    }
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
