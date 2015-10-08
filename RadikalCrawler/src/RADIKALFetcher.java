import java.util.List;

import org.apache.log4j.Logger;

import com.i2i.common.utils.threadpool.base.PooledMessageWorker;


public class RADIKALFetcher extends PooledMessageWorker {

	private int id;

	private static final Logger logger = Logger.getLogger(RADIKALFetcher.class);
	
	public RADIKALFetcher(int id){
		setId(id);
	}
	
	@Override
	public void run() {
		
		
		for (KoseYazari koseYazari : RADIKAL.yazarlar) {
			if(koseYazari.getId()%RADIKAL.fetcherCount == id){
				List<KoseYazisi> koseYazilari = Utils.getKoseYazisi(koseYazari, RADIKAL.sayfaSayisi);
				
				for (KoseYazisi koseYazisi : koseYazilari) {
					System.out.println("Id : " + getId() + " Yazar : " + koseYazisi.getYazarAdi() + " Baslik : " + koseYazisi.getBaslik() + " Date : " + koseYazisi.getTarih());
					String oneRecord = Utils.getOneRecord(koseYazisi);
					logger.info(oneRecord);
					logger.info("------------------------------------------------------------------------------------------ \n");
				}
			}
		}
			
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
