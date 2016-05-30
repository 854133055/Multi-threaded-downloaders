import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CyclicBarrier;


public class MainActivity {

    static String path = "http://dlsw.baidu.com/sw-search-sp/soft/6a/16871/uTorrent_3.4.5.41865.1458803953.exe";
	static int threadCount = 3;

	
	public static void main(String[] args) {
		File file = new File("E://1.exe");
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			
			if (conn.getResponseCode() == 200) {
				int length = conn.getContentLength();				
				RandomAccessFile  randomAccessFile = new RandomAccessFile(file, "rwd");
				 randomAccessFile.setLength(length);
				 randomAccessFile.close();
				
				CyclicBarrier barrier =new MainActivity().setCyclicBarrier();
				
				int size = length / threadCount;				
				for (int id = 0; id < threadCount; id++) {
					int startIndex = id * size;
					int endIndex = (id + 1) * size - 1;
					if (id == threadCount - 1) {
						endIndex = length - 1;
					}
					new DownLoadThread(id, startIndex, endIndex, barrier).start();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//设置CyclicBarrier，并在所有线程结束下载后打印一句话
	public CyclicBarrier setCyclicBarrier(){
		CyclicBarrier barrier = new CyclicBarrier(threadCount, new Runnable() {
			@Override
			public void run() {
				System.out.println("=======下载完成=======");
			}
		});
		return barrier;
	}
	
}
