import java.io.FileNotFoundException;
import java.io.FileReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Bullets {
	int bulletID,barrageID,buffID;
	int bulletCount;
	double light,medium,heavy;
	String ammoType;
	int offsetX;
	int offsetZ;
	int splash;
	int pierce;
	int range;
	int velocity;
	JSONObject bulletStats;
	int antisub;
	boolean ignoreShield;
	boolean shrapnel = false;
	
	public Bullets(int bulletid) {
		bulletID = bulletid;
		bulletCount = 0;
		importFiles();
		getBulletStats();
	}
	
	public void print() {
		System.out.println( "BulletID: "+ bulletID);
		System.out.println( "Bullet Count: " + bulletCount);
		System.out.println( "Ammo Type : " + ammoType);
		System.out.println( "Ammo Mods: " + light+ "/" +medium+ "/" + heavy);
		if(offsetX != 0)
			System.out.println("SpreadX: " + offsetX);
		if(offsetZ !=0) 
			System.out.println("SpreadZ: " + offsetZ);
		if(splash != 0)
			System.out.println("Splash: "+ splash);
		System.out.println("Pierce: " + pierce);
		System.out.println("Velocity: " + velocity);
		if(antisub !=0) {
			System.out.println("antisub: " + antisub);
		}
	}
	
	private void importFiles() {
		try {
			bulletStats = new JSONObject(new JSONTokener(new FileReader(System.getProperty("user.dir")+"\\src\\bullet_template.json")));
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void getBulletStats() {
		JSONObject bullet = bulletStats.getJSONObject(bulletID+"");
		getAmmoType(bullet.getInt("ammo_type"));
		checkBuff(bullet.getJSONArray("attach_buff"));
		antisub = bullet.getInt("antisub_enhancement");
		getAmmoMods(bullet.getJSONArray("damage_type"));
		getExtras(bullet.optJSONObject("extra_param"));
		getSplash(bullet.optJSONObject("hit_type"));
		pierce = bullet.getInt("pierce_count");
		range = bullet.getInt("range");
		velocity = bullet.getInt("velocity");
	}
	
	private void getSplash(JSONObject hitType) {
		if(hitType != null) {
			if(hitType.has("range"))
				splash = hitType.getInt("range");
			else
				splash = 0;
		}else {
			splash = 0;
		}
	}
	
	/**
	 * Checks extra_param for stats
	 * randomOffsetX/Z, ignoreShield
	 * @param extra
	 */
	private void getExtras(JSONObject extra) {
		if(extra != null) {
			offsetX = extra.optInt("randomOffsetX",0);
			offsetZ = extra.optInt("randomOffsetZ",0);
			ignoreShield = extra.optBoolean("ignoreShield", false);
			JSONObject shrapnelCheck = extra.optJSONObject("shrapnel");
			if(shrapnelCheck != null)
				shrapnel=true;
		}
	}
	
	private void getAmmoMods(JSONArray mods) {
		light = mods.getDouble(0);
		medium = mods.getDouble(1);
		heavy = mods.getDouble(2);
	}
	
	private void checkBuff(JSONArray buff) {
		if(!buff.isEmpty()) {
			buffID = buff.getJSONObject(0).getInt("buff_id");
		}else
			buffID = 0;
	}
	
	
	private void getAmmoType(int ammo) {
		switch(ammo) {
		case 1:
			ammoType = "Normal";
			break;
		case 2:
			ammoType = "AP";
			break;
		case 3:
			ammoType = "HE";
			break;
		case 4:
			ammoType = "Torpedo";
			break;
		case 5:
			ammoType = "AA";
			break;
		case 6:
			ammoType = "Bomb";
			break;
		case 7:
			ammoType = "SAP";
			break;
		default:
			ammoType = "Unknown";
		}
	}
	
	public int getBulletID() {
		return bulletID;
	}
	public int getBarrageID() {
		return barrageID;
	}
	public int getBulletCount() {
		return bulletCount;
	}
	public double getLight() {
		return light;
	}
	public double getMedium() {
		return medium;
	}
	public double getHeavy() {
		return heavy;
	}
	
	public void setLight(int l) {
		light = l;
	}
	public void setMedium(int m) {
		medium = m;
	}
	public void setHeavy(int h) {
		heavy = h;
	}
	public void addBullets(int count) {
		bulletCount += count;
	}
	public void setOffsetX(int x) {
		offsetX = x;
	}
	public void setOffsetZ(int z) {
		offsetZ = z;
	}
	public void setSplash(int s ) {
		splash = s;
	}
	public void setPierce(int p) {
		pierce = p;
	}
	public void setVelocity(int velo) {
		velocity = velo;
	}
	public void setRange(int r) {
		range = r;
	}
	public void setBuffID(int buff) {
		buffID = buff;
	}
}


	