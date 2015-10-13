
/**
 *
 * @author ergindoganyildiz
 *
 *May 17, 2015
 */
public class KoseYazari {
	
	private String koseYazariAdi;
	private String linkYazarAdi;
	private String tumYazilariLink;

	public KoseYazari(){
		
	}
	
	public KoseYazari(String koseYazariAdi, String linkYazarAdi){
		setKoseYazariAdi(koseYazariAdi);
		setLinkYazarAdi(linkYazarAdi);
		setTumYazilariLink(CommonLinks.BASE_URL + "/index/" + linkYazarAdi + "-page=1/");
	}
	
	@Override
	public String toString() {
		return "KoseYazari [koseYazariAdi = " + koseYazariAdi
				+ ", tumYazilariLink = " + tumYazilariLink + "]";
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

	public String getLinkYazarAdi() {
		return linkYazarAdi;
	}

	public void setLinkYazarAdi(String linkYazarAdi) {
		this.linkYazarAdi = linkYazarAdi;
	}
	
}
