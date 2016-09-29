package org.fnlp.FnlpService;

/**
 * 错误的类型，用于对话系统内部构造DialogException，或者用于外部（webservice.Service）构造ErrorResult
 */
public enum ErrorCode {
	UNKNOWN(9, "未知错误"),
	DIALOG(10, "无剩余sid可分配(当且仅当存在sid保持每小时和server交互一次直到2100000000个sid分配完毕再次循环到该占用的sid)"), 
	UID(11, null),
	MSG(12, "URL中did必须大于上回合did"), 
	RSP(13, "URL中did必须大于上回合did"), 
	SID(14, "sid无效或者过期"),
	INDEX(15, "使用错误index查询目的地列表"), 
	SEARCH_SERVER(16, "Search Server错误"),
	NLP_SERVER(17, "NLP Server错误"),
	TOKEN(18, "Token无效"), 
	
	Agent(20, "Agent 分配错误"), 
	
	LanGen(30, "语言生成错误"),
	NullContent(31,"无内容错误"),
	InvalidFunction(32,"没有该功能"),
	Other(40,"其他错误"), 
	
	MUSIC(50,"音乐播放错误"),
	MUSIC_ERRORCODE(51,"音乐ERRORCODE错误"), 
	
	APPCONTROL_ERRORCODE(61,"APPCONTROL ERRORCODE错误");
	
	private int code;
	private String info;
	
	private ErrorCode(int code, String info) {
		this.code = code;
		this.info = info;
	}
	
	public int getCode() {
		return code;
	}
	
	public String getInfo() {
		return info;
	}
	
}
