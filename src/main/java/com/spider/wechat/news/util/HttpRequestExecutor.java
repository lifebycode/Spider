package com.spider.wechat.news.util;
import com.spider.wechat.news.dto.HttpResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.http.MediaType;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@SuppressWarnings("deprecation")
public class HttpRequestExecutor {
	/**
	 * 服务处理响应超时时间
	 */
	private final static int SOCKET_TIMEOUT = 5000;

	/**
	 * 建立连接超时时间
	 */
	private final static int CONNECT_TIMEOUT = 5000;

	public static String doPostByJson(String uri, String postEntity){
		try {
			CloseableHttpClient httpClient = getHttpClient();
			HttpResponse response=doPostByJson(httpClient,uri,postEntity);
			return response.getStringResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
	}

	public static HttpResponse doGet(CloseableHttpClient httpclient, String uri, Header... headers)
			throws ClientProtocolException, IOException{
		return doGet(httpclient,uri,"UTF-8",headers);
	}


	public static HttpResponse doGet(CloseableHttpClient httpclient, String uri,String defaultCharset, Header... headers)
			throws ClientProtocolException, IOException{
		String result = null;
		HttpGet httpGet = new HttpGet(uri);
		RequestConfig config = RequestConfig.custom()
				.setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).build();   // 设置请求和传输超时
	    httpGet.setConfig(config);
	    httpGet.setHeaders(headers);
		long startTime = System.currentTimeMillis();
		CloseableHttpResponse response = httpclient.execute(httpGet);
		long costsMilliseconds =  (System.currentTimeMillis() - startTime);
		int statusCode = response.getStatusLine().getStatusCode();
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			result = EntityUtils.toString(entity, defaultCharset);
		}
		return new HttpResponse(statusCode,costsMilliseconds,result);
	}


	public static HttpResponse doPost(CloseableHttpClient httpclient, String uri, String body,String charset, Header... headers)
			throws ClientProtocolException, IOException{
		String result = null;
		HttpPost httpPost = new HttpPost(uri);
		RequestConfig config = RequestConfig.custom()
				.setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).build();// 设置请求和传输超时
		httpPost.setConfig(config);
		httpPost.setHeaders(headers);

		if (StringUtils.isNotBlank(body)) {
	    	StringEntity entity = new StringEntity(body, charset);
	    	httpPost.setEntity(entity);
	    }

		long startTime = System.currentTimeMillis();
		CloseableHttpResponse response = httpclient.execute(httpPost);
		long costsMilliseconds = System.currentTimeMillis() - startTime;
		int statusCode = response.getStatusLine().getStatusCode();
    	if (HttpStatus.SC_MOVED_PERMANENTLY == statusCode || HttpStatus.SC_MOVED_TEMPORARILY == statusCode || HttpStatus.SC_SEE_OTHER == statusCode) {
    	    HttpGet httpGet = new HttpGet(response.getLastHeader("location").getValue());
    	    response = httpclient.execute(httpGet);
    	}
    	HttpEntity entity = response.getEntity();
		if (entity != null) {
			result = EntityUtils.toString(entity, charset);
		}

		return new HttpResponse(statusCode,costsMilliseconds,result);
	}

	public static HttpResponse doPost(CloseableHttpClient httpclient, String uri, String body, Header... headers)
			throws ClientProtocolException, IOException{
	    return doPost(httpclient,uri,body, "UTF-8",headers);
	}

	
	public static HttpResponse doPostByXml(CloseableHttpClient httpclient, String uri, String postEntity)
			throws ClientProtocolException, IOException{
		Header contentTypeHeader = new BasicHeader(HTTP.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE);
		return doPost(httpclient, uri, postEntity, contentTypeHeader);
	}
	
	public static HttpResponse doPostByJson(CloseableHttpClient httpclient, String uri, String postEntity)
			throws ClientProtocolException, IOException{
		Header contentTypeHeader = new BasicHeader(HTTP.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		return doPost(httpclient, uri, postEntity, contentTypeHeader);
	}

	public static HttpResponse doPostWithQueryByJson(CloseableHttpClient httpclient, String uri, String postEntity , String queryParam)
			throws ClientProtocolException, IOException{
		Header contentTypeHeader = new BasicHeader(HTTP.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		if (StringUtils.isNotBlank(queryParam)) {
			uri += (uri.indexOf('?') == -1) ? '?' + queryParam : '&' + queryParam;
		}
		return doPost(httpclient, uri, postEntity, contentTypeHeader);
	}
	
	public static HttpResponse doPostWithQueryByJson(CloseableHttpClient httpclient, String uri, String postEntity , String queryParam, Header... headers)
			throws ClientProtocolException, IOException{
		if (StringUtils.isNotBlank(queryParam)) {
			uri += (uri.indexOf('?') == -1) ? '?' + queryParam : '&' + queryParam;
		}
		return doPost(httpclient, uri, postEntity, headers);
	}
	
	public static CloseableHttpResponse doGetWithQuery(CloseableHttpClient httpclient,	String uri, String queryParam, Header... headers)
			throws ClientProtocolException, IOException{
		if (StringUtils.isNotBlank(queryParam)) {
			uri += (uri.indexOf('?') == -1) ? '?' + queryParam : '&' + queryParam;
		}
		HttpGet httpGet = new HttpGet(uri);
		RequestConfig config = RequestConfig.custom()
				.setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).build();   // 设置请求和传输超时
	    httpGet.setConfig(config);
	    httpGet.setHeaders(headers);

		return httpclient.execute(httpGet);
	}
	
	public static void colseHttpClient(CloseableHttpClient httpClient){
		try {
			httpClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static CloseableHttpClient getHttpsClient() throws Exception{
		SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
				null, new TrustStrategy() {
					
					@Override
					public boolean isTrusted(X509Certificate[] arg0, String arg1) 
							throws CertificateException {
						return true;//信任所有
					}
				}).build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, 
				SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		return HttpClients.custom().setSSLSocketFactory(sslsf).build();
	}

	public static CloseableHttpClient getHttpClient() throws Exception{
		return HttpClients.custom().build();
	}



}
