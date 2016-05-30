import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
/*
 * 工具类，用于xml文件的读写
 */

public class MyXmlReader {
	
	static File fileprogress = new File("E://Dowdloading.xml");	
	static Document document = null;
	static Element progress = null;
	
	
	//创建SAXReader对象，对传入的文件进行读取，并返回Document对象
	public static Document readForDoc(File fileprogress) {	
			SAXReader saxReader = new SAXReader();  
			try {//此方法好像必须写在try catch中，否则会出异常
				document = saxReader.read(fileprogress);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return document;
	}

	
	//对document对象进行解析，拿到当前线程id的"progress"结点，将其返回
	public static Element readForElement(int threadId) throws DocumentException{		
			document = readForDoc(fileprogress);	               
	        Element root = document.getRootElement();  
	        Element threId = root.element("thread"+threadId);    
	        progress = threId.element("progress");
	        return 	progress;
	}

	
	//返回当前id下“progress”节点的值
	public static String readForString(int threadId) throws DocumentException{			
			progress = readForElement(threadId);
			return progress.getText();
	}
	
	
	//更改当前id下“progress”节点的值，并将其更新到文件中
	public static void xmlReadWriter(int threadId,int location) throws DocumentException, IOException{
			
			document = readForDoc(fileprogress);
			readForElement(threadId).setText(location+"");
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileprogress));
			XMLWriter  writer = new XMLWriter(bos);
			writer.write(document);
			writer.close();	
	}
}
