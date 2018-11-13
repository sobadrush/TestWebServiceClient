package com.ctbc.TestWebServiceClient;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import cn.com.webservice.weather.WeatherWebService;
import cn.com.webservice.weather.WeatherWebServiceLocator;
import cn.com.webservice.weather.WeatherWebServiceSoap;

public class MyTestWeather {

	public static void main(String[] args) throws RemoteException, ServiceException {
		WeatherWebService weatherWebService = new WeatherWebServiceLocator();
		WeatherWebServiceSoap weatherWebServiceSoap = weatherWebService.getWeatherWebServiceSoap();
//		String[] strArray = weatherWebServiceSoap.getWeatherbyCityName("福州");
//		List<String> slist = Arrays.asList(strArray);;
//		int i = 0;
//		for (String str : slist) {
//			System.out.println(i++ + ": " + str);
//		}
		
		String[] supCities = weatherWebServiceSoap.getSupportCity("北京");
		for (String city : supCities) {
			System.out.println(" city = " + city);
		}
	}

}
