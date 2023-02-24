import java.util.LinkedList;
import org.json.JSONObject;

public class Planes{
	LinkedList<String> weaponIDs;
	int planeCount;
	JSONObject weaponStats = JsonData.weaponStats;
	JSONObject planeStats = JsonData.planeStats;;
	JSONObject barrageStats = JsonData.barrageStats;;
	String dir = System.getProperty("user.dir");
	LinkedList<String> AAGuns = new LinkedList<String>();
	LinkedList<Weapons> weaponList = new LinkedList<Weapons>();
	
	public Planes(LinkedList<String> id, int planes) {
		weaponIDs = id;
		planeCount = planes;
		getWeapons();
		printWeapons();
	}
	
	private void getWeapons(){
		for(String w: weaponIDs) {
			if(isAAGun(w)) {
				continue;
			}else
			weaponList.add(new Weapons(w,true));
		}
	}
	
	private boolean isAAGun(String id) {
		JSONObject base = weaponStats.getJSONObject(weaponStats.getJSONObject(id).getInt("base")+"");
		int type =  base.getInt("type");
		if(type == 4) {
			AAGuns.add(base.getString("name"));
			return true;
		}
		else
			return false;
	}
	
	private void printWeapons(){
		System.out.println("----------------------------------------------------");
		System.out.print("AA guns: ");
		if(AAGuns.isEmpty()) {
			System.out.println("None");
		}else {
			for(String AA: AAGuns) {
				System.out.print(AA + ", ");
			}
			System.out.println("");
		}
		System.out.println("Total Planes: " + planeCount);
		for(Weapons w: weaponList) {
			w.printWeaponBulletMultiplier(planeCount, true);
		}

	}
}
