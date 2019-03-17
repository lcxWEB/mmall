package com.mmall;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class Demo {

	public static void main(String[] args) {

		int fourthBitFromRight = (888 & (1 << 3)) >> 3;
		System.out.println(fourthBitFromRight);

		String p = Integer.toBinaryString(1);
		p = new StringBuilder(p).reverse().toString();
		System.out.println(p);

		BeanFactory container = new XmlBeanFactory(new ClassPathResource("dd"));
		BeanFactory container1 = new ClassPathXmlApplicationContext("dd");
		BeanFactory container2 = new FileSystemXmlApplicationContext("dd");

		container.getBean("sss");
		container1.getBean("sdsd");
		container2.getBean("sdsds");

		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(null);

	}
}
