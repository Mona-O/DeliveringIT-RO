package l3m.cyber.planner.utils;

import l3m.cyber.planner.requests.PlannerParameter;
import l3m.cyber.planner.responses.PlannerResult;

import java.util.ArrayList;
import java.util.List;


public class Planner{

    private Double[][] distances;
    private Integer k;
    private Integer debut;
    private Partition partition;

    public Planner(PlannerParameter param){
        this(param.matrix(), param.k(), param.start());
    }

    public Planner(Double[][] distances, int k, int debut){
        this.distances = distances;
        this.k = k;
        this.debut = debut;
    }

    public Planner(){
        this(null, 0, 0);
    }


        public void divise() {
            // Utilisez la classe Partition pour diviser les livreurs en groupes
            Partition partition = new PartitionKCentres(distances.length, k);
            partition.partitionne(distances);

        }

        public PlannerResult result() {
            ArrayList<ArrayList<Integer>> tournees = new ArrayList<>();
            ArrayList<Double> longTournees = new ArrayList<>();

            // Pour chaque groupe de livreurs, calculer la tournée et la longueur de la tournée
            for (int i = 0; i < k; i++) {
                ArrayList<Integer> livreurs = partition.getPartie(i);
                Double[][] sousMatrice = extraireSousMatrice(livreurs);
                Graphe graphe = new Graphe(sousMatrice, livreurs);
                Graphe tsp = graphe.tsp2();
                tournees.add(tsp.getNomSommets());
                longTournees.add(calculerLongueurTournee(tsp.getNomSommets()));
            }

            return new PlannerResult(tournees, longTournees);
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

        private double calculerLongueurTournee(ArrayList<Integer> tournee) {
            double longueur = 0.0;
            for (int i = 0; i < tournee.size() - 1; i++) {
                longueur += distances[tournee.get(i)][tournee.get(i + 1)];
            }
            // Ajouter la distance du dernier au premier sommet pour fermer la boucle
            longueur += distances[tournee.get(tournee.size() - 1)][tournee.get(0)];
            return longueur;
        }
    }
