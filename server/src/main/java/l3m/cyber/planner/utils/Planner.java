package l3m.cyber.planner.utils;

import l3m.cyber.planner.requests.PlannerParameter;
import l3m.cyber.planner.responses.PlannerResult;

import java.util.ArrayList;


public class Planner{

    public Planner(PlannerParameter param){
        this(param.matrix(), param.k(), param.start());
    }

    public Planner(Double[][] distances, int k, int debut){
        // TODO : a completer
    }

    public Planner(){
        this(null, 0, 0);
    }


    
    public PlannerResult result(){
        
        //return new PlanningResult(tournees, longTournees);
        return new PlannerResult(new ArrayList<ArrayList<Integer>>(), new ArrayList<Double>()); // TODO: remplacer par la ligne precedente

    }


    
}