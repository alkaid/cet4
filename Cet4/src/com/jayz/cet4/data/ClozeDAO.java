package com.jayz.cet4.data;

import java.util.ArrayList;
import java.util.List;
import com.jayz.cet4.common.RegexUtil;
import com.jayz.cet4.model.Article;
import com.jayz.cet4.model.Question;

/**
 * 这个类属于完形填空DAO，getSubjects返回的是多个题目的对象
 * @author Jayz
 *
 */
public class ClozeDAO {
	
	private String fileStr;
	
	/**
	 * 
	 * @param fileStr整个文件的String内容
	 */
	public ClozeDAO(String fileStr) {
		this.fileStr = fileStr;
	}
	
	/**
	 * 通过文件内容获得文件所包含的问题
	 * @return
	 */
	public List<Article> getSubjects(){
		List<Article> articles = new ArrayList<Article>();
		//整篇文章的正则表达式
		String articleRegex = "(?<=\\<article\\>)[\\s\\S]*?(?=\\</article\\>)";
		//每篇文章标题的正则表达式
		String articleTitleRegex = "(?<=\\<articleTitle\\>)[\\s\\S]*?(?=\\</articleTitle\\>)";
		//每篇文章内容的正则表达式
		String articleContentRegex = "(?<=\\<articleContent\\>)[\\s\\S]*?(?=\\</articleContent\\>)";
		//每篇文章整个问题(包括答案)的正则表达式
		String subjectRegex = "(?<=\\<subject\\>)[\\s\\S]*?(?=\\</subject\\>)";
		//答题的正则表达式
		String choiceRegex = "(?<=\\<choice\\>)[\\s\\S]*?(?=\\</choice\\>)";
		//正确答案的正则表达式
		String answerRegex = "(?<=\\<answer\\>)[\\s\\S]*?(?=\\</answer\\>)";
		//答案解析的正则表达式
		String introductionRegex = "(?<=\\<introduction\\>)[\\s\\S]*?(?=\\</introduction\\>)";
		/** 组装Article对象 */
		List<String> articlesStr = RegexUtil.parseFileStr(articleRegex, fileStr);
		if(articlesStr.size()!=0){
			for(int i = 0;i<articlesStr.size();i++){
				Article article = new Article(i);
				//截取文章标题
				List<String> articleTitleList = RegexUtil.parseFileStr(articleTitleRegex, articlesStr.get(i));
				if(articleTitleList.size()>0&&articleTitleList.get(0)!=null){
					article.setArticleTitle(articleTitleList.get(0));
				}
				//截取文章
				List<String> articleContentList = RegexUtil.parseFileStr(articleContentRegex, articlesStr.get(i));
				if(articleContentList.size()>0&&articleContentList.get(0)!=null){
					article.setArticleContent(articleContentList.get(0));
				}
				//截取题目集
				List<String> subjectsStr = RegexUtil.parseFileStr(subjectRegex, articlesStr.get(i));
				if(subjectsStr.size()!=0){
					List<Question> questionList = new ArrayList<Question>();
					for(int j = 0;j<subjectsStr.size();j++){
						Question q = new Question(j);
						//截取选项
						List<String> choicesStr = RegexUtil.parseFileStr(choiceRegex, subjectsStr.get(j));
						if(subjectsStr.size()!=0){
							q.setChoice(choicesStr);
						}
						//截取正确答案
						List<String> answersStr = RegexUtil.parseFileStr(answerRegex, subjectsStr.get(j));
						if(answersStr.size()>0&&answersStr.get(0)!=null){
							q.setAnswer(answersStr.get(0));
						}
						//截取答案解析
						List<String> introductionsStr = RegexUtil.parseFileStr(introductionRegex, subjectsStr.get(j));
						if(introductionsStr.size()>0&&introductionsStr.get(0)!=null){
							q.setAnalysis(introductionsStr.get(0));
						}
						questionList.add(q);
					}
					article.setQuestion(questionList);
				}
				articles.add(article);
			}
		}
		
		return articles;
		
	}
		
}
