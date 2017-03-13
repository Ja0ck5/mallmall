package com.mallmall.common.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mallmall.common.bean.HttpResult;


@Service
public class ApiService {
	// 注入Httpclient对象
	@Autowired(required = false)
	private CloseableHttpClient httpclient;
	
	@Autowired(required = false)
	private RequestConfig requestConfig;
	/**
	 * doGet 方法，返回具体异常，由调用方处理 200 成功 返回内容，其他返回 null
	 * 
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String doGet(String url) throws ClientProtocolException, IOException {
		// 创建http GET请求
		HttpGet httpGet = new HttpGet(url);
		//设置请求参数
		httpGet.setConfig(requestConfig);
		CloseableHttpResponse response = null;
		try {
			// 执行请求
			response = httpclient.execute(httpGet);
			// 判断返回状态是否为200
			if (200 == response.getStatusLine().getStatusCode()) {
				String content = EntityUtils.toString(response.getEntity(), "UTF-8");
				return content;
			}
		} finally {
			if (response != null) {
				response.close();
			}
		}
		return null;
	}

	/**
	 * 带有参数的 get 请求
	 * 
	 * @param url
	 * @param param
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public String doGet(String url, Map<String, String> param)
			throws ClientProtocolException, IOException, URISyntaxException {
		URIBuilder uriBuilder = new URIBuilder(url);
		for (Map.Entry<String, String> entry : param.entrySet()) {
			uriBuilder.addParameter(entry.getKey(), entry.getValue());
		}
		return doGet(uriBuilder.build().toString());
	}

	/**
	 * 带参数的 post 请求
	 * 
	 * @param url
	 * @param param
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResult doPost(String url, Map<String, String> param) throws ClientProtocolException, IOException {
		// 创建http POST请求
		HttpPost httpPost = new HttpPost(url);
		//设置请求参数
		httpPost.setConfig(requestConfig);
		if (null != param) {
			// 设置2个post参数，一个是scope、一个是q
			List<NameValuePair> parameters = new ArrayList<NameValuePair>(0);
			for (Map.Entry<String, String> entry : param.entrySet()) {
				parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			// 构造一个form表单式的实体
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters,"UTF-8");
			// 将请求实体设置到httpPost对象中
			httpPost.setEntity(formEntity);
		}
		CloseableHttpResponse response = null;
		try {
			// 执行请求
			response = httpclient.execute(httpPost);
			return new HttpResult(response.getStatusLine().getStatusCode(),EntityUtils.toString(response.getEntity(), "UTF-8"));
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	/**
	 * 提交 json 参数的post 请求
	 * 
	 * @param url
	 * @param json
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResult doPostJson(String url, String json) throws ClientProtocolException, IOException {
		// 创建http POST请求
		HttpPost httpPost = new HttpPost(url);
		//设置请求参数
		httpPost.setConfig(requestConfig);
		if (null != json) {
			// 构造一个string的实体
			StringEntity stringEntity = new StringEntity(json,"UTF-8");
			// 将请求实体设置到httpPost对象中
			httpPost.setEntity(stringEntity);
		}
		CloseableHttpResponse response = null;
		try {
			// 执行请求
			response = httpclient.execute(httpPost);
			return new HttpResult(response.getStatusLine().getStatusCode(),EntityUtils.toString(response.getEntity(), "UTF-8"));
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	/**
	 * 不带参数的 post 请求
	 * 
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResult doPost(String url) throws ClientProtocolException, IOException{
		return doPost(url,null);
	}

}
