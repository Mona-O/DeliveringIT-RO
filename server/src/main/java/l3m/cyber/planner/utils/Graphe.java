package l3m.cyber.planner.utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;


public class Graphe{
    private int nbSommets;
    private int[][] adj; //matrice d'adjacence remplie de 0 t 1, symmétrique
    private Double[][] poidsA ;//matrice des poids des arêtes , pourra etre null si pas de poids
    private ArrayList<Integer> nomSommets; // au debut on peut utilise nb 0 a nbS-1 
     
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
    /**SPEC CONSTRUCTEUR
     * @param: n:int le nombre de sommets
     * @return graphe a n sommets, avec les noms des sommets=0 à n-1 
     */
    public Graphe(int n){
       // int[][] adj=new int[n][n]; //matrice nxn d'adjacence avec aucun arete
        
        
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
    public Graphe clone() {
        Graphe clone = new Graphe(this.nbSommets);
    
        clone.poidsA=this.poidsA.clone();
        clone.adj=this.adj.clone();
    
        // Copier les noms des sommets
        clone.nomSommets = new ArrayList<>(this.nomSommets);
    
        return clone;
    }
    

    /**SPEC
     * @param: none
     * @return: change la matrice de poids ponderes de null vers une matrice remplie de 1 pour toutes les aretes par defaut*/
     
    public void pondereAretes(){
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

    public void pondereAdj(){
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
    public ArrayList<Integer> tsp1(int n){
       return Auxiliaire.integerList(n);
    }

    public List<Triplet> listeAretes(){
        List<Triplet> LA= new ArrayList<>();
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


    public List<Triplet> aretesTriees(boolean croissant){
        List<Triplet> L_tmp=this.listeAretes();
        Collections.sort(L_tmp);
    
        if (!croissant){
            Collections.reverse(L_tmp);
        }
        return L_tmp;
    }
    public void afficherAretesTriees(List<Triplet> aretesTriees) {
        System.out.println("Liste des arêtes triées :");
        for (Triplet triplet : aretesTriees) {
            int[] sommets = triplet.getSommets();
            double poids = triplet.getPoids();
            System.out.println("(" + sommets[0] + ", " + sommets[1] + ") : poids = " + poids);
        }
    }
    
    

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
    

    private List<Triplet> dfsUnique(int u, boolean[] visite) {
        List<Triplet> tripletsParcourusUniques = new ArrayList<>();
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

    public Graphe tsp2(){
        Graphe g=this.KruskalInv();
        boolean[] visite = new boolean[nbSommets];
        List<Triplet> La=g.dfsUnique(0, visite);
        Graphe g2=new Graphe(nbSommets, La);
        return g2;
    }


    /*MAIN POUR TEST A RECOMMENTER */

    
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
                Double[][] poids = {{0.0,4.0,2.0,4.0}, {4.0, 0.0, 5.0,3.0}, {2.0, 5.0, 0.0,0.0}, {4.0, 3.0, 0.0,0.0}};
                ArrayList<Integer> nomsSommets = new ArrayList<>(Arrays.asList(0, 1, 2,3));
                Graphe graphe3 = new Graphe(poids, nomsSommets);
                System.out.println("Graphe 3 :");
                System.out.println(graphe3);
                
                Graphe graphe4=graphe3.KruskalInv();
                System.out.println("Graphe 4 :");
                System.out.println(graphe4);
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