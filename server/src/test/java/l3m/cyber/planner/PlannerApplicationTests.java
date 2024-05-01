package l3m.cyber.planner;

import l3m.cyber.planner.requests.PlannerParameter;
import l3m.cyber.planner.responses.PlannerResult;
import l3m.cyber.planner.utils.PartitionAlea;
import l3m.cyber.planner.utils.PartitionKCentres;
import l3m.cyber.planner.utils.Planner;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class PlannerApplicationTests {

	// ci-dessous : test unitaire idiot pour demonstration
	@Test
	void dummyTest(){
		System.out.println("*********************************");
		System.out.println("Hello world\n");
		assertTrue(1!=2);
		System.out.println("*********************************");
		
	}

	@Test
	void partitionTest(){
		Double[][] matrix = {{0.0,1.1},{1.1,0.0}};
		int k = 2;
		PartitionAlea partitionAlea = new PartitionAlea(10, k);
		for (int i = 0 ; i < k ; i++){
			assertNotNull(partitionAlea.getPartie(i));
			assertEquals(0,partitionAlea.getPartie(i).getFirst());
		}
		System.out.println(partitionAlea.toString());
		partitionAlea.partitionne(matrix);
		System.out.println(partitionAlea.toString());
		for (int i = 0 ; i < k ; i++){
			assertNotNull(partitionAlea.getPartie(i));
			assertEquals(0,partitionAlea.getPartie(i).getFirst());

		}
	}

	@Test
	public void testPartitionKCentre() {
		// Configuration du test
		Double[][] distances = {
				{0.0, 0.8, 1.5, 1.2, 1.3, 0.9, 1.4, 1.1, 1.0, 1.6},
				{0.8, 0.0, 1.4, 1.1, 1.2, 0.8, 1.3, 1.0, 0.9, 1.5},
				{1.5, 1.4, 0.0, 1.7, 2.2, 1.6, 2.1, 1.8, 1.9, 0.5},
				{1.2, 1.1, 1.7, 0.0, 1.4, 1.1, 1.3, 1.2, 1.0, 1.8},
				{1.3, 1.2, 2.2, 1.4, 0.0, 1.3, 2.1, 1.5, 1.4, 0.4},
				{0.9, 0.8, 1.6, 1.1, 1.3, 0.0, 1.4, 1.1, 1.0, 1.7},
				{1.4, 1.3, 2.1, 1.3, 2.1, 1.4, 0.0, 1.4, 1.3, 0.3},
				{1.1, 1.0, 1.8, 1.2, 1.5, 1.1, 1.4, 0.0, 1.1, 1.9},
				{1.0, 0.9, 1.9, 1.0, 1.4, 1.0, 1.3, 1.1, 0.0, 2.0},
				{1.6, 1.5, 0.5, 1.8, 0.4, 1.7, 0.3, 1.9, 2.0, 0.0}
		};
		int k = 2;

		// Création d'une instance de PartitionKCentre
		PartitionKCentres partitionKCentre = new PartitionKCentres(distances.length,k);
		System.out.println(partitionKCentre.toString());
		partitionKCentre.partitionne(distances);
		System.out.println(partitionKCentre.toString());


	}

	private ArrayList<ArrayList<Integer>> partitionResult;

	@Test
	public void testPartitionsWithoutDuplicates() {
		// Définir les données de test
		Double[][] distances = {
				{ 0.0 , 2.5, 3.0  , 0.1   , 17.0 , 15.5, 8.2 },
				{ 2.5 , 0.0  , 1.0  , 5.25, 18.0 , 3.5 , 12.0},
				{ 3.0  , 1.0  , 0.0  , 0.0  , 3.4, 9.9 , 14.0  },
				{ 0.1   , 5.25, 0.0 , 0.0   , 7.7, 8.8 , 6.8 },
				{ 17.0  , 18.0 , 3.4, 7.7 , 0.0  , 2.0   , 2.2 },
				{ 15.5, 3.5, 9.9, 8.8 , 2.0  , 0.0   , 3.3 },
				{ 8.2 , 12.0 , 14.0 , 6.8 , 2.2, 3.3 , 0.0   }
		};
		int k = 2; // Nombre de partitions souhaitées
		int debut = 0; // Point de départ des tournées

		// Créer un objet PlannerParameter avec les données de test
		PlannerParameter param = new PlannerParameter(distances, k, debut);

		// Créer un objet Planner avec les paramètres
		Planner planner = new Planner(param);

		// Exécuter la méthode divise pour obtenir les partitions
		planner.divise();

		// Obtenir le résultat des partitions
		PlannerResult result = planner.result();

		// Vérifier que le nombre de partitions est égal à k
		assertEquals(k, result.tournees().size());

		// Afficher les tournées générées dans la console
		System.out.println("Tournées générées :");
		for (ArrayList<Integer> tournee : result.tournees()) {
			System.out.println(tournee);
		}
	}

	// Méthode pour récupérer les tournées créées
	public ArrayList<ArrayList<Integer>> getPartitionResult() {
		return partitionResult;
	}




	/*
	@Test
	void nonnullTestPlanning(){
		Double[][] matrix = {{0.0,1.1},{1.1,0.0}};
		int k=1;
		int start=0;
		PlannerParameter param= new PlannerParameter(matrix, k, start);
		Planner pl= new Planner(param);
        PlannerResult pr=pl.result();
		assertTrue(pr.tournees() !=null); //le tableau tournees doit etre non null
		assertTrue(pr.longTournees() != null); // idem, le tableau longTournees doit etre non null
	}


	 */
}