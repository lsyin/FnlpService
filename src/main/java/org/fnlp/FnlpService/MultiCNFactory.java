package org.fnlp.FnlpService;

import java.util.TreeMap;

import org.fnlp.app.keyword.WordExtract;

import org.fnlp.ml.types.Dictionary;
import org.fnlp.nlp.cn.ChineseTrans;
import org.fnlp.nlp.cn.anaphora.Anaphora;
import org.fnlp.nlp.cn.ner.TimeNormalizer;
import org.fnlp.nlp.cn.tag.CWSTagger;
import org.fnlp.nlp.cn.tag.NERTagger;
import org.fnlp.nlp.cn.tag.POSTagger;
import org.fnlp.nlp.parser.dep.JointParser;
import org.fnlp.nlp.parser.dep.TreeCache;
import org.fnlp.util.exception.LoadModelException;
/**
 * 统一的自然语言处理入口
 * 自然语言处理对象有此函数产生， 确保整个系统只有这一个对象，避免重复构造。
 * @author xpqiu
 */
public class MultiCNFactory {

	public static CWSTagger seg;
	public static POSTagger pos;
	public static NERTagger ner;
	public static JointParser parser;
	public static TreeCache treeCache;
	public static WordExtract key;

	public static String segModel = "/seg.m";
	public static String posModel = "/pos.m";
	public static String parseModel = "/dep.m";
	public static String arModel = "/ar.m";
	public static String timeModel = "/TimeExp.m";	
	public static String keywordModel = "/key.m";
	

	public static MultiCNFactory factory = new MultiCNFactory();
	public static ChineseTrans ct = new ChineseTrans();
	
	public static TreeMap<String,CNFactory> map = new TreeMap<String, CNFactory>();

	
	
	/**
	 *  初始化，加载所有模型
	 * @return
	 * @throws LoadModelException 
	 */
	public static CNFactory getInstance(){
		
		CNFactory cnf ;
		cnf = new CNFactory();
		
		cnf.parser = parser;
		
//		DBCollection db_users = MongodbConfig.getCollection(MongodbConfig.DICT_COLLECTION);
//		DBCursor cur = db_users.find(new BasicDBObject("token", token));
//		if(cur.hasNext()){
//			DBObject d = cur.next();
//			d.get("dict");
//			cnf.seg = new CWSTagger(seg);
//		}
		
//		cnf.seg = new CWSTagger(seg);
//		cnf.seg.setDictionary(dict);
		
		cnf.seg = seg;
		cnf.pos = pos;		
		cnf.pos = new POSTagger(seg,pos);
		
		Dictionary dict = new Dictionary(true);
		dict.add(word,null);
		cnf.pos.setDictionary(dict, true);
		
		cnf.seg = cnf.pos.cws;
		return  cnf;

	}
	/**
	 * 初始化
	 * @param path 模型文件所在目录，结尾不带"/"。
	 * @param model 载入模型类型
	 * @return
	 * @throws LoadModelException 
	 */
	public static void init(String path) throws LoadModelException{
		if(path.endsWith("/"))
			path = path.substring(0,path.length()-1);		
		System.out.println("加载分词/词性标注/句法分析模型");
		loadSeg(path);
		loadTag(path);
		loadNER(path);
		loadParser(path);
		System.out.println("加载关键词抽取模型");
		key = new WordExtract(seg,path+"/stopwords");
	//	System.out.println("加载指代消解模型");
	//	ar = new Anaphora(pos,path+"/ar.m");
	//	System.out.println("加载时间表达式模型");
	//	time = new TimeNormalizer("models/TimeExp.m");

	}
	/**
	 * 读入句法模型
	 * @param path 模型所在目录，结尾不带"/"。
	 * @throws LoadModelException 
	 */
	public static void loadParser(String path) throws LoadModelException {
		if(parser==null){
			String file = path+parseModel;
			parser = new JointParser(file);
		}
	}
	/**
	 * 读入实体名识别模型
	 * @param path 模型所在目录，结尾不带"/"。
	 * @throws LoadModelException 
	 */
	public static void loadNER(String path) throws LoadModelException {
		if(ner==null && pos!=null){
			ner = new NERTagger(pos);
		}
	}
	/**
	 * 读入词性标注模型
	 * @param path 模型所在目录，结尾不带"/"。
	 * @throws LoadModelException 
	 */
	public static void loadTag(String path) throws LoadModelException {
		if(pos==null){

			String file = path+posModel;
			if(seg==null)
				pos = new POSTagger(file);
			else{
				pos = new POSTagger(seg,file);					
			}
		}
	}

	/**
	 * 读入分词模型
	 * @param path 模型所在目录，结尾不带"/"。
	 * @throws LoadModelException 
	 */
	public static void loadSeg(String path) throws LoadModelException {
		if(seg==null)
		{
			String file = path+segModel;
			seg = new CWSTagger(file);
		} 
	}




}