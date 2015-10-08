
/**
 *
 * @author ergindoganyildiz
 *
 *May 17, 2015
 */
public class KoseYazari {
	
	private int id;
	private String koseYazariAdi;

	public KoseYazari(){
		
	}
	
	public KoseYazari(int id, String koseYazariAdi){
		setId(id);
		setKoseYazariAdi(koseYazariAdi);
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getKoseYazariAdi() {
		return koseYazariAdi;
	}


	public void setKoseYazariAdi(String koseYazariAdi) {
		this.koseYazariAdi = koseYazariAdi;
	}

	@Override
	public String toString() {
		return "KoseYazari [id=" + id + ", koseYazariAdi=" + koseYazariAdi
				+ "]";
	}
	
}
