package com.acgist;

import java.io.Serializable;

public class WordCount implements Serializable {

	private static final long serialVersionUID = 1L;

	private String word;
	private long count;
	private long time;

	public WordCount() {
	}
	
	public WordCount(String word, long count, long time) {
		this.word = word;
		this.count = count;
		this.time = time;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return this.word + " : " + this.count + " : " + this.time;
	}

}
