import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
/*
 * xml文件的初始化：
 * <?xml version="1.0" encoding="UTF-8"?>
	<DowdloadThread>
  			<thread0>
    				<progress>0</progress>
  			</thread0>
  			<thread1>
    				<progress>0</progress>
  			</thread1>
  			<thread2>
    				<progress>0</progress>
  			</thread2>
	</DowdloadThread>
 * 
 */
public class MyXmlWrite {
	
	public static void xmlWriter() throws IOException{
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("DowdloadThread");
		
		Element thread0 = root.addElement("thread0");
		Element downprogress1 = thread0.addElement("progress");
		downprogress1.setText("0");
		
		Element thread1 = root.addElement("thread1");
		Element downprogress2 = thread1.addElement("progress");
		downprogress2.setText("0");
		
		Element thread2 = root.addElement("thread2");
		Element downprogress3 = thread2.addElement("progress");
		downprogress3.setText("0");
							
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setTrimText(false);
		format.setEncoding("utf-8");
		
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File("E://Dowdloading.xml")));
		XMLWriter writer = new XMLWriter(bos, format);
		writer.write(document);
}

}
