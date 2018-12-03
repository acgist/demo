package com.acgist.drools;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.acgist.drools.pojo.Person;

public class PersonTest {

	static KieSession getSession() {
		KieServices kieServices = KieServices.Factory.get();
		KieContainer kieContainer = kieServices.getKieClasspathContainer();
		return kieContainer.newKieSession("personRuleKSession");
	}

	public static void run() {
		KieSession kieSession = getSession();
		Person p1 = new Person("郭芙蓉", 8);
		Person p2 = new Person("佟湘玉", 18);
		Person p3 = new Person("李大嘴", 32);
		Person p4 = new Person("祝无双", 66);
		Person p5 = new Person("白展堂", 68);
		System.out.println(p1);
		System.out.println(p2);
		System.out.println(p3);
		System.out.println(p4);
		System.out.println(p5);
		System.out.println("---------------------------");
		kieSession.insert(p1);
		kieSession.insert(p2);
		kieSession.insert(p3);
		kieSession.insert(p4);
		kieSession.insert(p5);
		int count = kieSession.fireAllRules();
		System.out.println("---------------------------");
		System.out.println("总执行了" + count + "条规则");
		System.out.println("---------------------------");
		System.out.println(p1);
		System.out.println(p2);
		System.out.println(p3);
		System.out.println(p4);
		System.out.println(p5);
		kieSession.dispose();
	}

	public static void main(String[] args) {
		run();
	}

}
