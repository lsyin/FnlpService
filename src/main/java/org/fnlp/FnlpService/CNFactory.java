package org.fnlp.FnlpService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;




import org.fnlp.ml.types.Dictionary;
import org.fnlp.nlp.cn.ChineseTrans;
import org.fnlp.nlp.cn.tag.CWSTagger;
import org.fnlp.nlp.cn.tag.NERTagger;
import org.fnlp.nlp.cn.tag.POSTagger;
import org.fnlp.nlp.parser.dep.DependencyTree;
import org.fnlp.nlp.parser.dep.JointParser;
import org.fnlp.nlp.parser.dep.TreeCache;
import org.fnlp.util.exception.LoadModelException;
/**
 * 统一的自然语言处理入口
 * 自然语言处理对象有此函数产生， 确保整个系统只有这一个对象，避免重复构造。
 * @author xpqiu
 */
public class CNFactory {

	protected CWSTagger seg;
	protected POSTagger pos;
	protected NERTagger ner;
	protected JointParser parser;
 
	  //	protected AbstractExtractor key ;
	//自定义部分
	protected TreeCache treeCache;	
	protected Dictionary dict = new Dictionary(true);
	protected ChineseTrans ct = new ChineseTrans();

	
	/**
	 * 
	 * @param al 字典 ArrayList<String[]>
	 * 						每一个元素为一个单元String[].
	 * 						String[] 第一个元素为单词，后面为对应的词性
	 * @throws LoadModelException 
	 */
	public void addDict(ArrayList<String[]> al)  {
		dict.add(al);
		setDict();
	}	
	
	/**
	 * 增加词典
	 * @param words
	 * @param pos
	 */
	public void addDict(Collection<String> words, String pos) {
		for(String w:words){
			dict.add(w, pos);
		}
		setDict();
	}

	/**
	 * 更新词典
	 */
	private void setDict()  {
		if(dict==null||dict.size()==0)
			return;
		if(pos!=null){
			pos.setDictionary(dict, true);
		}
		else if(seg!=null){
			seg.setDictionary(dict);
		}
	}

	
	
	

	/**
	 * 分词
	 * @param input 字符串
	 * @return 词数组
	 */
	public String[] seg(String input){
		if(seg==null)
			return null;
		return seg.tag2Array(input);
	}

	/**
	 * 词性标注
	 * @param input 词数组
	 * @return 词性数组
	 */
	public String[] tag(String[] input){
		if(pos==null)
			return null;
		return pos.tagSeged(input);
	}

	/**
	 * 词性标注
	 * @param input 字符串
	 * @return 词+词性数组
	 */
	public String[][] tag(String input){
		if(pos==null||seg==null)
			return null;
		return pos.tag2Array(input);
	}
	/**
	 * 词性标注
	 * @param input 字符串
	 * @return 词/词性 的连续字符串
	 */
	public String tag2String(String input) {
		if(pos==null||seg==null)
			return null;
		return pos.tag(input);
	}

	/**
	 * 句法分析
	 * @param words 词数组
	 * @param pos 词性数组
	 * @return 依赖数组
	 */
	public int[] parse(String[] words, String[] pos){
		if(parser==null)
			return null;
		return parser.parse(words, pos);
	}
	

	

	/**
	 * 句法分析
	 * @param words 词数组
	 * @param pos 词性数组
	 * @return 句法树
	 */
	public DependencyTree parse2T(String[] words, String[] pos) {
		if(parser==null)
			return null;
		if(treeCache!=null){
			DependencyTree tree = treeCache.get(words,pos);
			if(tree!=null)
				return tree;
		}
		
		if(words==null||pos==null||words.length==0||pos.length==0||words.length!=pos.length)
			return null;
		return parser.parse2T(words, pos);
	}
	
	/**
	 * 实体名识别
	 * @param s
	 * @return 实体名+类型
	 */
	public  HashMap<String, String> ner(String s) {
		if(ner==null)
			return null;
		return ner.tag(s);
	}
	
	
	/**
	 * 关键词抽取
	 * @param s
	 * @return 关键词
	*/	 
	public Map<String, Integer> key(String s){
		if(MultiCNFactory.key==null)
			return null;
		return MultiCNFactory.key.extract(s, 5);
	}

	public  void readDepCache(String file) throws IOException{
		treeCache = new TreeCache();
		treeCache.read(file);	
	}
	



}