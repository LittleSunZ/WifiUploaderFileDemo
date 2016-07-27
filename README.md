# WifiUploaderFileDemo
类似掌阅Wifi传书、网易云阅读Wifi传书等...

![手机截图](https://github.com/LittleSunZ/WifiUploaderFileDemo/blob/master/1.png)
![网页截图](https://github.com/LittleSunZ/WifiUploaderFileDemo/blob/master/2.png)


Defaults.java 是配置端口和html路径,和要解析的各种格式<br/>
WebServer.java 是Service,监听是否有请求
```
WebServer.Start(context);
```
SessionThread.java 得到请求之后可以在run函数里面判断内容,代码如下
```
public void run() {
	try {
		InputStream socketInput = _clientSocket.getInputStream();
		byte[] buffer = new byte[BUFFER_MAX];
		socketInput.read(buffer);
		_dataHandle = new DataHandle(buffer);
		Log.i("SessionThread",_dataHandle.getFileName());
		//得到文件名判断是打开html还是调用接口
		if(_dataHandle.getFileName().contains("/upload_success")){
			try {
			  //可以规定一些路径，然后判断是做什么操作
				String json = _dataHandle.getFileName().replace("/upload_success?data=","");
				json = URLDecoder.decode(json,"utf-8");
				Log.i("json",json);
				
				//在这里可以判断，进行耗时操作之后可以返回数据给html
				byte[] content = null;
				try {
					JSONObject contentJson = new JSONObject();
					contentJson.put("result", 0);
					contentJson.put("msg", "上传成功!");

					content = contentJson.toString().getBytes("utf-8");
				} catch (Exception ec) {
					// TODO Auto-generated catch block
					ec.printStackTrace();
				}
				sendResponse(_clientSocket, content);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
		  //获取内容返回给前端
			byte[] content = _dataHandle.fetchContent();
			sendResponse(_clientSocket, content);
		}

	} catch (Exception e) {
		_myLog.l(Log.DEBUG, "Exception in TcpListener");
	}
}
```
DataHandle.java 在fetchContent函数返回内容,也可以返回无需耗时操作的数据
```
public byte[] fetchContent() {
	byte[] backData = null;
	Log.i("filename",_httpHeader.getFileName());
	if (_httpHeader.getFileName().contains("index.html")) {
		if (!isSupportMethod()) {
			backData = fetchNotSupportMethodBack();
			return backData;
		}

		String filePath = fetchFilePath();
		boolean hasFile = FileSp.isExist("/"+_httpHeader.getFileName());
		Log.e("FilePath", filePath + "   " + hasFile);
		if (!hasFile) {
			backData = fetchNotFoundBack();
			return backData;
		}
		// 判断是否是支持的数据类型，如果不支持，从fetchNotSupportFileBack获取数据进行响应。
		if (!isSupportExtension()) {
			backData = fetchNotSupportFileBack();
			return backData;
		}

		backData = fetchBackFromFile("/"+_httpHeader.getFileName());
	} else if(_httpHeader.getFileName().contains("/upload_success")){

	}else{
		if (!isSupportMethod()) {
			backData = fetchNotSupportMethodBack();
			return backData;
		}

		String filePath = fetchFilePath();
		boolean hasFile = FileSp.isExist(_httpHeader.getFileName());
		Log.e("FilePath", filePath + "   " + hasFile);
		if (!hasFile) {
			backData = fetchNotFoundBack();
			return backData;
		}
		// 判断是否是支持的数据类型，如果不支持，从fetchNotSupportFileBack获取数据进行响应。
		if (!isSupportExtension()) {
			backData = fetchNotSupportFileBack();
			return backData;
		}

		backData = fetchBackFromFile(_httpHeader.getFileName());
	}
```
