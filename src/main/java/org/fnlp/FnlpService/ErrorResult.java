package org.fnlp.FnlpService;

import javax.xml.bind.annotation.XmlRootElement;

import org.fnlp.FnlpService.ErrorCode;
@XmlRootElement(name="errorresult")
public class ErrorResult extends Result{


	public ErrorCode errorcode;
	
	public  ErrorResult(){
		
	}
	

	public  ErrorResult(Result rs,ErrorCode ec){
	

		this.errorcode=ec;
	}

}
