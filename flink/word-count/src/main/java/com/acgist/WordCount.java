package com.acgist;

import java.io.Serializable;

public class WordCount implements Serializable {

	private static final long serialVersionUID = 1L;

	private String word;
	private long count;

	public WordCount() {
	}

	public WordCount(String word, long count) {
		this.word = word;
		this.count = count;
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

	@Override
	public String toString() {
		return this.word + " : " + this.count;
	}

}
