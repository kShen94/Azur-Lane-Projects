import org.json.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShipData {
	JSONObject shipStats,weaponStats,planeStats,barrageStats,bulletStats,shipTemplate; 
	String dir = System.getProperty("user.dir");
	boolean checkRetro = false;
	public ShipData() {
		importFiles();
	}

	/**
	 * imports json files to jsonobjects
	 */
	public void importFiles() {
		try {
			shipStats = new JSONObject(new JSONTokener(new FileReader(dir+"\\src\\ship_data_statistics.json")));
			shipTemplate = new JSONObject(new JSONTokener(new FileReader(dir+"\\src\\ship_data_template.json")));
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Search shipStats json for names that contains shipName. Outputs all matches in a list
	 * @param shipName - uses contains, can use part of a name
	 * @return List of JSONObjects that contain shipName in name
	 */
	public List<JSONObject> getShip(String shipName) {
		Iterator<String> keys = shipStats.keys();
		List<JSONObject> ships = new ArrayList<JSONObject>();
		while (keys.hasNext()) {
			String key = keys.next();
			//checks keys only ending with 4
			if(key.endsWith("4") && !key.startsWith("9")) {
				if(shipStats.getJSONObject(key).getString("name").toLowerCase().contains(shipName.toLowerCase()))
					ships.add(shipStats.getJSONObject(key));
			}
		}
		return ships;
	}

	/**
	 * Search shipStats for names that contains shipName. Returns key if matches
	 * @param shipName
	 * @return key/id of ship
	 */
	public String getShipID(String shipName,boolean retro) {
		if(retro)
			checkRetro = true;
		Iterator<String> keys = shipStats.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			if(key.endsWith("4") && !key.startsWith("9")) {
				if(shipStats.getJSONObject(key).getString("english_name").toLowerCase().contains(shipName.toLowerCase()))
					return (shipTemplate.getJSONObject(key).getInt("group_type")+ "4");
			}
		}
		return null;
	}
	
	
	public JSONObject getShipByID (String id) {
		return shipStats.getJSONObject(id);
	}
	
}

