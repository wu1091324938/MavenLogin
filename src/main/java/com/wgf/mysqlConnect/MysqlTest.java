package com.wgf.mysqlConnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MysqlTest {

	/**
	 * 由于开发环境和测试的数据库地址不一定是同一个地址，所以把连接数据操作的独立出来，数据来源---读取properties文件的数据
	 * propertise一般放在src/main.resources目录下
	 * @param args
	 */
	public static void main(String[] args) {
		String url="jdbc:mysql://localhost:3306/practise06?useUnicode=true&autoReconnect=true&characterEncoding=utf-8";
		String user="root";
		String password="wu123456";
		
		try {
			//1、加载驱动程序
			Class.forName("com.mysql.jdbc.Driver");
			//2、建立数据库链接
			Connection con=DriverManager.getConnection(url, user, password);
			//3、操作数据库，实现增删改查
			Statement st= con.createStatement();
			String inputUser="kevin";
			String inputPwd="123456";
			String sql="select * from userinfo where username='"+inputUser+"' and pwd='"+inputPwd+"';";
			//ResultSet对象查询结果进行存储
			ResultSet result=st.executeQuery(sql);
			System.out.println(result);
			//.next()查询是否有下一行
			if(result.next()) {
				System.out.println("登录成功");
			}else {System.out.println("登录失败");}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
