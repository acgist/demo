package com.acgist;

public class WordCount {

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
