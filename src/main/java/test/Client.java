package test;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.fnlp.FnlpService.PostData;
import com.google.gson.Gson;
public class Client {
	 
	public static String post(String url, String path, String content) {
		URL u = null;
		HttpURLConnection con = null;
		try {
			u = new URL(url+"/"+path);
			con = (HttpURLConnection) u.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
		//	con.setRequestProperty("uid", uid);
		//	con.setRequestProperty("access_token", access_token);
			OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "utf-8");
			
			osw.write(content);			
			osw.flush();
			osw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
		 
		//读取返回内容
		StringBuffer buffer = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
			String temp;
			while ((temp = br.readLine()) != null) {
				buffer.append(temp);
				buffer.append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
		return buffer.toString();
	}	
	
	 // 模拟客户端发post to /contacts
	public static void main(String[] args) {
		String url = "http://101.231.32.206:8080/service";
	//	String uid = "test";
	//	String access_token = "test";
		
		String content;
		String path;		
		String s;
		
		Gson gson=new Gson();
		PostData pdata=new PostData();
		pdata.text="我喜欢桂香";
		pdata.appid="34";
		pdata.type="parser";
		
		content=gson.toJson(pdata);
//		path = "seg";		
//		String s = post(url,path, content,uid,access_token);
//		System.out.println(s);
		
	//	content ="we are good friends";
	//	content="we are good friend";
		path = "nlp?token=Mytoken";		
		s = post(url,path, content);
		System.out.println(content);
		System.out.println(s);
	}
 
}