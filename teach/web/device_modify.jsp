<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>添加设备</title>
</head>

<body>
	<input type="hidden" id="page_id" name="page_id" value="device_modify"/>
	设备    ID：<input id="device_id" name="device_id"><br>
	设备名称：<input id="device_name" name="device_name"><br>
	<button id="modify_button">提交修改</button>
</body>
</html>
<script src="jquery.min.js"></script>
<script src="device.js"></script>