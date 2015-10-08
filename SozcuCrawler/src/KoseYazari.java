
/**
 *
 * @author ergindoganyildiz
 *
 *May 17, 2015
 */
public class KoseYazari {
	
	private String koseYazariLink;
	private String koseYazariAdi;

	public KoseYazari(){
		
	}
	
	public KoseYazari(String koseYazariAdi, String koseYazariLink){
		setKoseYazariAdi(koseYazariAdi);
		setKoseYazariLink(koseYazariLink);
	}

	public String getKoseYazariLink() {
		return koseYazariLink;
	}

	public void setKoseYazariLink(String koseYazariLink) {
		this.koseYazariLink = koseYazariLink;
	}

	public String getKoseYazariAdi() {
		return koseYazariAdi;
	}

	public void setKoseYazariAdi(String koseYazariAdi) {
		this.koseYazariAdi = koseYazariAdi;
	}

	@Override
	public String toString() {
		return "KoseYazari [koseYazariLink=" + koseYazariLink
				+ ", koseYazariAdi=" + koseYazariAdi + "]";
	}
	
}
