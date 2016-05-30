import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import org.dom4j.DocumentException;

public class DownLoadThread extends Thread {
	int threadId;
	int startIndex;
	int endIndex;

	CyclicBarrier barrier = new CyclicBarrier(3);	
    static String path = "http://dlsw.baidu.com/sw-search-sp/soft/6a/16871/uTorrent_3.4.5.41865.1458803953.exe";
    int lastProgress= 0;
    
	public DownLoadThread(int threadId, int startIndex, int endIndex, CyclicBarrier barrier) {
		super();
		this.threadId = threadId;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.barrier = barrier;
	}

	@Override
	public void run() {
		File fileprogress = new File("E://Dowdloading.xml");
		//此处加锁，防止文件创建出错
			synchronized (fileprogress) {
				if (!fileprogress.exists()) {									
						try {
							MyXmlWrite.xmlWriter();
						} catch (IOException e) {
							e.printStackTrace();
						}					
				}
				beginDownload();		
			}			
	}
	
	//获得该ID下"progress"结点下的值，并计算此次下载的开始结点
	public int getStartProgress() throws DocumentException{
			String lastPro = MyXmlReader.readForString(this.threadId);
			lastProgress = Integer.valueOf (lastPro);
			startIndex = startIndex + lastProgress;
			System.out.println("此次下载开始结点是："+startIndex);
			return startIndex;
	}
	
	//拿到startIndex、endIndex，向服务器请求数据，并下载到临时文件中，并更改xml文件中数值
	public  void  beginDownload(){			
			File file = new File("E://1.exe");
			byte[] b = new byte[1024];				
			int len = 0;	
			int total;
			
			try {		
				startIndex = getStartProgress();		
				total = lastProgress;
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(5000);
				conn.setReadTimeout(5000);
				conn.setRequestProperty("Range", "bytes=" + startIndex + "-" + endIndex);
				
				if (conn.getResponseCode() == 206) {
						InputStream is = conn.getInputStream();				
						RandomAccessFile raf = new RandomAccessFile(file, "rwd");
						raf.seek(startIndex);
											
								while ((len = is.read(b)) != -1) {
									raf.write(b, 0, len);
									total = total + len;
									//将新的下载位置写到xml文件中
									MyXmlReader.xmlReadWriter(this.threadId, total);
									}
								raf.close();
								barrier.await();
				}			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
		
	}
	
}