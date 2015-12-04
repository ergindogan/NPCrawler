import java.util.List;

import org.apache.log4j.Logger;

import com.i2i.common.utils.threadpool.base.PooledMessageWorker;


/**
 * @author ergindoganyildiz
 * 
 * Oct 14, 2015
 */
public class POSTAFetcher extends PooledMessageWorker {
	
	private int id;
	private int retreivedPassages = 0;

	private static final Logger logger = Logger.getLogger(POSTAFetcher.class);
	
	public POSTAFetcher(int id){
		setId(id);
	}
	
	@Override
	public void run() {
		
		List<KoseYazisi> koseYazilari = Utils.getKoseYazisi(POSTA.yazarlar.get(id), POSTA.sayfaSayisi);
		for (KoseYazisi koseYazisi : koseYazilari) {
//			System.out.println("Id : " + getId() + " Yazar : " + koseYazisi.getKoseYazariAdi() + " Baslik : " + koseYazisi.getBaslik() + " Date : " + koseYazisi.getTarih());
			String oneRecord = Utils.getOneRecord(koseYazisi);
			logger.warn(oneRecord);
			logger.warn("------------------------------------------------------------------------------------------ \n");
			retreivedPassages++;
		}
			
		System.out.println(POSTA.yazarlar.get(id).getKoseYazariAdi() + " fetched..." + retreivedPassages + " passages retrieved!.");
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
