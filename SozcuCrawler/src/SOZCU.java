import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.i2i.common.utils.threadpool.base.ThreadPool;


public class SOZCU {
	
	public static int fetcherCount = 12;
	public static List<KoseYazari> yazarlar;
	public static int timeout = 20000;
	public static Calendar from = Calendar.getInstance();
	public static Calendar to = Calendar.getInstance();
	
	//Sozcu gazetesinde az sayida yazi yazanlarin eski aylardaki yazilarini alirken URL generate ettigimizde, suanki ayin degerleri tekrar geliyor cunku daha once
	//gostericek deger yok. Mukerrir kayitlar olusuyor.
	
	//Rahsan Gulsan, Nilay Ornek, Burak Goral, Demet Cengiz, Vahe Kilicarslan yazilarinin linklerinin bulundugu sayfa diger yazarlardan farkli, bunlar 
	//icin ayri bir kod yazmaya gerek var mi?
	
	public static void main(String args[]) throws ParseException{
		
		from.set(2012, 0, 1,0,0,0);
		to.set(2015, 0, 1,0,0,0);
		
		OutputController.configureLogger(new File("C:/Users/ergindogan/Desktop/SozcuCrawler.txt"), false);
		
		List<SOZCUFetcher> fetchers = new ArrayList<SOZCUFetcher>();
		
		yazarlar = Utils.getKoseYazarlari();
		
		for (int id = 1; id < (fetcherCount * (to.get(Calendar.YEAR) - from.get(Calendar.YEAR))) + 1; id++) {
			try {
				SOZCUFetcher fetcher = new SOZCUFetcher(id);
				fetchers.add(fetcher);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		
		ThreadPool fetcherPool = new ThreadPool("", fetchers);
		fetcherPool.setInterval(15);
		fetcherPool.start();

	}

}
