package org.fnlp.FnlpService;


import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;

import org.fnlp.FnlpService.MultiCNFactory;
public class Server {
    public static void main(String[] args) throws Exception {
    	
    	String path="WebContent/models/";
    	JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
        factory.setServiceClass(Service.class);       
        factory.setAddress("http://localhost:8088");
        factory.create();
        init(path);  //初始化配置

        System.out.println("Server start...");
    }
    
    
    public static void init(String path)
    {    	    	
    	
    	//加载模型，初始化配置
    	try {
    			System.out.println("data path : " + path);	
    			MultiCNFactory.init(path);
    	} catch (Exception e) {
			System.out.println("init fail!");
			e.printStackTrace();
		}
    }
    
}

