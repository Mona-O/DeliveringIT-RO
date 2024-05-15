package l3m.cyber.planner.utils;



import java.util.ArrayList;

public abstract class Partition {
    ////////Les attributs////////

    protected Integer nbElem;
    protected Integer k;
    protected Integer elemSpecial;
    protected ArrayList<Integer> elems;
    protected ArrayList<ArrayList<Integer>> parties;

    ////////////////////////////////////////////////

    ///////////////////////////////METHODES/////////////////////////



    public Partition(ArrayList<Integer> elems, Integer k,Integer elemSpecial ){
        this.elems = elems;
        this.nbElem = elems.size();
        this.k = k;
        this.elemSpecial = elemSpecial;
        this.parties = new ArrayList<>(k);
        partitionVide();
    }

    /////////////////////Constructeur 3 parametres//////////////////
    public Partition(Integer n,Integer k, Integer elemSpecial){
        this.k = k;
        this.elemSpecial = elemSpecial;
        this.nbElem = n;
        this.parties = new ArrayList<>(k);
        this.elems = integerList(n);
        partitionVide();
    }

    ////////////////////Constructeur 2 parametres//////////////
    public Partition(Integer n, Integer k){
        this(n, k, 0);
        partitionVide();
    }

    /////////////////////méthode abstraite void partitionne(Double[][] distance) a definire dans les sous classes//////
    public abstract void partitionne(Double[][] distance);

    //////////////////méthode partitionVide() qui alloue la mémoire nécessaire pour parties//////////////
    public void partitionVide(){
        ////supprimer toutes les parties existantes de la partition
        parties.clear();
        for (int i = 0; i < k; i++) {
            ArrayList<Integer> partie = new ArrayList<>();
            partie.add(elemSpecial); // Ajoute l'élément spécial à chaque partie
            parties.add(partie);
        }
    }

    /////////////////////méthode qui renvoie une description de notre partition/////////////
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Partition [nbElem=").append(nbElem);
        sb.append(", k=").append(k);
        sb.append(", elemSpecial=").append(elemSpecial);
        sb.append(", elems=").append(elems);
        sb.append(", parties=").append(parties);
        sb.append("]");
        return sb.toString();
    }

    //////////////////////méthode pour accéder à la i-ème partie de la partition////////////////
    public ArrayList<Integer> getPartie(Integer i){
        return this.parties.get(i);
    }

    // Fonction utilitaire pour générer une liste d'entiers de 0 à n-1
    public static ArrayList<Integer> integerList(int n) {
        ArrayList<Integer> list = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            list.add(i);
        }
        return list;
    }


}
