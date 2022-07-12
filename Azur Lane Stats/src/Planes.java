import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Planes{
	LinkedList<String> weaponIDs;
	int planeCount;
	JSONObject weaponStats;
	JSONObject planeStats;
	JSONObject barrageStats;
	String dir = System.getProperty("user.dir");
	LinkedList<String> AAGuns = new LinkedList<String>();
	LinkedList<Weapons> weaponList = new LinkedList<Weapons>();
	
	public Planes(LinkedList<String> id, int planes) {
		weaponIDs = id;
		planeCount = planes;
		importFiles();
		getWeapons();
		printWeapons();
	}
	

	
	private void importFiles() {
		try {
			weaponStats = new JSONObject(new JSONTokener(new FileReader(dir+"\\src\\weapon_property.json")));
			planeStats = new JSONObject(new JSONTokener(new FileReader(dir+"\\src\\aircraft_template.json")));
			barrageStats = new JSONObject(new JSONTokener(new FileReader(dir+"\\src\\barrage_template.json")));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
			w.printWeaponBulletMultiplier(planeCount);
		}
	}
}
