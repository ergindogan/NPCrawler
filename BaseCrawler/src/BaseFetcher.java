import interfaces.Fetcher;

import org.apache.log4j.Logger;

import com.i2i.common.utils.threadpool.base.PooledMessageWorker;


public class BaseFetcher extends PooledMessageWorker implements Fetcher {
	
	protected static Logger logger;
	
	private int id;

	public BaseFetcher(Class<?> cls, int id) {
		logger = Logger.getLogger(cls);
		setId(id);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
