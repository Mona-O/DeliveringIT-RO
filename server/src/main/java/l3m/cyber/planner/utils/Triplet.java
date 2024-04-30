package l3m.cyber.planner.utils;

public class Triplet implements Comparable<Triplet> {
    private int s1;
    private int s2;
    private double p;

    public Triplet(int s1, int s2, double p){
        this.s1=s1;
        this.s2=s2;
        this.p=p;
    }
    public double getPoids(){
        return this.p;
    }
    public int getS1(){
        return s1;
    }
    public int getS2(){
        return s2;
    }
    public int[] getSommets(){
        int[] l=new int[2];
        l[0]=s1;
        l[1]=s2;
        return l;
    }
    @Override
    public int compareTo(Triplet tr2) {
        double p = tr2.getPoids();
         return p>this.p ?  (-1) : p<this.p ? 1 :0; //si poids de tr2>poids alors -1, sinon 1 si poids de tr2< poid; si egaux alors 0
    }
    
}
