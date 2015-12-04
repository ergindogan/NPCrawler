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
		
		List<KoseYazisi> koseYazilari = Utils.getKoseYazisi(RADIKAL.yazarlar.get(id), RADIKAL.sayfaSayisi);
		for (KoseYazisi koseYazisi : koseYazilari) {
			System.out.println("Id : " + getId() + " Yazar : " + koseYazisi.getKoseYazariAdi() + " Baslik : " + koseYazisi.getBaslik() + " Date : " + koseYazisi.getTarih());
			String oneRecord = Utils.getOneRecord(koseYazisi);
			logger.warn(oneRecord);
			logger.warn("------------------------------------------------------------------------------------------ \n");
		}
			
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
