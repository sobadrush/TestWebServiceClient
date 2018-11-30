package com.ctbc.test.aml;

import java.io.BufferedInputStream;
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
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAMLWebServiceThread {


	@org.junit.Rule 
	public org.junit.rules.TestName testCaseName = new TestName();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		System.out.println(String.format("============================================================="));
		System.out.println(String.format("========================== %s ========================", testCaseName.getMethodName()));
		System.out.println(String.format("============================================================="));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
//	@Ignore
	public void test001() throws InterruptedException {
		
		final String encoding = "BIG5";
//		final String encoding = "UTF-8";
		
		final String propFileName = "mytest.properties";
		final String projectDir = System.getProperty("user.dir");
//		final String filePath = projectDir + "/src/test/resources/20181015105937_Z00039837_TCoE_UTF8.dat";
//		final String filePath = projectDir + "/src/test/resources/20181015105937_Z00039837_TCoE_BIG5.dat";
		final String filePath = projectDir + "/src/test/resources/20181015105937_Z00039837_TCoE_Big5_CHI.dat";
		final String fileTargetPath = projectDir + "/src/test/resources/AML測試output.txt";

		// 清空 AML測試output.txt
		try {
			FileChannel.open(Paths.get(fileTargetPath), StandardOpenOption.WRITE).truncate(0).close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//---------------------------------
		
		Properties props = new Properties();
		try {
			props.load(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(propFileName), StandardCharsets.UTF_8));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		File srcFile = new File(filePath);
		File targetFile = new File(fileTargetPath);

		if (!targetFile.exists()) {
			try {
				targetFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		BufferedInputStream bis = null;
		BufferedWriter bw = null;
		try {
			//bis = new BufferedInputStream(new UnicodeBOMInputStream(new FileInputStream(srcFile)).skipBOM());// eclipse error : https://stackoverrun.com/cn/q/9393924
			bis = new BufferedInputStream(new FileInputStream(srcFile));
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile, true), StandardCharsets.UTF_8));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//---------------------------------
		try {
			int threadNum = 5; /* init thread */
			Thread[] threadArray = getMyThreads(threadNum, bis, bw, props, encoding);
			ExecutorService es = createExecutorService(threadArray, 5 /* max thread */);

			boolean isFinshed = false;
			while (!(isFinshed = es.awaitTermination(1, TimeUnit.SECONDS))) { // 每隔1秒檢查Thread-Pool是否關閉
//				System.out.println(String.format("======= %s - Thread-Pool尚未關閉 =======", Thread.currentThread().getName()));
			}
			
			if (isFinshed == true) {
				System.out.println(String.format("======= %s - Finshed >>> Thread-Pool已關閉！ =======", Thread.currentThread().getName()));
				bw.close();
				bis.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			srcFile = null;
			targetFile = null;
		}
		//---------------------------------
	}

	
	public static Thread[] getMyThreads(int numOfThread, BufferedInputStream bis, BufferedWriter bw, Properties props, String encoding) {
		MyThreadAML amlRunnableObj = new MyThreadAML(bis, bw, props, encoding);
		Thread[] threadArr = new Thread[numOfThread];
		for (int n = 1 ; n <= numOfThread ; n++) {
			Thread th = new Thread(amlRunnableObj);// 自訂的Runnable類
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
}









