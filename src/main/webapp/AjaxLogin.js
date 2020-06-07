/**
 * 登录界面使用异步登录的ajax脚本
 */
function login(){
	//使用Ajax向服务器发起请求
	//$在此处表示引用jquery的方法
	$.ajax(
			{
			//接口四大要素：url,http请求方法，请求体，请求头
			//url为ajax发送的请求地址
			url:"./Login",
			//http请求方法
			type:"post",
			//请求头：预期服务器返回数据的类型
			//dataType可使用json格式
			//dataType:"text",
			dataType:"json",
			//请求体：将form表单中的参数信息序列化成ajax可以提交的数据
			//通过CSS的ID定位form元素
			data:$("#formData").serialize(),
			
			//function无方法名，result为jquery为本来就有的参数，此处是拿result进行处理
			//接口请求成功直接调用success,否则调用error(自动判断)
			success:function(result){
				//alert("接口调用成功"+"；"+result);
				$("#msg")[0].innerText="接口返回的信息是："+result["msg"];
				if(result['statusCode']=="1"){
					window.location.href="user.html";
				}
			},
			error:function(result){
				alert("接口调用失败，请检查ajax的服务器代码"+"；"+result);
			}
			
			}
		)
}
//获取用户信息
function getUser() {
	// 定义一个存放url的变量，指定请求的接口的地址
	var AjaxURL = "./GetUserInfo";
	$.ajax({
		// 方法用post
		type : "post",
		// 返回和请求的参数类型传递方式。
		dataType : "json",
		// 请求的接口地址
		url : AjaxURL,
		// 接口执行的返回，当接口调用成功时，执行success中的方法
		success : function(result) {
			// 将返回结果解析出来的对应内容填写到对应的元素中
			document.getElementById("userid").innerText = result["id"];
			document.getElementById("nickname").innerText = result["nickname"];
			document.getElementById("describe").innerText = result["introduce"];
		},
		// 接口调用出错时，执行该方法
		error : function() {
			alert("调用UserInfo接口出错，请检查。");
		}
	});
}


function logout(){
	//使用Ajax向服务器发起请求
	//$在此处表示引用jquery的方法
	$.ajax(
			{
			//接口四大要素：url,http请求方法，请求体，请求头
			//url为ajax发送的请求地址
			url:"./Logout",
			//http请求方法
			type:"post",
			//请求头：预期服务器返回数据的类型
			//dataType可使用json格式
			//dataType:"text",
			dataType:"json",
			//请求体：将form表单中的参数信息序列化成ajax可以提交的数据
			//通过CSS的ID定位form元素
			data:$("#formData").serialize(),
			
			//function无方法名，result为jquery为本来就有的参数，此处是拿result进行处理
			//接口请求成功直接调用success,否则调用error(自动判断)
			success:function(result){
				alert("注销成功");
				//注销成功后跳转回登录页面
				window.location.href="index.html";
			},
			error:function(result){
				alert("接口调用失败，请检查ajax的服务器代码"+"；"+result);
			}
			
			}
		)
}


