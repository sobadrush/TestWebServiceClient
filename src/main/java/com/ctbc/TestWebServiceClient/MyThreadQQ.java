package com.ctbc.TestWebServiceClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.alibaba.fastjson.JSONObject;

public class MyThreadQQ implements Runnable {

	private static int nn = 1;

	private BufferedReader br = null;
	private BufferedWriter bw = null;

	public MyThreadQQ() {

	}

	public MyThreadQQ(BufferedReader br, BufferedWriter bw) {
		this.br = br;
		this.bw = bw;
	}

	@Override
	public void run() {

		String lineStr = null;

//		while (true) {
			try {

				Integer cityId = null;
				String cityName = null;
				
				while ((lineStr = br.readLine()) != null) {

					// ========== ( Step1. (1)Read file (2)Parseing text ) ==========
//					synchronized (this.br) { // (※※)拿到 br 的thread才可進來進行IO
						System.out.println(String.format("%03d , %s , %s", nn++, Thread.currentThread().getName(), lineStr));
						cityId = Integer.parseInt(lineStr.split(" ")[0]);
						cityName = lineStr.split(" ")[2];
//					} // end-of synchronized

					// ========== ( Step2. send Request ) ==========
					List<String> townsList = getTaiwanCityTownship(cityId);

					// ========== ( Step3. write file ) ==========
//					synchronized (this.bw) { // (※※)拿到 bw 的thread才可進來進行IO
						String cityInfo = String.format("%s , CityName = %s , CityId = %d %s", Thread.currentThread().getName(), cityName, cityId, System.lineSeparator());
						System.out.print(" >>> cityInfo >>> " + cityInfo);

						bw.write(" >>>>>>>>>> cityInfo : " + cityInfo);
						for (String town : townsList) {
							String townsInfo = Thread.currentThread().getName() + " - " + town + System.lineSeparator();
							System.out.print(townsInfo);
							bw.write(" >>> townsInfo : " + townsInfo);
						}

//					} // end-of synchronized
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

//			if (lineStr == null) {
////				try {
////					System.err.println(String.format("@@@ %s , 讀到文件結尾 @@@", Thread.currentThread().getName()));
////					bw.flush();
////				} catch (IOException e) {
////					e.printStackTrace();
////				}
//				break;
//			}

//		}
	}

	private static List<String> getTaiwanCityTownship(int cityId) {
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
			HttpGet request = new HttpGet(String.format("http://works.ioa.tw/weather/api/cates/%d.json", cityId));

			// add request header
			request.addHeader("Content-type", "application/json; charset=utf-8");
			HttpResponse response = httpClient.execute(request);

			String resCodeStr = String.format("<<< [%s] getTaiwanCityTownship(), Response Code : %s", Thread.currentThread().getName(), response.getStatusLine().getStatusCode());
			System.err.println(resCodeStr);

			try (BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));) {
				StringBuffer sb = new StringBuffer();
				String line = "";
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}

				List<String> townsList = new ArrayList<String>();
				JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(sb.toString());
				int townsSize = jsonObject.getJSONArray("towns").size();
				for (int i = 0 ; i < townsSize ; i++) {
					// System.out.println(" >>> " + jsonObject.getJSONArray("towns").getJSONObject(i).getString("name"));
					townsList.add(" >>> " + jsonObject.getJSONArray("towns").getJSONObject(i).getString("name"));
				}

				return townsList;
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String getCityName(int cityId) {
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
			HttpGet request = new HttpGet(String.format("http://works.ioa.tw/weather/api/cates/%d.json", cityId));

			request.addHeader("Content-type", "application/json; charset=utf-8");
			HttpResponse response = httpClient.execute(request);

			String resCodeStr = String.format("<<< [%s] getCityName(), Response Code : %s", Thread.currentThread().getName(), response.getStatusLine().getStatusCode());
			//System.err.println(resCodeStr);

			try (BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));) {
				StringBuffer sb = new StringBuffer();
				String line = "";
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}

				JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(sb.toString());
				// System.out.println(" >>> " + jsonObject);
//				Set<Entry<String, Object>> eset = jsonObject.entrySet();
//				for (Entry<String, Object> entry : eset) {
//					System.out.println(entry.getKey() + " - " + entry.getValue());
//				}
				return jsonObject.getString("name");
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return "=== no disqualified city ===";
	}

}
