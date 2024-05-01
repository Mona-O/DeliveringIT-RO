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

    public Planner(PlannerParameter param) {
        this(param.matrix(), param.k(), param.start());
    }

    public Planner(Double[][] distances, int k, int debut) {
        if (distances == null || distances.length < k) {
            throw new IllegalArgumentException("Invalid matrix size or null matrix.");
        }
        if (debut < 0 || debut >= distances.length) {
            throw new IllegalArgumentException("Invalid start index.");
        }
        this.distances = distances;
        this.k = k;
        this.debut = debut;
        this.longTournees = new ArrayList<>();
    }

    public Planner() {
        this(null, 0, 0);
    }

    public void divise() {
        if (k > distances.length) {
            throw new IllegalArgumentException("k cannot be greater than the number of elements.");
        }
        partition = new PartitionKCentres(distances.length, k);
        partition.partitionne(distances);

        tournees = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            tournees.add(new ArrayList<>(partition.getPartie(i)));
        }
    }

    public PlannerResult result() {
        return new PlannerResult(tournees, longTournees);
    }

    public void calculeTournees() {
        longTournees.clear();
        for (ArrayList<Integer> tournee : tournees) {
            ArrayList<Integer> optimizedTournee = calculeUneTournee(tournee);
            double tourneeLength = calculeLongueurTournee(optimizedTournee);
            longTournees.add(tourneeLength);
        }
    }

    private ArrayList<Integer> calculeUneTournee(ArrayList<Integer> selec) {
        Double[][] sousMatrice = extraireSousMatrice(selec);
        Graphe graphe = new Graphe(sousMatrice, selec);
        Graphe tsp = graphe.tsp2();
        return new ArrayList<>(tsp.getNomSommets());
    }

    public void calculeLongTournees() {
        longTournees.clear(); // Clear previous results
        for (ArrayList<Integer> tournee : tournees) {
            double tourneeLength = calculeLongueurTournee(tournee);
            longTournees.add(tourneeLength);
        }
    }

    private double calculeLongueurTournee(ArrayList<Integer> tournee) {
        double length = 0.0;
        for (int i = 0; i < tournee.size() - 1; i++) {
            length += distances[tournee.get(i)][tournee.get(i + 1)];
        }
        length += distances[tournee.get(tournee.size() - 1)][tournee.get(0)]; // Completing the loop
        return length;
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