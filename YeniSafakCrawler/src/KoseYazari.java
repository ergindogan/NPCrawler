
/**
 *
 * @author ergindoganyildiz
 *
 *May 17, 2015
 */
public class KoseYazari {
	
	private String koseYazariAdi;
	private String koseYazariLink;
	
	public KoseYazari(){
		
	}
	
	public KoseYazari(String koseYazariAdi, String koseYazariLink){
		setKoseYazariAdi(koseYazariAdi);
		setKoseYazariLink(koseYazariLink);
	}

	public String getKoseYazariAdi() {
		return koseYazariAdi;
	}

	public void setKoseYazariAdi(String koseYazariAdi) {
		this.koseYazariAdi = koseYazariAdi;
	}

	public String getKoseYazariLink() {
		return koseYazariLink;
	}

	public void setKoseYazariLink(String koseYazariLink) {
		String result = "http://www.yenisafak.com.tr" + koseYazariLink + "/?page=";
		this.koseYazariLink = result;
	}
	
}
