/**
 * @Date 2014年11月20日  下午6:06:42
 * @author 张亚飞
 * @Copyright 云路科技  ©2014
 */

package cn.effine.test;

import java.util.Arrays;

public class FirstClass {
	public void sayHello() {
		int i = 0;
		System.out.println(i);
	}

	public String sayWord() {
		System.out.println("第二次修改");

		System.out.println("say word !");

		System.out.println("第一次修改");
		
		String str = "effine";
		char[] c = str.toCharArray();
		System.out.println();
		
		Arrays.sort(c);
		

		return null;
	}
}
