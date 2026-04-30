package com.acgist.flink;

import com.acgist.flink.fun.Function;

public class FunExe {

	public Object exe(Function fun) {
		return fun.exe();
	}
	
	static Function fun = () -> {
		System.out.println("exe");
		return null;
	};

	public static void main(String[] args) {
		FunExe exe = new FunExe();
		exe.exe(fun);
	}

}
