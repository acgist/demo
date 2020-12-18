package com.acgist.sentence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.fnlp.nlp.cn.CNFactory;
import org.fnlp.nlp.cn.CNFactory.Models;
import org.fnlp.nlp.corpus.StopWords;
import org.fnlp.util.exception.LoadModelException;
import org.junit.Test;

/**
 * seg.m 分词模型
 * pos.m 词性标注模型
 * dep.m 依存句法分析模型
 */
public class SentenceSegmentation {

	/**
	 * 中文分词
	 */
	@Test
	public void seg() throws LoadModelException {
		CNFactory factory = CNFactory.getInstance("models", Models.SEG);
		String[] words = factory.seg("我非常喜欢看动画片，你喜欢吗！");
		for (String word : words) {
			System.out.println(word);
		}
	}
	
	/**
	 * 中文词性标注
	 */
	@Test
	public void pos() throws LoadModelException {
		CNFactory factory = CNFactory.getInstance("models", Models.ALL);
		String[][] words = factory.tag("我非常喜欢看动画片，你喜欢吗！");
		int length = words.length;
		String[] header = words[0];
		for (int index = 0; index < header.length; index++) {
			System.out.print(header[index] + "：");
			for(int jndex = 1; jndex < length; jndex++) {
				System.out.print(words[jndex][index]);
			}
			System.out.println();
		}
	}
    public static void main(String args[]) {  
        int arr[][] = { { 1 }, { 2, 3 }, { 4, 5, 6 }, { 7, 8, 9, 10 } };  
        for (int x = 0; x < arr.length; x++) {  
            for (int y = 0; y < arr[x].length; y++) {  
                System.out.print(arr[x][y] + "、");  
            }  
            System.out.println("");  
        }  
    }  
	/**
	 * 实体名识别
	 */
	@Test
	public void ner() throws LoadModelException {
		CNFactory.getInstance("models");
	 	HashMap<String, String> result = CNFactory.ner("喻鱼非常喜欢看动画片，他的弟弟喻胜也喜欢！他们住在北京。");
	 	System.out.println(result);
	}
	
	/**
	 * 停顿词过滤
	 */
	@Test
	public void stop() throws LoadModelException {
		CNFactory factory = CNFactory.getInstance("models", Models.SEG);
		String[] words = factory.seg("我的爱好是看动画片，你喜欢吗！");
//		for (String string : words) {
//			System.out.println(string);
//		}
		StopWords stopWords = new StopWords("models/stop.txt");
		stopWords.phraseDel(words).forEach(System.out::println);
	}
	
	/**
	 * 自定义词典
	 */
	@Test
	public void dictSeg() throws LoadModelException {
		CNFactory factory = CNFactory.getInstance("models");
		CNFactory.addDict(Arrays.asList(new String[]{"喻鱼", "喜欢看"}));
		String[] words = factory.seg("喻鱼非常喜欢看动画片，你喜欢吗！");
		for (String word : words) {
			System.out.println(word);
		}
	}
	
	/**
	 * 自定义词性
	 * 词性标注：介词,疑问副词,形谓词,连词,叹词,时态词,名词,趋向词,人名,量词,疑问代词,限定词,运算符,副词,语气词,网址,把动词,省略词,指示词,方位词,机构名,时间短语,实体名,拟声词,动词,被动词,形容词,情态词,用户名,序数词,并列连词,标点,品牌名,型号名,人称代词,数词,地名,结构助词,从属连词,事件名,邮件,
	 */
	@Test
	public void dictPos() throws LoadModelException, IOException {
		String content = "喻鱼非常喜欢看动画片，你喜欢吗！";
		CNFactory factory = CNFactory.getInstance("models");
		CNFactory.addDict(new ArrayList<>(Arrays.asList(new String[]{"喻鱼", "人名"}, new String[]{"喜欢看", "人名", "动词", "情态词"})));
		String[] words = factory.seg(content);
		for (String word : words) {
			System.out.println(word);
		}
		System.out.println(factory.tag2String(content));
		System.out.println(CNFactory.ner(content));
	}

}
