package listener;




import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

//import com.lingclouds.rs.MongodbConfig;
import org.fnlp.FnlpService.MultiCNFactory;
//import org.fnlp.WebService.Service;




/**
 * 服务器启动初始化
 */
public class Init implements ServletContextListener {
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {	
		// 运行时参数 : -Xmx1200m
		/*
		 workspace下/.metadata/.plugins/org.eclipse.wst.server.core/servers.xml
		  打开后将其中的start-timeout的值改大点儿就行了
		*/
		System.out.println("Init");
		ServletContext ctx = event.getServletContext();
		
		String path = ctx.getRealPath("/");
		path += "/models/";
		//String path = ctx.getInitParameter("path_of_data");
		System.out.println("data path : " + path);	
		
		try {
		//	MongodbConfig.setUpMongodb();
			MultiCNFactory.init(path);
		} catch (Exception e) {
			System.out.println("init fail!");
			e.printStackTrace();
		}
		
			
	}
}
