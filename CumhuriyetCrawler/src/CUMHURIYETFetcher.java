import java.util.List;

import org.apache.log4j.Logger;

import com.i2i.common.utils.threadpool.base.PooledMessageWorker;


public class CUMHURIYETFetcher extends PooledMessageWorker {
	
	private int id;

	private static final Logger logger = Logger.getLogger(CUMHURIYETFetcher.class);
	
	public CUMHURIYETFetcher(int id){
		setId(id);
	}
	
	@Override
	public void run() {
		
		
		for (KoseYazari koseYazari : CUMHURIYET.yazarlar) {
			if(koseYazari.getId()%CUMHURIYET.fetcherCount == id){
				List<KoseYazisi> koseYazilari = Utils.getKoseYazisi(koseYazari, CUMHURIYET.sayfaSayisi);
				
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
