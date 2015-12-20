import java.io.File;
import java.util.List;


public class BaseMain {
	
	protected static int fetcherCount = 0;
	protected static List<BaseKoseYazari> yazarlar;
	protected static final int timeout = 30000;
	
	protected void configureLogger(String path){
		OutputController.configureLogger(new File(path), false);
	}
	
	protected void configureAttributes(int fetcherCount){
		BaseMain.fetcherCount = fetcherCount;
	}

}
