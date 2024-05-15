package l3m.cyber.planner;

import l3m.cyber.planner.requests.PlannerParameter;
import l3m.cyber.planner.responses.PlannerResult;
import l3m.cyber.planner.utils.PartitionAlea;
import l3m.cyber.planner.utils.PartitionKCentres;
import l3m.cyber.planner.utils.Planner;
import l3m.cyber.planner.utils.RandomPartition;
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
				{0.0, 20.0, 25.0, 30.0, 80.0, 75.0, 50.0},
				{20.0, 0.0, 35.0, 50.0, 90.0, 45.0, 70.0},
				{25.0, 35.0, 0.0, 40.0, 35.0, 85.0, 95.0},
				{30.0, 50.0, 40.0, 0.0, 70.0, 80.0, 60.0},
				{80.0, 90.0, 35.0, 70.0, 0.0, 55.0, 45.0},
				{75.0, 45.0, 85.0, 80.0, 55.0, 0.0, 65.0},
				{50.0, 70.0, 95.0, 60.0, 45.0, 65.0, 0.0}
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
				{ 3.0  , 1.0  , 0.0  , 0.7  , 3.4, 9.9 , 14.0  },
				{ 0.1   , 5.25, 0.7 , 0.0   , 7.7, 8.8 , 6.8 },
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

	@Test
	public void testRandomPartition() {
		Double[][] distances = {
				{0.0, 20.0, 25.0, 30.0, 80.0, 75.0, 50.0},
				{20.0, 0.0, 35.0, 50.0, 90.0, 45.0, 70.0},
				{25.0, 35.0, 0.0, 40.0, 35.0, 85.0, 95.0},
				{30.0, 50.0, 40.0, 0.0, 70.0, 80.0, 60.0},
				{80.0, 90.0, 35.0, 70.0, 0.0, 55.0, 45.0},
				{75.0, 45.0, 85.0, 80.0, 55.0, 0.0, 65.0},
				{50.0, 70.0, 95.0, 60.0, 45.0, 65.0, 0.0}
		};
		int k = 3;


		RandomPartition partition = new RandomPartition(distances.length,k, 0.1);
		System.out.println(partition.toString());
		partition.partitionne(distances);
		System.out.println(partition.toString());
	}



	@Test
	void nonnullTestPlanning(){
		Double[][] matrix = {
				{0.0, 20.0, 25.0, 30.0, 80.0, 75.0, 50.0},
				{20.0, 0.0, 35.0, 50.0, 90.0, 45.0, 70.0},
				{25.0, 35.0, 0.0, 40.0, 35.0, 85.0, 95.0},
				{30.0, 50.0, 40.0, 0.0, 70.0, 80.0, 60.0},
				{80.0, 90.0, 35.0, 70.0, 0.0, 55.0, 45.0},
				{75.0, 45.0, 85.0, 80.0, 55.0, 0.0, 65.0},
				{50.0, 70.0, 95.0, 60.0, 45.0, 65.0, 0.0}};
		int k=2;
		int start=0;
		PlannerParameter param= new PlannerParameter(matrix, k, start);
		Planner pl= new Planner(param);
		pl.divise();
		pl.calculeTournees();
		pl.calculeLongTournees();
        PlannerResult pr=pl.result();
		System.out.println(pr.toString());
	}



}