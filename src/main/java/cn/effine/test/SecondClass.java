/**
 * @Date 2014年11月20日  下午6:06:42
 * @author 张亚飞
 * @Copyright 云路科技  ©2014
 */

package cn.effine.test;

public class SecondClass {
	public static void main(String[] args) {

		//类型转换
		byte a1 = 2, a2 = 4, a3;
		short s = 16;
		// a2 = s;
		// a3 = a1 * a2;

		Employee e = new Employee("123");
		System.out.println(e.empID);

	}

}

class Person {
	String name = "No name";

	public Person(){}
	public Person(String nm) {
		name = nm;
	}
}

class Employee extends Person {
	String empID = "0000";

	public Employee(String id) {
		empID = id;

	}
}
