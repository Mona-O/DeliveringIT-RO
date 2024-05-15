package l3m.cyber.planner.services;

import l3m.cyber.planner.utils.Planner;
import l3m.cyber.planner.requests.PlannerParameter;
import l3m.cyber.planner.responses.PlannerResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlannerService {

    public PlannerResult getResult(PlannerParameter params){
        Planner pl= new Planner(params);
        for (int i = 0; i < params.matrix().length; i++) {
            // Parcourir les colonnes de chaque ligne
            for (int j = 0; j < params.matrix()[i].length; j++) {
                // Afficher l'élément de la matrice à la position (i, j)
                System.out.print(params.matrix()[i][j] + " ");
            }
        }
        System.out.println(params.matrix().length);
        pl.divise();
        pl.calculeTournees();
        pl.calculeLongTournees();
        return pl.result();
    }





}
