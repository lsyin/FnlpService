package org.fnlp.FnlpService;



import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="result")
public class Result {
	
	public String process_time;
		
	public String type;
	
	public String text;

	public String[] word;
	
	public String[] pos;
	
	public String[] dep;
	
	public int[] head;
	
}
