package l3m.cyber.planner.utils;

import java.util.ArrayList;
import java.util.Collections;

public class RandomPartition extends Partition {
    private Double improvement;
    private static int nbEssais = 10000;
    public RandomPartition(int n, int k, Double improvement) {
        super(n,k);
        this.improvement = improvement;
    }


    @Override
    public void partitionne(Double[][] distance) {
        ArrayList<ArrayList<Integer>> bestPartition = null;
        double bestScore = Double.MAX_VALUE;

        for(int i = 0 ; i < nbEssais ; i++) {
            Collections.shuffle(elems);
            partitionVide();
            distributeElements();

            double currentScore = evaluatePartition(distance);
            if (currentScore < bestScore - improvement) {
                bestScore = currentScore;
                bestPartition = deepCopy(parties);
            }

        }
        System.out.println(bestScore);

        if (bestPartition != null) {
            parties = bestPartition;
        }
    }

    private void distributeElements() {
        int index = 0;
        for (Integer elem : elems) {
            if (!elem.equals(elemSpecial)) {
                parties.get(index % k).add(elem);
                index++;
            }
        }
    }

    private double evaluatePartition(Double[][] distance) {
        double score = 0.0;
        for (ArrayList<Integer> partition : parties) {
            for (Integer elem : partition) {
                for (Integer otherElem : partition) {
                    score += distance[elem][otherElem];
                }
            }
        }
        return score;
    }

    private ArrayList<ArrayList<Integer>> deepCopy(ArrayList<ArrayList<Integer>> original) {
        ArrayList<ArrayList<Integer>> copy = new ArrayList<>(original.size());
        for (ArrayList<Integer> list : original) {
            copy.add(new ArrayList<>(list));
        }
        return copy;
    }
}
