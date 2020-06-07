package com.wgf.mysqlConnect;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class MysqlCon {

	private Connection con;

	/**
	 * 由于开发环境和测试的数据库地址不一定是同一个地址，所以把连接数据操作的独立出来，数据来源---读取properties文件的数据
	 * propertise一般放在src/main.resources目录下
	 * 
	 * @param args
	 */
	// 连接数据库
	public void CreateConnection() {
		// 实例化properties对象(java自带，用来对properties文件内容的读取)
		Properties pro = new Properties();
		try {
			// 通过load()方法加载properties文件内容;文件放在src/main/resources目录上，此处的路径以/开头
			pro.load(this.getClass().getResourceAsStream("/login.properties"));
			String dbUrl = pro.getProperty("DBURL");
			String dbUser = pro.getProperty("DBUSER");
			String dbPwd = pro.getProperty("DBPWD");
			// 加载驱动
			Class.forName("com.mysql.jdbc.Driver");
			// 建立连接
			con = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
			// 设置超时时间(在url中设置了一直重连的配置，若不设置超时时间，无法知道连接失败的原因)
			DriverManager.setLoginTimeout(10);

		} catch (IOException | ClassNotFoundException | SQLException e) {

			e.printStackTrace();
		}
	}
	//对登录信息进行检验，通过执行sql语句后判断是否有数据返回
	public boolean login(String user, String pwd) {
		String sql = "select * from userinfo where username= '" + user + "' and pwd='" + pwd + "';";
		System.out.println(sql);
		// 声明Statement对象
		Statement st;
		// 保存结果集
		ResultSet rs = null;
		try {
			// 实例化Statement对象，进行数据的增删查改；con是成员变量里的Connection对象
			st = con.createStatement();
			// 执行查询
			rs = st.executeQuery(sql);
			// 查询结果集不为null，且存在下一行数据
			if (rs != null && rs.next()) {
				// 元组数据代表数据库查询结果中的表头行
				ResultSetMetaData rsmd = rs.getMetaData();

				// 创建map来存储每行的数据
				Map<String, String> m = new HashMap<String, String>();

				// getClolumnCount()获取有多少列
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					// rsmd.getColumnName()是列名，rs.getString()是对应列的数据
					m.put(rsmd.getColumnName(i), rs.getString(i));
				}
				System.out.println(m.toString());
				// 关闭Statement对象（释放资源）
				con.close();
				// 关闭ResultSet(查询结果集)对象（释放资源）
				rs.close();
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	//调用存储过程来验证登录信息，不执行编译sql语句；预防sql注入
	public boolean CallProcedureLogin(String user, String pwd) {
		try {
			// 创建调用存储过程的对象CallableStatement;
			// 注：是java.sql.CallableStatement,非com.mysql.CallableStatement.jdbc
			// 使用Connection对象的preparecall()方法调用存储过程，"{call 存储过程名称(?,?)} "
			// 用？代替参数,用 , 间隔
			CallableStatement cs = con.prepareCall("{call login(?,?)}");
			// 传递参数，setString(第几个参数（int），参数)
			cs.setString(1, user);
			cs.setString(2, pwd);
			System.out.println(cs);
			// CallableStatent对象引用executeQuery()方法执行存储过程后返回测试结果给，ResultSet测试集对象
			ResultSet rs = cs.executeQuery();
			// 查询结果集不为null，且存在下一行数据
			if (rs != null && rs.next()) {
				// 元组数据代表数据库查询结果中的表头行
				ResultSetMetaData rsmd = rs.getMetaData();
				// 创建map来存储每行的数据
				Map<String, String> map = new HashMap<String, String>();

				// getClolumnCount()获取有多少列
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					// rsmd.getColumnName()是列名，rs.getString()是对应列的数据
					map.put(rsmd.getColumnName(i), rs.getString(i));
				}
				System.out.println(map.toString());
				// 关闭CallableStatement对象（释放资源）
				cs.close();
				// 关闭ResultSet(查询结果集)对象（释放资源）
				rs.close();
				return true;
			}
			//不满足if条件，也要释放资源
			cs.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	//获取查询语句的所有信息
	public List<Map<String,String>> getAllData(String sql){
		//创建Statement对象，操作数据库的增删查改
		Statement st;
		//list<map<String,String>>将数据库的数据
		List<Map<String,String>> listData=new ArrayList<Map<String,String>>();
		
		try {
			//实例化创建操作数据库对象Statement
			st=con.createStatement();
			//执行查询语句返回给ResultSet对象
			ResultSet rs=st.executeQuery(sql);
			//结果集中引用getMetaData()方法获取表头给ResultSetMetaData对象
			//获取表头，无须放入循环里面
			ResultSetMetaData rsmd=rs.getMetaData();
			
			while(rs !=null && rs.next()) {
				//实例化map对象，用于存储每一列的数据
				Map<String,String> map=new HashMap<String,String>();
				
				for(int i=1;i<rsmd.getColumnCount();i++) {
					//以{key:value}的形式存储数据
					map.put(rsmd.getColumnName(i), rs.getString(i));
				}
				//for循环结束，代表第一列的数据存储完毕，然后将这一列数据作为一个整体存储listData中
				listData.add(map);
				System.out.println(map.toString());
			}
			System.out.println("数据库中所有的元素："+listData);
			rs.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listData;
	}
	
	public Map<String,String> getUserInfo(String user) {
		String sql = "select * from userinfo where username= '" + user+"';";
		System.out.println(sql);
		// 声明Statement对象
		Statement st;
		// 保存结果集
		ResultSet rs = null;
		try {
			// 实例化Statement对象，进行数据的增删查改；con是成员变量里的Connection对象
			st = con.createStatement();
			// 执行查询
			rs = st.executeQuery(sql);
			// 查询结果集不为null，且存在下一行数据
			if (rs != null && rs.next()) {
				// 元组数据代表数据库查询结果中的表头行
				ResultSetMetaData rsmd = rs.getMetaData();

				// 创建map来存储每行的数据
				Map<String, String> map = new HashMap<String, String>();

				// getClolumnCount()获取有多少列
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					// rsmd.getColumnName()是列名，rs.getString()是对应列的数据
					if(!(rsmd.getColumnName(i)).equals("pwd")||!(rsmd.getColumnName(i)).equals("username")) {
						map.put(rsmd.getColumnName(i), rs.getString(i));
					}
				}
				System.out.println(map.toString());
				// 关闭Statement对象（释放资源）
				con.close();
				// 关闭ResultSet(查询结果集)对象（释放资源）
				rs.close();
				return map;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
	
