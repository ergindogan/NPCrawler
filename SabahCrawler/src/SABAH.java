import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.i2i.common.utils.threadpool.base.ThreadPool;


public class SABAH {

	public static int fetcherCount = 10;
	public static int pagesToCrawl = 50;
	public static List<KoseYazari> yazarlar;
	
	//Her sayfada 6 yazi var, toplam yazi sayisi sayfa sayisi * 6.
	
	public static void main(String[] args) {
		
		OutputController.configureLogger(new File("C:/Users/ergindogan/Desktop/SabahCrawler.txt"), false);
		
		List<SABAHFetcher> fetchers = new ArrayList<SABAHFetcher>();
		
		yazarlar = Utils.getKoseYazarlari();
		
		for (int id = 1; id <= fetcherCount; id++) {
			try {
				SABAHFetcher fetcher = new SABAHFetcher(id);
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
