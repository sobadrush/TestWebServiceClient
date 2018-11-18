package com.ctbc.TestWebServiceClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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

public class TestThreadReadFileWithWeatherHttpGet {

	public static void main(String[] args) throws InterruptedException {
		String filePath = "E:/CTBC_workspace_phantom/TestWebServiceClient/src/main/java/_01_測試資料/台灣鄉鎮API查詢條件.txt";
		String fileTargetPath = "E:/CTBC_workspace_phantom/TestWebServiceClient/src/main/java/_01_測試資料/台灣鄉鎮API查詢結果.txt";
		//---------------------------------
		int threadNum = 22; /* init thread */
		Thread[] threadArray = getMyThreads(threadNum, filePath, fileTargetPath);
		ExecutorService es = createExecutorService(threadArray, 22 /* max thread */);

		boolean isFinshed = false;
		while (!(isFinshed = es.awaitTermination(2, TimeUnit.SECONDS))) { // 每隔2秒檢查Thread-Pool是否關閉
//			System.out.println("======= Thread-Pool尚未關閉 =======");
		}

		System.out.println("finshed >>> " + (isFinshed == true ? "Thread-Pool已關閉！" : isFinshed));
		//---------------------------------
	}

	/**
	 * 產生台灣鄉鎮API查詢資料文字檔
	 */
	public static void generateCityTestData() {
		File targetFile = new File("E:/CTBC_workspace_phantom/TestWebServiceClient/src/main/java/_01_測試資料/台灣鄉鎮API查詢條件.txt");
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

	public static String getCityName(int cityId) {
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
			HttpGet request = new HttpGet(String.format("https://works.ioa.tw/weather/api/cates/%d.json", cityId));

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
			HttpGet request = new HttpGet(String.format("https://works.ioa.tw/weather/api/cates/%d.json", cityId));

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

	public static Thread[] getMyThreads(int num, String fromfilePath, String targetPath) {

		List<String> allLinesList = getAllLineList(fromfilePath);
		int allLineNum = allLinesList.size();
		System.out.println("allLineNum >>> " + allLineNum);
		
		if (num > allLineNum) {
			throw new RuntimeException("線程數不可大於最大資料列數 : " + allLineNum);
		}
		
		File targetFile = new File(targetPath);
		if (!targetFile.exists()) {
			try {
				targetFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			FileChannel.open(Paths.get(targetPath), StandardOpenOption.WRITE).truncate(0).close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Thread[] threadArr = new Thread[num];

		int numOfThread = num;
		int perThreadToConsume = allLineNum / numOfThread;
		int residue = allLineNum % numOfThread;

		for (int n = 1 ; n <= numOfThread ; n++) {

			int start = (n - 1) * perThreadToConsume + 1;
			int end = n * perThreadToConsume;

			if (n == numOfThread) { // 最後一筆，把剩下沒跑的資料列加進去
				end += residue;
			}

			final int startFinal = start;
			final int endFinal = end;

			System.out.println(String.format("Thread-%d 負責 : %s - %s", n, startFinal, endFinal));
			System.out.println("========================");

			Thread th = new Thread(new Runnable() {
				@Override
				public void run() {
					// System.out.println(startFinal + " --- " + endFinal);

					StringBuffer sb = new StringBuffer();

					for (int cityId = startFinal ; cityId <= endFinal ; cityId++) {

						//---------------------------------------
						String cityName = getCityName(cityId);
						String strtemp = String.format("%s , CityName = %s , CityId = %d %s", Thread.currentThread().getName(), cityName, cityId, System.lineSeparator());
						sb.append(strtemp);
						List<String> townsList = getTaiwanCityTownship(cityId);
						// System.out.println(Thread.currentThread().getName() + " - cityName = " + cityName);
						
						synchronized (this.getClass()) {
							try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile, true), StandardCharsets.UTF_8));) {
								String cityInfo = String.format("%s , CityName = %s , CityId = %d %s", Thread.currentThread().getName(), cityName, cityId, System.lineSeparator());
								bw.write(cityInfo);

								for (String town : townsList) {
									String townsInfo = Thread.currentThread().getName() + " - " + town + System.lineSeparator();
									bw.write(townsInfo);
								}

							} catch (IOException e) {
								e.printStackTrace();
							}
						} // end-of synchronized
						
					}

//					try {
//						synchronized (this.getClass()) {
//							FileUtils.writeStringToFile(targetFile, sb.toString(), StandardCharsets.UTF_8, true);
//						}
//					} catch (IOException e) {
//						e.printStackTrace();
//					}

				}
			});

			//-------------------
			threadArr[n - 1] = th;
			//-------------------

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

}
