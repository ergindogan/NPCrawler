
/**
 * @author ergindoganyildiz
 * 
 * Oct 14, 2015
 */
public class KoseYazari {
	private String koseYazariAdi;
	private String tumYazilariLink;

	public KoseYazari(){
		
	}
	
	public KoseYazari(String koseYazariAdi, String tumYazilariLink){
		setKoseYazariAdi(koseYazariAdi);
		setTumYazilariLink(tumYazilariLink);
	}

	public String getKoseYazariAdi() {
		return koseYazariAdi;
	}


	public void setKoseYazariAdi(String koseYazariAdi) {
		this.koseYazariAdi = koseYazariAdi;
	}


	public String getTumYazilariLink() {
		return tumYazilariLink;
	}


	public void setTumYazilariLink(String tumYazilariLink) {
		this.tumYazilariLink = tumYazilariLink;
	}

}
