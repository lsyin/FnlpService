package org.fnlp.FnlpService;

import javax.ws.rs.DefaultValue;

import javax.ws.rs.Path; 
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.fnlp.FnlpService.CNFactory;
import org.fnlp.FnlpService.MultiCNFactory;

import org.fnlp.FnlpService.ErrorCode;


import java.io.FileNotFoundException;
import java.io.IOException; 
import java.util.Date;
import com.google.gson.Gson;  
import com.google.gson.GsonBuilder; 
  

@Path("nlp/")
public class Service {
	

	
	@GET
	@Produces("text/plain")
	public String welcome() throws IOException{
		return "welcome";
	}
	
	/*
	 *  处理从客户端传过来的数据，判断类型，分别进行处理。
	 */
	 
	
	@POST
	@Produces("application/json")
	public String nlp(@DefaultValue("")		@QueryParam("token")  String token,String data) throws FileNotFoundException,IOException{
		
	//	content = new String(content.getBytes("ISO8859-1"), "utf-8"); //必须加上这个，因为客户端是以utf-8形式写入的，而java虚拟机默认是以ISO8859-1编码的，所以以utf-8格式显示的话会出现错误，必须将其转换成utf-8,在jetty测试时要加上这个。tomcat下不需要
		CNFactory cnf = MultiCNFactory.getInstance();
		Date start = new Date();
		Gson gson = new Gson();  // 使用GSON	，这个返回时如果属性值为空则返回的json字符串中不会显示这个属性。
		Gson gs = new GsonBuilder().serializeNulls().create();// 经过这样处理返回的json中如果属性值为空的话则可以默认设置为null了，就不会在返回时有属性值不存在了。
		Result rs = new Result();
	//处理由post方式发送过来的数据，默认发送过来的数据是json形式的字符串data，将字符串data转换成json对象并解析，如果传过来的不是json字符串，返回错误error_json
		PostData pdata=gson.fromJson(data, PostData.class);
		//if(pdata)是json,待完善
		String content=pdata.text;
		String type=pdata.type;
		String appid=pdata.appid;
		
		rs.type=type;
		rs.text=content;		
		if((content==null||content.length()==0)){
			Date end = new Date();
			rs.process_time = (end.getTime() - start.getTime()) + " ms";
			ErrorResult	error = new ErrorResult(rs,ErrorCode.NullContent);
			return gson.toJson(error);
		}		
				
		if(type.equals("seg"))
			rs.word = cnf.seg(content);
		else if(type.equals("pos")){
			String[][] strs = cnf.tag(content);
			if(strs==null){			
				ErrorResult	error = new ErrorResult(rs,ErrorCode.UNKNOWN);
				return gson.toJson(error);			
			}
			rs.word = strs[0];
			rs.pos = strs[1];
		}			
		else if(type.equals("parser")){
			String[][] strs = cnf.tag(content);
			if(strs==null){
			//	return new ErrorResult(rs,ErrorCode.UNKNOWN);
				ErrorResult	error = new ErrorResult(rs,ErrorCode.UNKNOWN);
				return gson.toJson(error);
			}	
			rs.word = strs[0];
			rs.pos = strs[1];
			rs.head = cnf.parse(strs[0], strs[1]);
			rs.dep=cnf.parse2T(strs[0], strs[1]).getTypes().split(" ");

		} 
		else{
			Date end = new Date();
			rs.process_time = (end.getTime() - start.getTime()) + " ms";
			ErrorResult	error = new ErrorResult(rs,ErrorCode.InvalidFunction);
			return gson.toJson(error);
	
		}
		Date end = new Date();
		rs.process_time = (end.getTime() - start.getTime()) + " ms";
		
		
	    return   gs.toJson(rs); 
	//	return rs;
	}

	
//   GET方式，暂时不需要，因为uid不好指定。
/*	
	@GET
	@Path("seg/{content}")
	@Produces("application/json")
	public Result seg(@PathParam("content") String content) throws FileNotFoundException,IOException{
		CNFactory cnf = MultiCNFactory.getInstance(uid);
		Result rs = new Result();

		if((content==null||content.length()==0)){
			return new ErrorResult(rs,ErrorCode.NullContent);
		}
		rs.uid = uid;
		rs.content=content;
		rs.word=cnf.seg(content);
		
    	System.out.println(rs.word);
    	
		return rs; //默认是utf-8格式，如果浏览器的中文显示格式是gbk的话则显示乱码
		
	}
	
	@GET
	@Path("pos/{content}")
	@Produces("application/json")
	public Result pos(@PathParam("content") String content) throws FileNotFoundException,IOException{
		CNFactory cnf = MultiCNFactory.getInstance(uid);
		Result rs = new Result();
		
		if((content==null||content.length()==0)){
			return new ErrorResult(rs,ErrorCode.NullContent);
		}
		rs.content=content;
		String[][] strs=cnf.tag(content);
		if(strs==null)
			return new ErrorResult(rs,ErrorCode.UNKNOWN);
		rs.uid = uid;		
		rs.word=strs[0];
		rs.pos=strs[1];		
		return rs;
		
	}
	
	@GET
	@Path("parser/{content}")
	@Produces("application/json")
	public Result parser(@PathParam("content") String content) throws FileNotFoundException,IOException{
		CNFactory cnf = MultiCNFactory.getInstance(uid);
		Result rs = new Result();
		
		if((content==null||content.length()==0)){
			return new ErrorResult(rs,ErrorCode.NullContent);
		}
		rs.content=content;
		String[][] strs=cnf.tag(content);
		if(strs==null)
			return new ErrorResult(rs,ErrorCode.UNKNOWN);
		rs.uid = uid;		
		rs.word=strs[0];
		rs.pos=strs[1];	
		rs.head=cnf.parse(strs[0],strs[1]);
		rs.type = cnf.parse2T(strs[0], strs[1]).getTypes().split(" ");
		return rs;		
	}
	
*/
}
