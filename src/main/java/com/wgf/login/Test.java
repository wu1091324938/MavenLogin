package com.wgf.login;

import java.util.Map;

import com.wgf.mysqlConnect.MysqlCon;

public class Test {

	public static void main(String[] args) {
		String userinfo="{";
		MysqlCon mysql=new MysqlCon();
		mysql.CreateConnection();
		Map<String,String> userMap=mysql.getUserInfo("Will");
		for(String key:userMap.keySet()) {
			//json字符串的格式"键":"值"
			userinfo+="\"" + key + "\":\"" +userMap.get(key) +"\" " ;
		}
		userinfo+="}";
		//userinfo=userinfo.replace(",}", "}");
		System.out.println(userinfo);
	}

}
