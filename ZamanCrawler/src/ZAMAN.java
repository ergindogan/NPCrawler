import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.i2i.common.utils.threadpool.base.ThreadPool;



public class ZAMAN {
	
	public static int fetcherCount = 21;
	public static List<KoseYazari> yazarlar;
	public static int timeout = 20000;
	public static String fromString;
	public static String toString;
	
	public static void main(String args[]){
		
		Calendar cal1 = Calendar.getInstance();
		cal1.set(2013, 7, 2,0,0,0);
		
		Calendar cal2 = Calendar.getInstance();
		cal2.set(2015, 7, 2,0,0,0);
		
		fromString = Utils.parseDateToString(cal1);
		toString = Utils.parseDateToString(cal2);
		
		
		OutputController.configureLogger(new File("C:/Users/ergindogan/Desktop/ZamanCrawler.txt"), false);

		List<ZAMANFetcher> fetchers = new ArrayList<ZAMANFetcher>();
		
		yazarlar = Utils.getKoseYazarlari();
		
		for (int id = 1; id < fetcherCount; id++) {
			try {
				ZAMANFetcher fetcher = new ZAMANFetcher(id);
				fetchers.add(fetcher);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		
		ThreadPool fetcherPool = new ThreadPool("", fetchers);
		fetcherPool.setInterval(15);
		fetcherPool.start();
		
//		try {
//			PrintWriter out = new PrintWriter("C:/Users/ergindogan/Desktop/ZamanCrawler.txt");
//			Date from;
//			Date to;
//			
//			
//			Calendar cal1 = Calendar.getInstance();
//			cal1.set(2015, 4, 26,0,0,0);
//			from = cal1.getTime();
//			
//			Calendar cal2 = Calendar.getInstance();
//			cal2.set(2015, 5, 29,0,0,0);
//			to = cal2.getTime();
//			
//			List<KoseYazari> yazarlar = Utils.getKoseYazarlari();
//			for (KoseYazari koseYazari : yazarlar) {
//				List<KoseYazisi> koseYazilari = Utils.getKoseYazisi(koseYazari, from, to);
//				
//				for (KoseYazisi koseYazisi : koseYazilari) {
//					System.out.println("Yazar : " + koseYazisi.getYazarAdi() + " Baslik : " + koseYazisi.getBaslik() + " Date : " + koseYazisi.getTarih());
//					String oneRecord = Utils.getOneRecord(koseYazisi);
//					out.write(oneRecord);
//					out.write("------------------------------------------------------------------------------------------ \n");
//				}
//				
//			}
//			out.close();
//			
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
		
	}

}
