package l3m.cyber.planner.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PartitionKCentres extends Partition {
    public PartitionKCentres(int n, int k) {
        super(n, k);
    }

    @Override
    public void partitionne(Double[][] distances) {
        // Initialisation
        List<Integer> centers = new ArrayList<>();
        int[] nearestCenter = new int[nbElem];
        double[] minDistToCenter = new double[nbElem];
        Arrays.fill(nearestCenter, -1);
        Arrays.fill(minDistToCenter, Double.MAX_VALUE);

        // Ajouter l'élément spécial à chaque centre initialement
        centers.add(elemSpecial);

        // Mettre à jour les distances initiales pour l'élément spécial
        for (int i = 0; i < nbElem; i++) {
            nearestCenter[i] = elemSpecial;
            minDistToCenter[i] = distances[elemSpecial][i];
        }

        // Sélectionner les k-1 autres centres
        for (int j = 1; j < k; j++) {
            int nextCenter = findFurthest(minDistToCenter);
            centers.add(nextCenter);
            for (int i = 0; i < nbElem; i++) {
                double distance = distances[i][nextCenter];
                if (distance < minDistToCenter[i]) {
                    minDistToCenter[i] = distance;
                    nearestCenter[i] = nextCenter;
                }
            }
        }


        for (int i = 0; i < nbElem; i++) {
            if (i != elemSpecial) { // Ne pas réajouter l'élément spécial
                int centerIndex = centers.indexOf(nearestCenter[i]);
                this.parties.get(centerIndex).add(i);
            }
        }
    }

    private int findFurthest(double[] distances) {
        double maxDistance = -1;
        int furthestIndex = -1;
        for (int i = 0; i < distances.length; i++) {
            if (distances[i] > maxDistance) {
                maxDistance = distances[i];
                furthestIndex = i;
            }
        }
        return furthestIndex;
    }
}