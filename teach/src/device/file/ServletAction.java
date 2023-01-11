package device.file;
/*
 * 待完成：用MVC模式分开DB和Action操作
 * 增删改查看导印统功能的实现
 */
import device.dao.Data;
import device.dao.DeviceDao;
import device.dao.MySQLDao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServletAction extends HttpServlet {
	String module="device";
	String sub="file";
	public void showDebug(String msg){
		System.out.println("["+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date())+"]["+module+"/"+sub+"/ServletAction]"+msg);
	}
	/*
	 * 处理顺序：先是service，后根据情况doGet或者doPost
	 */
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			processAction(request,response);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	/*========================================函数分流 开始========================================*/
	public void processAction(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, JSONException {
		HttpSession session = request.getSession();
		request.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action");
		boolean actionOk = false;
		int resultCode=0;
		String resultMsg="ok";
		JSONObject json=new JSONObject(); 
		showDebug("[processAction]收到的action是："+action);
		if (action == null){
			resultMsg="传递过来的action是NULL";
		}else{
			json.put("action",action);
			json.put("result_code",0);
			json.put("result_msg","ok");
			//这几个常规增删改查功能
			if (action.equals("get_device_record")) {
				actionOk=true;
				try {
					getDeviceRecord(request, response, json);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (action.equals("add_device_record")) {
				actionOk=true;
				try {
					addDeviceRecord(request, response, json);
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (action.equals("modify_device_record")) {
				actionOk=true;
				try {
					modifyDeviceRecord(request, response, json);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (action.equals("delete_device_record")) {
				actionOk=true;
				try {
					deleteDeviceRecord(request, response, json);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (action.equals("query_record")) {
				actionOk=true;
				try {
					queryRecord(request, response, json);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (action.equals("update_record")) {
				actionOk=true;
				try {
					updateRecord(request, response, json);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(action.equals("upload")){
				actionOk=true;
				try {
					upload(request, response, json);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			try {
				responseBack(request,response,json);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	/*========================================函数分流 结束========================================*/
	/*========================================公共函数 开始========================================*/
	private Data getPageParameters(HttpServletRequest request,HttpServletResponse response,JSONObject json) throws JSONException{
		Data data=new Data();
		HttpSession session = request.getSession();
		/*----------------------------------------获取所有表单信息 开始----------------------------------------*/
		showDebug("[getPageParameters]----------------------------------------获取所有表单信息 开始----------------------------------------");
		JSONObject param=data.getParam();
		Enumeration requestNames=request.getParameterNames();
		for(Enumeration e=requestNames;e.hasMoreElements();){
			String thisName=e.nextElement().toString();
			String thisValue=request.getParameter(thisName);
			showDebug("[getPageParameters]"+thisName+"="+thisValue);
			param.put(thisName, thisValue);
		}
		showDebug("[getPageParameters]data的Param="+data.getParam().toString());
		showDebug("[getPageParameters]----------------------------------------获取所有表单信息 完毕----------------------------------------");
		/*----------------------------------------获取所有表单信息 完毕----------------------------------------*/
		/*----------------------------------------获取所有服务器端信息 开始----------------------------------------*/
		boolean isAjax=true;if (request.getHeader("x-requested-with") == null || request.getHeader("x-requested-with").equals("com.tencent.mm")){isAjax=false;}	//判断是异步请求还是同步请求，腾讯的特殊
		/*----------------------------------------获取所有服务器端信息 结束----------------------------------------*/
		return data;
	}
	private void responseBack(HttpServletRequest request,HttpServletResponse response,JSONObject json) throws JSONException {
		boolean isAjax=true;if (request.getHeader("x-requested-with") == null || request.getHeader("x-requested-with").equals("com.tencent.mm")){isAjax=false;}	//判断是异步请求还是同步请求，腾讯的特殊
		showDebug("request.getHeader是：x-requested-with=" + request.getHeader("x-requested-with")+",isAjax="+isAjax+"，session_id="+request.getHeader("Cookie"));
		if(isAjax){
			response.setContentType("application/json; charset=UTF-8");
			try {
				response.getWriter().print(json);
				response.getWriter().flush();
				response.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			String action=json.getString("action");
			String errorNo="0";
			String errorMsg="ok";
			String url = module+"/"+sub+"/result.jsp?action="+action+"&result_code="+errorNo+ "&result_msg=" + errorMsg;
			if(json.has("redirect_url")) url=json.getString("redirect_url");
			try {
				response.sendRedirect(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/*========================================公共函数 结束========================================*/
	/*========================================MySQL HTTP操作通用函数 开始========================================*/
	private void updateRecord(HttpServletRequest request, HttpServletResponse response,JSONObject json) throws JSONException, SQLException {
		MySQLDao dao=new MySQLDao();
		Data data=getPageParameters(request,response,json);
		dao.updateRecord(data,json);
	}
	private void queryRecord(HttpServletRequest request, HttpServletResponse response,JSONObject json) throws JSONException, SQLException {
		MySQLDao dao=new MySQLDao();
		Data data=getPageParameters(request,response,json);
		dao.queryRecord(data,json);
	}
	/*========================================MySQL HTTP操作通用函数 结束========================================*/
	/*========================================CRUD业务函数 开始========================================*/
	private void getDeviceRecord(HttpServletRequest request, HttpServletResponse response,JSONObject json) throws JSONException, SQLException {
		DeviceDao dao=new DeviceDao();
		Data data=getPageParameters(request,response,json);
		dao.getDeviceRecord(data,json);
	}
	private void modifyDeviceRecord(HttpServletRequest request, HttpServletResponse response,JSONObject json) throws JSONException, SQLException {
		DeviceDao dao=new DeviceDao();
		Data data=getPageParameters(request,response,json);
		dao.modifyDeviceRecord(data,json);
	}
	private void deleteDeviceRecord(HttpServletRequest request, HttpServletResponse response,JSONObject json) throws JSONException, SQLException {
		DeviceDao dao=new DeviceDao();
		Data data=getPageParameters(request,response,json);
		dao.deleteDeviceRecord(data,json);
	}
	private void addDeviceRecord(HttpServletRequest request, HttpServletResponse response,JSONObject json) throws JSONException, SQLException {
		DeviceDao dao=new DeviceDao();
		Data data=getPageParameters(request,response,json);
		dao.addDeviceRecord(data,json);
	}
	/*========================================CRUD业务函数 结束========================================*/
	/*========================================上传文件函数 开始========================================*/
	private void upload(HttpServletRequest request, HttpServletResponse response,JSONObject json) throws JSONException, SQLException {
		//先做初始化工作，定义一堆变量
		String fileUrl=null;
		String rootPath="D:\\upload";
		String filePath="teach\\ydbc2022";
		File fileDir = new File(rootPath+"\\"+filePath);
		File tmpRepository=null;
		//String rootUrl="/upload";
		String rootUrl="";
		String fileUrlPath=rootUrl+"/teach/ydbc2022";
		String tmpPath = rootPath + "\\temp\\"; // 临时路径
		tmpRepository = new File(tmpPath);
		if (!tmpRepository.exists() && !tmpRepository.isDirectory())
			tmpRepository.mkdirs();
		String objectId=null;
		String filePathName=null;
		List fileList = new ArrayList();
		List fieldList = new ArrayList();

		//开始接收上传文件
		try {
			if (ServletFileUpload.isMultipartContent(request)) {
				DiskFileItemFactory dff = new DiskFileItemFactory();// 创建该对象
				dff.setRepository(tmpRepository);// 指定上传文件的临时目录
				dff.setSizeThreshold(1024000);// 指定在内存中缓存数据大小,单位为byte
				ServletFileUpload sfu = new ServletFileUpload(dff);// 创建该对象
				sfu.setHeaderEncoding("UTF-8");
				sfu.setSizeMax(1000000000);// 指定单个上传文件的最大尺寸
				/*--------------------临时测试--------------------*/
				List<FileItem> list = sfu.parseRequest(request);

				for (Iterator<FileItem> iter = list.iterator(); iter.hasNext();) {
					FileItem item = iter.next();
					if (item.isFormField()) {
						String fieldName=item.getFieldName();
						String fieldValue=item.getString("UTF-8");
						HashMap map = new HashMap();
						map.put(fieldName, fieldValue);
						fieldList.add(map);
						//showDebug("[upload]收到字段："+fieldName+"="+fieldValue);
					}else{
						//如果是form-data
						String fileName=item.getName();
						int fileSize=0;
						if(!fileName.isEmpty()){
							//如果带有路径，就去掉路径，找文件名
							fileName=fileName.substring(fileName.lastIndexOf("\\")+1);
							//fileName=(new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date());
							objectId="UPLOAD_"+(new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date());
							showDebug("[upload]文件路径："+fileDir + "\\" + fileName);
							filePathName=fileDir + "\\" + fileName;
							FileOutputStream out=new FileOutputStream(fileDir + "\\" + fileName);
							InputStream in = item.getInputStream();
							byte buffer[] = new byte[1024];
							int len = 0;
							while((len=in.read(buffer))>0){
								out.write(buffer,0,len);
								fileSize=fileSize+len;
							}
							in.close();
							out.close();
							fileUrl=fileUrlPath+"/"+fileName;		//fileUrl
						}
						// 构造返回结果的json
						HashMap map = new HashMap();
						map.put("file_size", fileSize);
						map.put("file_path_name", filePathName);
						map.put("file_url_name", fileUrl);
						map.put("file_object_id", objectId);
						showDebug("[upload]存到：fileName="+fileName+",filePath="+filePath+",fileSize="+fileSize+",fileUrl="+fileUrl);
						fileList.add(map);
					}
				}
				/*--------------------测试完毕--------------------*/
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		json.put("upload_files", fileList);
		json.put("upload_fields", fieldList);
	}
	/*========================================上传文件函数 结束========================================*/
}
