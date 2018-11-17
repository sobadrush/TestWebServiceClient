package com.ctbc.TestWebServiceClient;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class TestThreadReadFile {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		String filePath = "E:/CTBC_workspace_phantom/TestWebServiceClient/src/main/java/_01_測試資料/myFile.txt";
		String fileTargetPath = "E:/CTBC_workspace_phantom/TestWebServiceClient/src/main/java/_01_測試資料/targetFile.txt";
//---------------------------------
		ExecutorService es = createExecutorService(7, 5, filePath, fileTargetPath);
		es.shutdown(); // ExecutorService停止接收新的任務並且等待已經提交的任務（包含提交正在執行和提交未執行）執行完成

		boolean isFinshed = false;
		while (!(isFinshed = es.awaitTermination(2, TimeUnit.SECONDS))) { // 每隔2秒檢查Thread-Pool是否關閉
			System.out.println("======= Thread-Pool尚未關閉 =======");
		}

		System.out.println("finshed >>> " + (isFinshed == true ? "Thread-Pool已關閉！" : isFinshed));
//---------------------------------

//		List<Future<String>> futList = createExecutorServiceFutList(7, 5 ,filePath, fileTargetPath);
//		for (Future<String> future : futList) {
//			System.out.println(future.get());
//		}

	}

	public static List<Future<String>> createExecutorServiceFutList(int num, int maxNum, String fromfilePath, String targetPath) throws InterruptedException {

		File targetFile = new File(targetPath);
		if (targetFile.exists()) {
			try {
				targetFile.delete();
				targetFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		List<Future<String>> resultList = new ArrayList<Future<String>>();
		List<String> allLinesList = getAllLineList(fromfilePath);
		int allLineNum = allLinesList.size();
		System.out.println("allLineNum >>> " + allLineNum);

		int numOfThread = num;
		int perThreadToConsume = allLineNum / numOfThread;
		int residue = allLineNum % numOfThread;

//		ExecutorService es = Executors.newCachedThreadPool();
		ExecutorService es = Executors.newFixedThreadPool(maxNum);
		for (int n = 1 ; n <= numOfThread ; n++) {

			int start = (n - 1) * perThreadToConsume + 1;
			int end = n * perThreadToConsume;

			if (n == numOfThread) {
				end += residue;
			}

			final int startFinal = start;
			final int endFinal = end;

			// System.out.println(String.format("%s - %s", startFinal , endFinal));
			// System.out.println("========================");

			Future<String> fut = es.submit(new Callable<String>() {
				@Override
				public String call() throws Exception {
					for (int i = startFinal ; i <= endFinal ; i++) {
						String lineStr = allLinesList.get(i - 1);
						System.out.println(Thread.currentThread().getName() + " - " + lineStr);

						FileUtils.writeStringToFile(targetFile, lineStr, StandardCharsets.UTF_8, true);

						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					return "fuck";
				}
			});

			resultList.add(fut);
		}

		return resultList;
	}

	public static ExecutorService createExecutorService(int num, int maxNum, String fromfilePath, String targetPath) throws InterruptedException {

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
		

		List<String> allLinesList = getAllLineList(fromfilePath);
		int allLineNum = allLinesList.size();
		System.out.println("allLineNum >>> " + allLineNum);

		int numOfThread = num;
		int perThreadToConsume = allLineNum / numOfThread;
		int residue = allLineNum % numOfThread;

//		ExecutorService es = Executors.newCachedThreadPool();
		ExecutorService es = Executors.newFixedThreadPool(maxNum);
		for (int n = 1 ; n <= numOfThread ; n++) {

			int start = (n - 1) * perThreadToConsume + 1;
			int end = n * perThreadToConsume;

			if (n == numOfThread) {
				end += residue;
			}

			final int startFinal = start;
			final int endFinal = end;

			// System.out.println(String.format("%s - %s", startFinal , endFinal));
			// System.out.println("========================");

			es.submit(new Runnable() {
				@Override
				public void run() {
					for (int i = startFinal ; i <= endFinal ; i++) {
						String lineStr = allLinesList.get(i - 1);
						System.out.println(Thread.currentThread().getName() + " - " + lineStr);

						try (BufferedWriter bw = IOUtils.buffer(new FileWriter(targetFile, true));) {
							bw.write(lineStr);
							bw.newLine();

							Thread.sleep(10);

						} catch (IOException | InterruptedException e1) {
							e1.printStackTrace();
						}
//						try {
//							FileUtils.writeStringToFile(targetFile, lineStr, StandardCharsets.UTF_8 , true);
//							Thread.sleep(10);
//						} catch (IOException | InterruptedException e1) {
//							e1.printStackTrace();
//						}
					}
				}
			});
		}

		return es;
	}

	public static List<Thread> createThread(int num, String filePath) throws InterruptedException {
		List<Thread> thList = new ArrayList<>();
		List<String> allLinesList = getAllLineList(filePath);
		int allLineNum = allLinesList.size();
		System.out.println("allLineNum >>> " + allLineNum);

		int numOfThread = num;
		int perThreadToConsume = allLineNum / numOfThread;
		int residue = allLineNum % numOfThread;
		for (int n = 1 ; n <= numOfThread ; n++) {
			int start = (n - 1) * perThreadToConsume + 1;
			int end = n * perThreadToConsume;

			if (n == numOfThread) {
				end += residue;
			}

			final int startFinal = start;
			final int endFinal = end;

			// System.out.println(String.format("%s - %s", startFinal , endFinal));
			// System.out.println("========================");

			Thread th = new Thread(new Runnable() {

				@Override
				public void run() {
					for (int i = startFinal ; i <= endFinal ; i++) {
						String lineStr = allLinesList.get(i - 1);
						System.out.println(Thread.currentThread().getName() + " - " + lineStr);
					}
				}

			}, ("Thread ─ " + n));
			th.start();
			//th.join();
			thList.add(th);
		}
		return thList;
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
