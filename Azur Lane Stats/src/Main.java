import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ShipStats s = new ShipStats();
		//String id = s.getShipID("Repulse.META", true);
		LinkedList<String> weaponList;
		String id = "9704034";
		if(id != null) {
		s.getShipStats(id);
		s.printStats(120, 100);
		id = s.getID();
		ShipSkills skills = new ShipSkills(id);
		weaponList = skills.getWeaponList();
		while(!weaponList.isEmpty()) {
			new Weapons(weaponList.pop());
		}
		
		}else {
			System.out.println("Check name or files");
		}
	}

}
