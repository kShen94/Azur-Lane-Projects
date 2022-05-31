import java.util.HashMap;
import java.util.TreeMap;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ShipStats s = new ShipStats();
		String id = s.getShipID("vanguard", true);
		//String id = "203104";
		if(id != null) {
		s.getShipStats(id);
		s.printStats(120, 100);
		id = s.getID();
		ShipSkills skills = new ShipSkills(id);
		}else {
			System.out.println("Check name or files");
		}
		Weapons w = new Weapons("69590");
	}

}
