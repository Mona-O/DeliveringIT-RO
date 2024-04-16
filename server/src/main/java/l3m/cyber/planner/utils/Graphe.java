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
        int[][] adj=new int[n][n]; //matrice nxn d'adjacence avec aucun arete
        
        this.adj=adj;
        this.nbSommets=n;
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
    public Object clone() {
            Graphe clone = new Graphe(this.nbSommets);
            
    
            
            clone.adj = new int[this.adj.length][];
            for (int i = 0; i < this.adj.length; i++) {
                clone.adj[i] = Arrays.copyOf(this.adj[i], this.adj[i].length);
            }
            
            
            if (this.poidsA != null) {
                clone.poidsA = new Double[this.poidsA.length][];
                for (int i = 0; i < this.poidsA.length; i++) {
                    clone.poidsA[i] = Arrays.copyOf(this.poidsA[i], this.poidsA[i].length);
                }
            }
            
   
            clone.nomSommets = new ArrayList<>(this.nomSommets);

            return clone;
}

    /**SPEC
     * @param: none
     * @return: change la matrice de poids ponderes de null vers une matrice remplie de 1 pour toutes les aretes par defaut
     */
    public void pondereAretes(){
        Double[][] tmp= new Double[this.nbSommets][this.nbSommets];
        for (int i = 0; i < this.nbSommets; i++) {
            for(int j=0;j<this.nbSommets;j++){
                tmp[i][j]=(double) 1;
            }
        }
        this.poidsA=tmp;
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
                if (adj[i][j]==1){
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

    

    public Graphe KruskalInv(){
        Graphe CopieI=(Graphe) this.clone();
        List<Triplet> LA= CopieI.aretesTriees(false);
        int[] tmp;
        while(CopieI.estConnexe()){
            tmp=LA.get(0).getSommets();
            CopieI.retirerArrete(tmp[0], tmp[1]);
            LA.remove(0);
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

    private void dfs(int u, boolean[] visite) {
        Stack<Integer> pile = new Stack<>();
        pile.push(u);
        while (!pile.isEmpty()) {
            int sommet = pile.pop();
            visite[sommet] = true;
            for (int voisin = 0; voisin < nbSommets; voisin++) {
                if (adj[sommet][voisin] == 1 && !visite[voisin]) {
                    pile.push(voisin);
                }
            }
        }
    }
    private List<Triplet> doubleAretes(){
        List<Triplet> La=this.listeAretes();
        for (int i=0;i<nbSommets;i++){
            La.add(La.get(i));
        }
        return La;
    }
    private Graphe toCycleHam(){
        List<Triplet> l=this.listeAretes();
        List<Triplet> L_fin= new ArrayList<>();
        for (int i=0;i<l.size();i++){
            if (!L_fin.contains(l.get(i))){
                L_fin.add(l.get(i));
            }
        }
        Graphe g= new Graphe(nbSommets, L_fin);
        return g;
    }

    public Graphe tsp2(){
        Graphe g=this.KruskalInv();
       
        List<Triplet> La=this.doubleAretes();
        Graphe g2=new Graphe(nbSommets, La);
        Graphe g3=g2.toCycleHam();
        return g3;
    }


    /*MAIN POUR TEST A RECOMMENTER */

    
        public class Main {
            public static void main(String[] args) {
                
                // Création d'un graphe avec 5 sommets
                Graphe graphe1 = new Graphe(5);
                System.out.println("Graphe 1 :");
                System.out.println(graphe1);

                // Création d'un graphe avec des poids spécifiques et des noms de sommets
                Double[][] poids = {{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}};
                ArrayList<Integer> nomsSommets = new ArrayList<>(Arrays.asList(0, 1, 2));
                Graphe graphe2 = new Graphe(poids, nomsSommets);
                System.out.println("Graphe 2 :");
                System.out.println(graphe2);

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
                Graphe graphe3 = graphe1.tsp2();
                System.out.println("Graphe 3 après application de tsp2 :");
                System.out.println(graphe3);
                
                
            }
        }
    
}