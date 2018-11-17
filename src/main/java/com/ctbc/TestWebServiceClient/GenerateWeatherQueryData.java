package com.ctbc.TestWebServiceClient;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.apache.commons.io.FileUtils;

import cn.com.webservice.weather.WeatherWebService;
import cn.com.webservice.weather.WeatherWebServiceLocator;
import cn.com.webservice.weather.WeatherWebServiceSoap;

public class GenerateWeatherQueryData {
	public static void main(String[] args) throws ServiceException, RemoteException {
		
		WeatherWebService weatherWebService = new WeatherWebServiceLocator();
		WeatherWebServiceSoap weatherWebServiceSoap = weatherWebService.getWeatherWebServiceSoap();
		
		String filePath = "E:/CTBC_workspace_phantom/TestWebServiceClient/src/main/java/_01_測試資料/天氣API查詢條件.txt";
		File outputFile = new File(filePath);
		
		if (!outputFile.exists()) {
			try {
				outputFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// 清空舊檔案
		try {
			FileChannel.open(outputFile.toPath(), StandardOpenOption.WRITE).truncate(0).close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 取得所有支援此WebService的城市
		String[] supCities = weatherWebServiceSoap.getSupportCity("");
		for (int i = 0 ; i < supCities.length ; i++) {
			// System.out.println(" city = " + city);
			supCities[i] = String.format("%03d - 城市 = %s", i+1 , supCities[i]);
			System.out.println(supCities[i]);
		}

		List<String> supCitiesList = Arrays.asList(supCities);
		try {
			FileUtils.writeLines(outputFile, StandardCharsets.UTF_8.name(), supCitiesList, null, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}









