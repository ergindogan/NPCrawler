import java.util.List;

import org.apache.log4j.Logger;

import com.i2i.common.utils.threadpool.base.PooledMessageWorker;


public class MILLIYETFetcher extends PooledMessageWorker {
	
	private int id;

	private static final Logger logger = Logger.getLogger(MILLIYETFetcher.class);
	
	public MILLIYETFetcher(int id){
		setId(id);
	}
	
	@Override
	public void run() {
		
		
		for (KoseYazari koseYazari : MILLIYET.yazarlar) {
			List<KoseYazisi> koseYazilari = Utils.getKoseYazisi(koseYazari, getId());
			
			for (KoseYazisi koseYazisi : koseYazilari) {
				System.out.println("Id : " + getId() + " Yazar : " + koseYazisi.getYazarAdi() + " Baslik : " + koseYazisi.getBaslik() + " Date : " + koseYazisi.getTarih());
				String oneRecord = Utils.getOneRecord(koseYazisi);
				logger.warn(oneRecord);
				logger.warn("------------------------------------------------------------------------------------------ \n");
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
