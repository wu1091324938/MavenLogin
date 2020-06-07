package com.wgf.mysqlConnect;

import java.util.List;
import java.util.Map;

public class GetDataTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MysqlCon mysql=new MysqlCon();
		mysql.CreateConnection();
		List<Map<String,String>> list=mysql.getAllData("select * from userinfo");
		System.out.println(list.get(0));
		//文字 “数据库所有的元素”之前打印的信息，是每存入一列数据进map后输一遍的打印语句（第一个输出语句）
		//与文字 “数据库所有的元素”同一行的信息，是将所有数据存入List后作为一个整体后打印的（第二个输出语句）
		//最后一行信息，是当前main方法中通过list的下标获取的第一条信息
	}

}
