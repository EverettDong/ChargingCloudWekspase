package com.cpit.icp.collect.process;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Person {
	 private String name;
	    private int age;
	    private List<String> data;
	public Person() {
			// TODO Auto-generated constructor stub
	
		}
		// get/set方法
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
	   
	    
		public List<String> getData() {
			return data;
		}
		public void setData(List<String> data) {
			this.data = data;
		}
		public static void main(String[] args) {
			
		   // Person person = new Person("luoxn28", 23);
		    String className = "com.cpit.icp.collect.process.Person";
		    try {
			Class clazz = Class.forName(className);
Object obj = clazz.newInstance();
		    Field[] fields = clazz.getDeclaredFields();
		    for (Field field : fields) {
		        String key = field.getName();
		        PropertyDescriptor descriptor;
				
					descriptor = new PropertyDescriptor(key, clazz);
					
					System.out.println(field.getType());
					 Method methodset = descriptor.getWriteMethod();
					 Type fc = field.getGenericType(); 
						 if (fc instanceof ParameterizedType) // 【3】如果是泛型参数的类型
							{
								ParameterizedType pt = (ParameterizedType) fc;
								Class genericClazz = (Class) pt.getActualTypeArguments()[0]; // 【4】
								if ( genericClazz.getName().startsWith("java.util.Date")
										|| genericClazz.getName().startsWith("javax")
										|| genericClazz.getName().startsWith("com.sun")
										|| genericClazz.getName().startsWith("sun")
										|| genericClazz.getName().startsWith("boolean")
										|| genericClazz.getName().startsWith("double")
										|| genericClazz.getName().startsWith("int")) {
									continue;
								}
								System.out.println(genericClazz);
										List li = new ArrayList();
										li.add("ss");
									methodset.invoke(obj, li);	
										// 得到泛型里的class类型对象。
								//List thisChain = new ArrayList(chain); 
//								System.out.println(chain);
								//thisChain.add(field); //!!
								//result.addAll(getBomFields1(new ArrayList(thisChain), genericClazz.getDeclaredFields()));
							}

						 
					 
		    }
					 // methodset.invoke(obj, "aa");
					 // if(key.equalsIgnoreCase("data")) {
						  
					//  }
					  
				   //     Object value = method.invoke(person);

				     //   System.out.println(key + ":" + value);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		      

		    }
}	    
	    


