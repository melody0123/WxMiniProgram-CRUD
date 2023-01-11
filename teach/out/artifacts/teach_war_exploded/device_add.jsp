<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>添加设备</title>
</head>

<body>
	<input type="hidden" id="page_id" name="page_id" value="device_add"/>
	设备    ID：<input id="device_id" name="device_id"><br>
	设备名称：<input id="device_name" name="device_name"><br>
	设备类型：<input id="device_type" name="device_type">建议输入：“video”，或者“sensor”，或者“other”<br>
	<button id="add_button" name="add_button">确定添加</button>
</body>
</html>
<script src="jquery.min.js"></script>
<script src="device.js"></script>