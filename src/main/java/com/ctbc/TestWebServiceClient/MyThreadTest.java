package com.ctbc.TestWebServiceClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

class Cat implements Runnable {

	private int num;

	@Override
	public void run() {
		System.out.println("num = " + num++);
	}

//	public static void main(String[] args) {
//		File f = new File("E:\\CTBC_workspace_phantom\\TestWebServiceClient\\src\\main\\java\\_01_測試資料\\myFile.txt");
//		try(BufferedWriter buffWt = new BufferedWriter(new FileWriter(f));) {
//			for (int i = 1; i <= 1000; i++) {
//				buffWt.write(String.format("哈哈哈哈哈-%05d", i));
//				buffWt.newLine();
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}

class MyTxtReader implements Runnable {

	private int startedLineNum;
	private List<String> strlist;

	private File f;

	public MyTxtReader() {
	}

	public MyTxtReader(File f, int startedLineNum) {
		this.f = f;
		this.startedLineNum = startedLineNum;
	}

	@Override
	public void run() {
		
		try (BufferedReader br = new BufferedReader(new FileReader(f));) {
			String strLine = null;
			
//			for (int i = 0 ; i < array.length ; i++) {
//				
//			}
			
			while ((strLine = br.readLine()) != null) {
				System.out.println(strLine);
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Override
//	public void run() {
//		String line = null;
//		int count = 0;
//		while (true) {
//			//System.out.println(Thread.currentThread().getName());
//			this.list = new ArrayList<String>();
//			synchronized (br) {
//				try {
//					while ((line = br.readLine()) != null) {
//						if (count < 15) {
//							list.add(line);
//							count++;
//						} else {
//							list.add(line);
//							count = 0;
//							break;
//						}
//					}
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//			try {
//				Thread.sleep(100);
//				display(this.list);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//
//			if (line == null) {
//				break;
//			}
//		}
//
//	}
//
//	public void display(List<String> list) {
//		for (String str : list) {
//			System.out.println(str);
//		}
//		System.out.println(list.size());
//	}

}

public class MyThreadTest {

	private static ThreadPoolTaskExecutor getThreadPool(int initNum) {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setQueueCapacity(10000); // 当池子大小小于corePoolSize，就新建线程，并处理请求
		taskExecutor.setCorePoolSize(initNum);  // 当池子大小等于corePoolSize，把请求放入workQueue中，池子里的空闲线程就去workQueue中取任务并处理
		taskExecutor.setMaxPoolSize(10); // 当workQueue放不下任务时，就新建线程入池，并处理请求，如果池子大小撑到了maximumPoolSize，就用RejectedExecutionHandler来做拒绝处理 
		taskExecutor.setKeepAliveSeconds(60);  // 当池子的线程数大于corePoolSize时，多余的线程会等待keepAliveTime长时间，如果无请求可处理就自行销毁
		taskExecutor.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy());
		taskExecutor.initialize();
		return taskExecutor;
	}

	public static void main(String[] args) throws InterruptedException {

//		Thread t1 = new Thread(new MyTxtReader(), "A");
//		Thread t2 = new Thread(new MyTxtReader(), "B");
//		t1.start();
//		t2.start();

//		ThreadPoolTaskExecutor taskExecutor = getThreadPool(5);
//		File myFile = new File("E:\\CTBC_workspace_phantom\\TestWebServiceClient\\src\\main\\java\\_01_測試資料\\myFile.txt");
//		MyTxtReader myTxtReader = new MyTxtReader(myFile);
//		taskExecutor.execute(myTxtReader);
//		System.out.println("123");

//		Cat kitty = new Cat();
//		while (true) {
//			taskExecutor.execute(kitty);
//			Thread.sleep(1000);
//		}
	}

//	public static void main(String[] args) throws RemoteException, ServiceException {
//		WeatherWebService weatherWebService = new WeatherWebServiceLocator();
//		WeatherWebServiceSoap weatherWebServiceSoap = weatherWebService.getWeatherWebServiceSoap();
////		String[] strArray = weatherWebServiceSoap.getWeatherbyCityName("福州");
////		List<String> slist = Arrays.asList(strArray);;
////		int i = 0;
////		for (String str : slist) {
////			System.out.println(i++ + ": " + str);
////		}
//		
//		String[] supCities = weatherWebServiceSoap.getSupportCity("北京");
//		for (String city : supCities) {
//			System.out.println(" city = " + city);
//		}
//	}

}
