package com.ctbc.TestWebServiceClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.alibaba.fastjson.JSONObject;

public class TestThreadReadFileWithWeatherHttpGet2 {

	public static void main(String[] args) throws InterruptedException {

		String projectDir = System.getProperty("user.dir");

		String filePath = projectDir + "/src/main/java/_01_測試資料/台灣鄉鎮API查詢條件.txt";
		String fileTargetPath = projectDir + "/src/main/java/_01_測試資料/台灣鄉鎮API查詢結果.txt";

		try {
			FileChannel.open(Paths.get(fileTargetPath), StandardOpenOption.WRITE).truncate(0).close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//---------------------------------

		File srcFile = new File(filePath);
		File targetFile = new File(fileTargetPath);
		
		if (!targetFile.exists()) {
			try {
				targetFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		//---------------------------------
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(srcFile), StandardCharsets.UTF_8));
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile, true), StandardCharsets.UTF_8));) {
			int threadNum = 5; /* init thread */
			Thread[] threadArray = getMyThreads(threadNum, br, bw);
			ExecutorService es = createExecutorService(threadArray, 5 /* max thread */);

			boolean isFinshed = false;
			while (!(isFinshed = es.awaitTermination(1, TimeUnit.SECONDS))) { // 每隔2秒檢查Thread-Pool是否關閉
				System.out.println("======= Thread-Pool尚未關閉 =======");
			}

			if (isFinshed == true) {
				System.out.println(" Finshed >>> Thread-Pool已關閉！");
				bw.close();
				br.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			srcFile = null;
			targetFile = null;
		}
		//---------------------------------
	}

	public static String getCityName(int cityId) {
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
			HttpGet request = new HttpGet(String.format("http://works.ioa.tw/weather/api/cates/%d.json", cityId));

			request.addHeader("Content-type", "application/json; charset=utf-8");
			HttpResponse response = httpClient.execute(request);

			String resCodeStr = String.format("<<< [%s] getCityName(), Response Code : %s", Thread.currentThread().getName(), response.getStatusLine().getStatusCode());
			System.err.println(resCodeStr);

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

	public static List<String> getTaiwanCityTownship(int cityId) {
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

	public static Thread[] getMyThreads(int numOfThread, BufferedReader br, BufferedWriter bw) {
		Thread[] threadArr = new Thread[numOfThread];
		for (int n = 1 ; n <= numOfThread ; n++) {
			Thread th = new Thread(new MyThreadQ(br, bw));
			threadArr[n - 1] = th;
		}
		return threadArr;
	}

	public static ExecutorService createExecutorService(Thread[] threadArr, Integer maxnum) {

		int threadNum = threadArr.length;
		int maxThreadNum = (maxnum == null) ? (threadNum + 3) : maxnum;

//		ExecutorService es = Executors.newCachedThreadPool();
		ExecutorService es = Executors.newFixedThreadPool(maxThreadNum);

		for (int i = 0 ; i < threadNum ; i++) {
			es.submit(threadArr[i]);
		}

		es.shutdown(); // ExecutorService停止接收新的任務並且等待已經提交的任務（包含提交正在執行和提交未執行）執行完成
		return es;
	}

	public static List<String> getAllLineList(String fromPath) {
		List<String> linesList = new ArrayList<>();
		try {
			linesList = FileUtils.readLines(new File(fromPath), StandardCharsets.UTF_8);
//			System.out.println(linesList.size());
//			for (String line : linesList) {
//				System.out.println(line);
//			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.synchronizedList(linesList);

	}

	/**
	 * 產生台灣鄉鎮API查詢資料文字檔
	 */
	public static void generateCityTestData() {
		File targetFile = new File(System.getProperty("user.dir") + "/src/main/java/_01_測試資料/台灣鄉鎮API查詢條件.txt");
		if (!targetFile.exists()) {
			try {
				targetFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			FileChannel.open(targetFile.toPath(), StandardOpenOption.WRITE).truncate(0).close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 1 ; i <= 22 ; i++) {
			String cityName = getCityName(i);
			String lineStr = String.format("%03d - %s\n", i, cityName);
			try {
				FileUtils.writeStringToFile(targetFile, lineStr, StandardCharsets.UTF_8, true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
