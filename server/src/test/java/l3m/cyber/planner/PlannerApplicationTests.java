package l3m.cyber.planner;

import l3m.cyber.planner.requests.PlannerParameter;
import l3m.cyber.planner.responses.PlannerResult;
import l3m.cyber.planner.utils.PartitionAlea;
import l3m.cyber.planner.utils.PartitionKCentres;
import l3m.cyber.planner.utils.Planner;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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
				{0.0, 1.227, 0.640, 0.441, 0.416, 0.635, 0.806, 0.758, 0.258, 0.569},
				{1.227, 0.0, 1.358, 1.022, 1.370, 1.235, 0.459, 1.031, 1.352, 1.104},
				{0.640, 1.358, 0.0, 0.502, 0.785, 0.208, 0.535, 0.650, 0.760, 0.398},
				{0.441, 1.022, 0.502, 0.0, 0.657, 0.209, 0.599, 0.857, 0.195, 0.114},
				{0.416, 1.370, 0.785, 0.657, 0.0, 0.287, 0.606, 0.287, 0.096, 0.388},
				{0.635, 1.235, 0.208, 0.209, 0.287, 0.0, 0.507, 0.877, 0.331, 0.127},
				{0.806, 0.459, 0.535, 0.599, 0.606, 0.507, 0.0, 0.474, 0.466, 0.882},
				{0.758, 1.031, 0.650, 0.857, 0.287, 0.877, 0.474, 0.0, 0.339, 0.779},
				{0.258, 1.352, 0.760, 0.195, 0.096, 0.331, 0.466, 0.339, 0.0, 0.343},
				{0.569, 1.104, 0.398, 0.114, 0.388, 0.127, 0.882, 0.779, 0.343, 0.0}
		};
		int k = 2;

		// Création d'une instance de PartitionKCentre
		PartitionKCentres partitionKCentre = new PartitionKCentres(distances.length,k);
		System.out.println(partitionKCentre.toString());
		partitionKCentre.partitionne(distances);
		System.out.println(partitionKCentre.toString());


	}

	
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
	
}