package com.acgist.dl4j.gender;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.datavec.api.berkeley.Pair;
import org.datavec.api.conf.Configuration;
import org.datavec.api.records.reader.impl.LineRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.split.InputSplit;
import org.datavec.api.split.InputStreamInputSplit;
import org.datavec.api.writable.DoubleWritable;
import org.datavec.api.writable.Writable;

/**
 * 性别读取器，把什么样的数据放入深度学习网络其实就是在建模，这里把名字中的字符都映射成二进制，这也决定了隐层的输入
 */
public class GenderRecordReader extends LineRecordReader {
	
	private static final long serialVersionUID = 1L;
	
	private List<String> labels; // 标签数组
	private List<String> names = new ArrayList<String>(); // 名字的二值数组，包括二值标签
	private String possibleCharacters = ""; // 来自于名字的字母表，用于把逗号分隔的名字转成的二进制数
	public int maxLengthName = 0; // 名字最长的长度
	private int totalRecords = 0; // 总共的名字数量
	private Iterator<String> iterator; // 名字迭代器
	
	/**
	 * 允许客户端程序传一串名字
	 * @param labels M和F
	 */
	public GenderRecordReader(List<String> labels) {
		this.labels = labels;
	}

	/**
	 * 返回名字数量
	 */
	public int totalRecords() {
		return totalRecords;
	}

	/**
	 * 这函数做了一下几件事：
	 * 1.找到具体路径的文件
	 * 2.文件以逗号分隔名字和性别
	 * 3.每个性别对应一个文件
	 * 4.把名字定位临时文件
	 * 5.把名字的字符转成二进制
	 * 6.合并每个名字所有字符的二进制
	 * 7.找出唯一字符表去产生二值字符串
	 * 8.从文件中取出等量的记录，保证数据平衡
	 * 9.这个函数使用java8的stream特征，只需不到1分钟，普通方式要处理5-7分钟，
	 * 10.找到转换后的二值文件
	 * 11.把名字列表设置成可迭代模式
	 */
	@Override
	public void initialize(InputSplit split) throws IOException, InterruptedException {
		// 如果split是FileSplit的实例，注意FileSplit继承BaseInputSplit，BaseInputSplit继承InputSplit，split是InputSplit类
		if (split instanceof FileSplit) {
			URI[] locations = split.locations(); // 文件定位，感觉方法还是挺全的
			// 至少有俩文件
			if (locations != null && locations.length >= 1) {
				String longestName = ""; // 最长名字
				String uniqueString = ""; // 唯一字符
				List<Pair<String, List<String>>> tmpNames = new ArrayList<Pair<String, List<String>>>(); // 临时名字数组
				for (URI location : locations) { // 遍历每个路径
					File file = new File(location); // 路径对应文件夹
					List<String> tmp = this.labels.stream().filter(
							line -> file.getName().equals(line + ".csv")
						).collect(Collectors.toList());
					if (tmp.size() > 0) {
						java.nio.file.Path path = Paths.get(file.getAbsolutePath());
						List<String> tmpList = java.nio.file.Files.readAllLines(path, Charset.defaultCharset())
								.stream().map(element -> element.split(",")[0]).collect(Collectors.toList());
						Optional<String> optional = tmpList.stream()
								.reduce((a, b) -> a.length() >= b.length() ? a : b);
						if (optional.isPresent() && optional.get().length() > longestName.length()) {
							longestName = optional.get();
						}
						uniqueString = uniqueString + tmpList.toString(); // 把名字数组转成字符串
						Pair<String, List<String>> tmpPair = new Pair<String, List<String>>(tmp.get(0), tmpList);
						tmpNames.add(tmpPair); // 把文件名和名字数组构成pair装入tempNames数组
					} else {
						throw new InterruptedException("没有labels对应的CSV文件"); // 没文件报错
					}
				}
				this.maxLengthName = longestName.length(); // 赋值最大长度
				String unique = Stream.of(uniqueString).map(w -> w.split("")).flatMap(Arrays::stream)
						.distinct().collect(Collectors.toList()).toString();

				char[] chars = unique.toCharArray(); // 唯一字符转成字符数组
				Arrays.sort(chars); // 升序排列字符数组
				unique = new String(chars); // 再转成字符串
				unique = unique.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(",", ""); // 去掉方括号逗号
				if (unique.startsWith(" ")) {
					unique = " " + unique.trim();
				}
				this.possibleCharacters = unique; // 赋值给唯一字符串
				Pair<String, List<String>> tempPair = tmpNames.get(0); // 拿出第一个文件
				int minSize = tempPair.getSecond().size(); // 计算文件数据量
				for (int i = 1; i < tmpNames.size(); i++) { // 循环找到最小的数据量
					if (minSize > tmpNames.get(i).getSecond().size()) {
						minSize = tmpNames.get(i).getSecond().size();
					}
				}
				List<Pair<String, List<String>>> oneMoreTmpNames = new ArrayList<Pair<String, List<String>>>();
				for (int i = 0; i < tmpNames.size(); i++) { // 循环文件
					int diff = Math.abs(minSize - tmpNames.get(i).getSecond().size()); // 看每个文件数据量比最小的多多少
					List<String> tmpList = new ArrayList<String>();
					if (tmpNames.get(i).getSecond().size() > minSize) { // 如果比最小的大，只取最小长度的数据
						tmpList = tmpNames.get(i).getSecond();
						tmpList = tmpList.subList(0, tmpList.size() - diff);
					} else {
						tmpList = tmpNames.get(i).getSecond(); // 如果一样长保持不变
					}
					Pair<String, List<String>> tempNewPair = new Pair<String, List<String>>(tmpNames.get(i).getFirst(), tmpList);
					oneMoreTmpNames.add(tempNewPair);// 这样所有文件数据量都一样了
				}
				tmpNames.clear();
				List<Pair<String, List<String>>> secondMoreTmpNames = new ArrayList<Pair<String, List<String>>>();
				for (int i = 0; i < oneMoreTmpNames.size(); i++) {// 遍历刚才的数组
					int gender = oneMoreTmpNames.get(i).getFirst().equals("M") ? 1 : 0; // 给M编号1，F编号0
					List<String> secondList = oneMoreTmpNames.get(i).getSecond().stream()
							.map(element -> getBinaryString(element.split(",")[0], gender))
							.collect(Collectors.toList()); // 把名字转成二进制，并加上新编的类别号
					Pair<String, List<String>> secondTempPair = new Pair<String, List<String>>(
							oneMoreTmpNames.get(i).getFirst(), secondList);
					secondMoreTmpNames.add(secondTempPair); // 放入数组
				}
				oneMoreTmpNames.clear(); // 清空
				for (int i = 0; i < secondMoreTmpNames.size(); i++) {
					names.addAll(secondMoreTmpNames.get(i).getSecond());// 把所有文件名加到二进制名字数组
				}
				secondMoreTmpNames.clear(); // 清空
				this.totalRecords = names.size(); // 二进制名字总数
				Collections.shuffle(names); // shuffle
				this.iterator = names.iterator(); // 变成迭代器
			} else {
				throw new InterruptedException("没有labels对应的CSV文件");
			}
		} else if (split instanceof InputStreamInputSplit) {
			throw new InterruptedException("没有labels对应的CSV文件");
		}
	}

	@Override
	public List<Writable> next() { // 复写next方法，逗号分隔把数值转成小数且是一个可写的列表
		if (iterator.hasNext()) {
			List<Writable> list = new ArrayList<>();
			String currentRecord = iterator.next();
			String[] tmp = currentRecord.split(",");
			for (int i = 0; i < tmp.length; i++) {
				list.add(new DoubleWritable(Double.parseDouble(tmp[i])));
			}
			return list;
		} else {
			throw new IllegalStateException("没有更多元素");
		}
	}

	@Override
	public boolean hasNext() { // 复写hasNext
		if (iterator != null) {
			return iterator.hasNext();
		}
		throw new IllegalStateException("没有更多元素");
	}

	@Override
	public void close() throws IOException {
	}

	@Override
	public void setConf(Configuration conf) {
		this.conf = conf;
	}

	@Override
	public Configuration getConf() {
		return conf;
	}

	@Override
	public void reset() { // 复写reset，把保存的名字赋给迭代器
		this.iterator = names.iterator();
	}

	private String getBinaryString(String name, int gender) {
		String binaryString = "";
		for (int j = 0; j < name.length(); j++) { // 对每个名字，遍历每个字符，从字符集中找到索引，把索引转成二进制，并补足5位0
			String fs = StringUtils.leftPad(Integer.toBinaryString(this.possibleCharacters.indexOf(name.charAt(j))), 5, "0");
			binaryString = binaryString + fs;
		}
		binaryString = org.apache.commons.lang3.StringUtils.rightPad(binaryString, this.maxLengthName * 5, "0");// 这名字处理完了，要保证最大长度一致，右补0，比如某人名字是一个字符串，最长是两个字符串，缺的就补0
		binaryString = binaryString.replaceAll(".(?!$)", "$0,");// 这里是一个鬼畜般的用法，老衲也是查了半天，$是结束符，?!$代表不是结束符，.(?!$)代表不是结束符的一个字符，$0是找到这个字符,
		System.out.println(name + "姓名二进制：" + binaryString);
		return binaryString + "," + String.valueOf(gender);
	}

}
