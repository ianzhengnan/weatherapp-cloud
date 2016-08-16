package com.ian.hcp.samples.weatherapp.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import com.sap.core.connectivity.api.http.HttpDestination;

@Path("/weather")
@Produces({MediaType.APPLICATION_JSON})
public class WeatherService {

	private static final int COPY_CONTENT_BUFFER_SIZE = 1024;
	
	@GET
	@Path("/")
	@Produces({MediaType.APPLICATION_JSON})
	public String getWeatherInformation(@QueryParam(value = "id") String id, @QueryParam(value = "q") String q){
		
		HttpClient httpClient = null;
		
		String retVal = null;
		
		try{
			Context ctx = new InitialContext();
			
			HttpDestination destination = (HttpDestination)ctx.lookup("java:comp/env/" + "openweathermap-destination");
			
			httpClient = destination.createHttpClient();
			
			final String baseURL = destination.getURI().toString();
	
			String destnationURL = null;
			
			System.out.println(baseURL);
			
			if (id != null && id.trim().length() > 0) {
				destnationURL = MessageFormat.format("{0}&id={1}&units=metric", baseURL, id);
			}else{
				destnationURL = MessageFormat.format("{0}&q={1}&units=metric", baseURL, q);
			}
			
			System.out.println(destnationURL);
			
			HttpGet httpGet = new HttpGet(destnationURL);
			
			HttpResponse httpResponse = httpClient.execute(httpGet);
			
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			
			if(statusCode != HttpServletResponse.SC_OK){
				throw new ServletException("Expected response status code is 200 but it is " + statusCode + " .");
			}
			
			HttpEntity entity = httpResponse.getEntity();
			
			if (entity != null) {
				InputStream inputStream = entity.getContent();
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				
				try{
					byte[] buffer = new byte[COPY_CONTENT_BUFFER_SIZE];
					int len;
					while((len = inputStream.read(buffer)) != -1){
						outputStream.write(buffer, 0, len);
					}
				}catch(IOException e){
					throw e;
				}catch(RuntimeException e){
					httpGet.abort();
					throw e;
				}finally{
					try{
						inputStream.close();
					}catch(Exception e){
						//Ignore
					}
				}
				
				retVal = outputStream.toString("UTF-8");
			}
		}catch(NamingException e){
			String errorMessage = "Lookup of destination failed with reason: "
					+ e.getMessage()
					+ ". See "
					+ "logs for details. Hint: Make sure to have the destination "
					+ "[openweathermap-destination]" + " configured.";
			retVal = errorMessage;
		}catch(Exception e){
			String errorMessage = "Connectivity operation failed with reason: "
					+ e.getMessage()
					+ "logs for details. Hint: Make sure to have an HTTP proxy configured in your "
					+ "local Eclipse environment in case your environment uses "
					+ "communication.";
			retVal = errorMessage;
		}finally {
			if (httpClient != null) {
				httpClient.getConnectionManager().shutdown();
			}
		}
		
		return retVal;
	}
	
}