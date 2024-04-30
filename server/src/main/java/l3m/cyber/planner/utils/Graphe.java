package l3m.cyber.planner.utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.Collections;
import java.util.List;


public class Graphe{
    
    //////////////  ATTRIBUTS/////////////////////////////////////////7

    protected int nbSommets;
    private int[][] adj; //matrice d'adjacence remplie de 0 t 1, symmétrique
    protected Double[][] poidsA ;//matrice des poids des arêtes , pourra etre null si pas de poids
    private ArrayList<Integer> nomSommets; // au debut on peut utilise nb 0 a nbS-1 

    
    
    ///////////////// CONSTRUCTEUR ////////////////////////////////7   
    public Graphe(int nbSommets, List<Triplet> triplets) {
        this(nbSommets);
        for (Triplet triplet : triplets) {
            int[] s=triplet.getSommets();
            ajouteArrete(s[0],s[1]);
            if (triplet.getPoids() != 0.0) {
                ajouterPoids(s[0],s[1],triplet.getPoids());
            }
        }
    }
    /**SPEC CONSTRUCTEUR
     * @param: n:int le nombre de sommets
     * @return graphe a n sommets, avec les noms des sommets=0 à n-1 
     */
    public Graphe(int n){
         this.nbSommets=n;
         this.pondereAdj();
         this.nomSommets=this.tsp1(n); // a changer quand on change les noms + tsp
         this.pondereAretes();
     }
    /**SPEC CONSTRUCTEUR
     * @param: poidsA tableau de poids de chque arete, 
     *          nomSommets: array de int represetnant les nom distincts des sommets du graphe
     * @return : Graphe avec ces elements 
     */
    public Graphe( Double[][] poidsA,ArrayList<Integer> nomSommets){
        if (Auxiliaire.estCarreeSym(poidsA)){
            this.nbSommets=poidsA.length;
            adj=Auxiliaire.poidsToAdjacence(poidsA);
            this.nomSommets=nomSommets;
            this.poidsA=poidsA;
        }
        else{
            throw new Error("poidsA donné faux");
        }

    }
    ////////////////////////////////////////GETTER////////////////////////////////7
    public ArrayList<Integer> getNomSommets(){
        return this.nomSommets;
    }
    ///////////////////////////////////////// SETTER///////////////////////7


    public void ajouterPoids(int s1, int s2, double poids) {
        poidsA[s1][s2] = poids;
        poidsA[s2][s1] = poids;
    }

    public void ajouteArrete(int s1, int s2){
        ajouteArrete(s1, s2,(double) 1);
     }
    public void ajouteArrete(int s1, int s2, double p){
        this.adj[s1][s2]=1;
        this.adj[s2][s1]=1;
        this.ajouterPoids(s1, s2, p);
    }


    public void retirerArrete(int s1, int s2){
        this.adj[s1][s2]=0;
        this.adj[s2][s1]=0;
        this.poidsA[s2][s1]= (double) 0;
        this.poidsA[s1][s2]=(double) 0;
    }
    ///////////////////////7SETTER DEFAUT MATRICE POIDS ET ADJ  ////////////////////////////////////77
        /**SPEC
     * @param: none
     * @return: change la matrice de poids ponderes de null vers une matrice remplie de 1 pour toutes les aretes par defaut*/

     private void pondereAretes(){
        Double[][] tmp= new Double[this.nbSommets][this.nbSommets];
        for (int i = 0; i < this.nbSommets; i++) {
            for(int j=0;j<this.nbSommets;j++){
                if (i!=j){
                  tmp[i][j]=(double) 1;
                }

            }
        }
        this.poidsA=tmp;
    }

    private void pondereAdj(){
        int[][] tmp= new int[this.nbSommets][this.nbSommets];
        for (int i = 0; i < this.nbSommets; i++) {
            for(int j=0;j<this.nbSommets;j++){
                if (i!=j){
                    tmp[i][j]= 1;
                  }
            }
        }

        this.adj=tmp;
    }


    ////////////////////////////////////////FONCTIONS DIVERS /////////////////////////////////77
    public Graphe clone() {
        Graphe clone = new Graphe(this.nbSommets);

        clone.poidsA=this.poidsA.clone();
        clone.adj=this.adj.clone();

        // Copier les noms des sommets
        clone.nomSommets = new ArrayList<>(this.nomSommets);

        return clone;
    }
    public ArrayList<Integer> tsp1(int n){
        return Auxiliaire.integerList(n);
     }
     public ArrayList<Triplet> listeAretes(){
         ArrayList<Triplet> LA= new ArrayList<>();
         Triplet tmp;
         double poids;
 
         for (int i=0;i<nbSommets;i++){
             for (int j=0;j<nbSommets;j++){
                 if (this.adj[i][j]==1){
                     poids=this.poidsA[i][j];
                     tmp=new Triplet(j, i, poids);
                     LA.add(tmp);
 
                 }
             }
         } return LA;
     }
     public ArrayList<Triplet> aretesTriees(boolean croissant){
        ArrayList<Triplet> L_tmp=this.listeAretes();
        Collections.sort(L_tmp);

        if (!croissant){
            Collections.reverse(L_tmp);
        }
        return L_tmp;
    }
    ///////////////////////////////////////////////AFFICHAGE///////////////////////////
    
    /**SPEC toString 
     * @return: renvoie une chaine de caractere qui est tous les elements de l'element graphe de notre classe Graphe
     */
    public String toString(){
        String s="";
        s=s+"nb sommets: "+this.nbSommets+"\nmatrice d'adj=\n"+this.adjacenceToString()+"\nmatrice poids\n"+this.poidsToString()+"\nnom sommets=\n"+this.nomsSommetsToString()+"\n";
        return s;
    }


    private String adjacenceToString() {
        String result = "";
        for (int i = 0; i < adj.length; i++) {
            for (int j = 0; j < adj[0].length; j++) {
                result += adj[i][j] + " ";
            }
            result += "\n";
        }
        return result;
    }

    private String poidsToString() {

        String result = "";
        for (int i = 0; i < poidsA.length; i++) {
            for (int j = 0; j < poidsA[0].length; j++) {
                result += poidsA[i][j] + " ";
            }
            result += "\n";
        }
        return result;
    }

    private String nomsSommetsToString() {
        String result = "";
        for (Integer nom : nomSommets) {
            result += nom + " ";
        }
        return result;
    }
    private void afficherAretesTriees(List<Triplet> aretesTriees) {
        System.out.println("Liste des arêtes triées :");
        for (Triplet triplet : aretesTriees) {
            int[] sommets = triplet.getSommets();
            double poids = triplet.getPoids();
            System.out.println("(" + sommets[0] + ", " + sommets[1] + ") : poids = " + poids);
        }
    }
    //////////////////////////////////////////////PARCOURS DFS DIVERS ////////////////////////////7
        private List<Triplet> dfs(int u, boolean[] visite) {
            List<Triplet> tripletsParcourus = new ArrayList<>();
            Stack<Integer> pile = new Stack<>();
            pile.push(u);
            while (!pile.isEmpty()) {
                int sommet = pile.pop();
                visite[sommet] = true;
                for (int voisin = 0; voisin < nbSommets; voisin++) {
                    if (adj[sommet][voisin] == 1 && !visite[voisin]) {
                        pile.push(voisin);
                        tripletsParcourus.add(new Triplet(sommet, voisin, poidsA[sommet][voisin]));
                    }
                }
            }
            return tripletsParcourus;
        }
    
    
        private ArrayList<Triplet> dfsUnique(int u, boolean[] visite) {
            ArrayList<Triplet> tripletsParcourusUniques = new ArrayList<>();
            Stack<Integer> pile = new Stack<>();
            pile.push(u);
            while (!pile.isEmpty()) {
    
                int sommet = pile.pop();
                if (!visite[sommet]){
    
    
                visite[sommet] = true;
                for (int voisin = 0; voisin < nbSommets; voisin++) {
                    if (adj[sommet][voisin] == 1 && !visite[voisin]) {
                        pile.push(voisin);
                        tripletsParcourusUniques.add(new Triplet(sommet, voisin, poidsA[sommet][voisin]));
                    }
                }
            }
            }
            return tripletsParcourusUniques;
        }
        private ArrayList<Integer> sommetDFS(ArrayList<Triplet> T){
             ArrayList<Integer> l = new ArrayList<>();
             for (int i=0;i<T.size();i++){
                int s1=T.get(i).getS1();
                int s2=T.get(i).getS2();
                if (!l.contains(s1)){
                    l.add(s1);
                }
                if (!l.contains(s2)){
                    l.add(s2);
                }
             }
             return l;
    
        }
    ////////////////////////////////FCT INTERMEDIAIRE 
        
    public Graphe KruskalInv(){
        Graphe CopieI=this.clone();
        List<Triplet> LA= CopieI.aretesTriees(false);
        int[] derniereAreteEnleve = null;

        int[] tmp;
        double poidsArajouterFin=0.0;
        while(CopieI.estConnexe()){
            tmp=LA.get(0).getSommets();
            CopieI.retirerArrete(tmp[0], tmp[1]);
            poidsArajouterFin=LA.get(0).getPoids();
            LA.remove(0);
            derniereAreteEnleve=tmp;
        }
        if (derniereAreteEnleve != null) {
            CopieI.ajouteArrete(derniereAreteEnleve[0], derniereAreteEnleve[1],poidsArajouterFin);
        }

        return CopieI;
    }
    public boolean estConnexe(){
        boolean[] visite = new boolean[nbSommets];
        dfs(0, visite); // On commence le DFS à partir du sommet 0 ici par defaut, apres il faut le mettre a start
        for (boolean v : visite) {
            if (!v) {
                return false; // Si un sommet n'est pas atteint, le graphe n'est pas connexe
            }
        }
        return true;
    }


    private ArrayList<Triplet> cheminFin(ArrayList<Integer> l,ArrayList<Triplet> La){
        ArrayList<Triplet> s=new ArrayList<>();
        for(int i=0;i<l.size()-1;i++){
            for(int j=0;j<La.size();j++){
                if ((l.get(i)==La.get(j).getS1()&&l.get(i+1)==La.get(j).getS2())){
                    if (!s.contains(La.get(j))){
                        s.add(La.get(j));
                    }
                }
            }
        }
        return s;
    }
    ////////////////////////////V.2.2 TSP
    public Graphe tsp2(){
        ArrayList<Triplet> origi_sommets=this.aretesTriees(false);
        afficherAretesTriees(origi_sommets);
        Graphe g=this.KruskalInv();
        boolean[] visite = new boolean[nbSommets];
        ArrayList<Triplet> La=g.dfsUnique(0, visite);
        ArrayList<Integer> l= g.sommetDFS(La);
        System.out.println("\n chemin fin : \n");
        afficherAretesTriees(cheminFin(l,origi_sommets));
        Graphe g2=new Graphe(nbSommets, La);
        return g2;
    }



    /*/////////////////////////MAIN POUR TEST UNITAIRE A RECOMMENTER/////////////////////////////////////////7 */


        public class Main {
            public static void main(String[] args) {

                // Création d'un graphe avec 5 sommets
                Graphe graphe1 = new Graphe(4);
                System.out.println("Graphe 1 :");
                System.out.println(graphe1);
                System.out.println("--------------------------------------------");

                Graphe graphe2=graphe1.KruskalInv();
                System.out.println("Graphe 2 :");
                System.out.println(graphe2);
                System.out.println("--------------------------------------------");
                // Création d'un graphe avec des poids spécifiques et des noms de sommets
                Double[][] poids = {{0.0,4.0,2.0,4.0}, {4.0, 0.0, 5.0,3.0}, {2.0, 5.0, 0.0,2.0}, {4.0, 3.0, 2.0,0.0}};
                ArrayList<Integer> nomsSommets = new ArrayList<>(Arrays.asList(0, 1, 2,3));
                Graphe graphe3 = new Graphe(poids, nomsSommets);
                System.out.println("Graphe 3 :");
                System.out.println(graphe3);

                Graphe graphe4=graphe3.KruskalInv();
                System.out.println("Graphe 4 dfs ");
                //System.out.println(graphe4);
                boolean[] visite = new boolean[graphe4.nbSommets];
                //graphe4.afficherAretesTriees(graphe4.dfs(0, visite));
                graphe3.tsp2();
/* 
                // Clonage du graphe 2
                Graphe cloneGraphe2 = (Graphe) graphe2.clone();
                System.out.println("Clone du Graphe 2 :");
                System.out.println(cloneGraphe2);

            

                // Ajout d'une arête au graphe 1
                graphe1.ajouteArrete(0, 4,8);
                graphe1.ajouteArrete(0, 3,5);
                graphe1.ajouteArrete(1, 2,3);
                graphe1.ajouteArrete(2, 4,2);
                graphe1.ajouteArrete(1, 4,3);
                graphe1.ajouteArrete(4, 3,6);


                System.out.println("Graphe 1 après :");
                System.out.println(graphe1);

                // Test de la méthode tsp2
               // Graphe graphe3 = graphe1.tsp2();
                //System.out.println("Graphe 3 après application de tsp2 :");
                //System.out.println(graphe3);
                */

            }
        }

}