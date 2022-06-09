import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Weapons {
	String weapon_id;
	JSONObject weapon;
	List<Integer> bullet_id = new LinkedList<>();
	List<Integer> barrage_id = new LinkedList<>();
	List<Integer> bulletCount = new LinkedList<>();
	JSONObject weaponStats;
	JSONObject planeStats;
	JSONObject barrageStats;
	JSONArray barrageArray;
	JSONArray bulletArray;
	String dir = System.getProperty("user.dir");
	int damage;
	int coeff;
	int attr;
	int attrRatio;
	String scaling;
	JSONObject baseWeapon;
	HashMap<Integer,Bullets> map = new HashMap();
	Stack<Integer> mapValues = new Stack();
	
	public Weapons(String id) {
		weapon_id = id;
		importFiles();
		checkWeapon();
		printWeapon();
	}
	// planes = type 10/11?
	
	private void importFiles() {
		try {
			weaponStats = new JSONObject(new JSONTokener(new FileReader(dir+"\\src\\weapon_property.json")));
			planeStats = new JSONObject(new JSONTokener(new FileReader(dir+"\\src\\aircraft_template.json")));
			barrageStats = new JSONObject(new JSONTokener(new FileReader(dir+"\\src\\barrage_template.json")));
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void checkWeapon() {
		weapon = weaponStats.getJSONObject(weapon_id);
		if(weapon.has("base"))
			baseWeapon = weaponStats.getJSONObject(weapon.getInt("base")+"");
		else
			baseWeapon = weapon;
		if(baseWeapon.getInt("type")== 10 || baseWeapon.getInt("type")== 11 ) {
			getPlane();
		}else
			getGun();
	}
	
	private void getPlane() {
		
	}
	
	private void getGun() {
		damage = weapon.getInt("damage");
		coeff = baseWeapon.getInt("corrected");
		attr = baseWeapon.getInt("attack_attribute");
		attrRatio = baseWeapon.getInt("attack_attribute_ratio");
		getScaling(attr);
		if(weapon.has("bullet_ID")) 
			bulletArray = weapon.getJSONArray("bullet_ID");
		if(weapon.has("barrage_ID"))
			barrageArray = weapon.getJSONArray("barrage_ID");
		
		if(bulletArray == null) 
			bulletArray = baseWeapon.getJSONArray("bullet_ID");
		if(barrageArray == null) 
			barrageArray = baseWeapon.getJSONArray("barrage_ID");
		
		getBullets();
	}
	
	private void getScaling(int attr) {
		switch (attr) {
		case(1):
			scaling = "FP";
			break;
		case(2):
			scaling = "TRP";
			break;
		case(3):
			scaling = "AA";
			break;
		case(4):
			scaling = "AVI";
			break;
		case(5):
			scaling = "ASW";
			break;
		}
	}
	private void getBullets() {
		int b;
		for(int i = 0; i < bulletArray.length();i++) {
			b = bulletArray.getInt(i);
			if(map.containsKey(b)) {
				addBullet(b,i);
			}else {
				mapValues.push(b);
				map.put(b, createBullet(b,i));
			}
		}
		combineBullets();
	}
	private void combineBullets() {
		Bullets a,b;
		int t;
		Stack<Integer> temp = new Stack();
		Stack<Integer> values = mapValues;
		Stack<Integer> result = new Stack();
		a = map.get(values.pop());
		result.push(a.getBulletID());
		while(!values.isEmpty()) {
			t=values.pop();
			b = map.get(t);
			if(a.getLight()==b.getLight() &&  a.getMedium()==b.getMedium() && a.getHeavy()==b.getHeavy())
				a.addBullets(b.getBulletCount());
			else {
				temp.push(t);
			}
			if(values.isEmpty() && !temp.isEmpty()) {
				values = temp;
				temp.clear();
				a = map.get(values.pop());
				result.push(a.getBulletID());
			}
		}
		mapValues = result;
	}
	private Bullets createBullet(int b,int i) {
		Bullets bullet = new Bullets(b);
		JSONObject barrage = barrageStats.getJSONObject(barrageArray.getInt(i)+"");
		int primal = barrage.getInt("primal_repeat")+1;
		int senior = barrage.getInt("senior_repeat")+1;
		bullet.addBullets(primal*senior);
		return bullet;
	}
	private void addBullet(int b, int i) {
		JSONObject barrage = barrageStats.getJSONObject(barrageArray.getInt(i)+"");
		int primal = barrage.getInt("primal_repeat")+1;
		int senior = barrage.getInt("senior_repeat")+1;
		Bullets bullet = map.get(b);
		bullet.addBullets(primal*senior);
		map.put(b, bullet);
	}
	public void printWeapon() {
		while(!mapValues.isEmpty()) {
			Bullets b = map.get(mapValues.pop());
			System.out.println("-----------------------------------------------");
			System.out.println("WeaponID: "+ weapon_id);
			System.out.println( "BulletID: "+ b.bulletID);
			System.out.println("Damage: " +damage);
			System.out.println("Coefficient: " +coeff/100);
			System.out.println("Scaling: " + attrRatio/100 +"x "+scaling);
			System.out.println( "Bullet Count: " + b.bulletCount);
			System.out.println( "Ammo Type : " + b.ammoType);
			System.out.println( "Ammo Mods: " + b.light+ "/" +b.medium+ "/" + b.heavy);
			if(b.offsetX != 0)
				System.out.println("SpreadX: " + b.offsetX);
			if(b.offsetZ !=0) 
				System.out.println("SpreadZ: " + b.offsetZ);
			if(b.splash != 0)
				System.out.println("Splash: "+ b.splash);
			if(b.buffID != 0)
				System.out.println("BuffID: "+b.buffID);
			System.out.println("Pierce: " + b.pierce);
			System.out.println("Velocity: " + b.velocity);
			if(b.antisub !=0)
				System.out.println("antisub: " + b.antisub);
			if(b.ignoreShield)
				System.out.println("Ignore Shields");
		}
	}
	
}
