import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.i2i.common.utils.threadpool.base.ThreadPool;


public class YENISAFAK {

	public static int fetcherCount = 20;
	public static int pagesToCrawl = 3;
	public static List<KoseYazari> yazarlar;
	public static int timeout = 20000;
	
	public static void main(String[] args) {
		
		OutputController.configureLogger(new File("C:/Users/ergindogan/Desktop/YeniSafakCrawler.txt"), false);

		List<YENISAFAKFetcher> fetchers = new ArrayList<YENISAFAKFetcher>();
		
		yazarlar = Utils.getKoseYazarlari();
		
		for (int id = 1; id < fetcherCount; id++) {
			try {
				YENISAFAKFetcher fetcher = new YENISAFAKFetcher(id);
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
