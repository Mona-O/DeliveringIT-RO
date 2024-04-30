package l3m.cyber.planner.utils;

import java.util.Random;

public class PartitionAlea extends Partition{
    ////////////Constructeur/////////////////
    public PartitionAlea(Integer n, Integer k){
        super(n,k);
    }

    public void partitionne(Double[][] distances) {
        // Appel de la méthode partitionVide pour initialiser la partition
        partitionVide();

        // Création d'un objet Random pour générer des nombres aléatoires
        Random random = new Random();

        // Parcours de tous les éléments à partitionner (sauf l'élément spécial)
        for (int i = 0; i < nbElem; i++) {
            if (!(i == elemSpecial)){
                // Génération d'un indice de partie aléatoire entre 0 et k-1 inclus
                int randomPart = random.nextInt(k);

                // Ajout de l'élément courant à la partie aléatoire sélectionnée
                parties.get(randomPart).add(elems.get(i));
            }
        }
    }


}
