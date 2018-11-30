package com.ctbc.test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.datacontract.schemas._2004._07.PatriotOfficer_DomesticWireService.DomesticWireParameter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;

import com.ctbc.util.UnicodeBOMInputStream;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestReadProp {

	private String propFileName = "mytest.properties";

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
		System.out.println(String.format("=========================== %s =========================", testCaseName.getMethodName()));
		System.out.println(String.format("============================================================="));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	@Ignore
	public void test001() throws IOException {
		String str = "TWTCoEBANCSSIT            2018101520181015SIT00002                      01O                                                                      YILMAZ,ADEM                                                                                                                                                                                                                                                                         888888888888888.00YOUSSEF AAA ABU AZIZA, SAID                                                                                                                                                                                                                                       AIDER,FARID                                                           A                                                                                                                                                                          ";
		System.out.println(str.substring(1 - 1, 2));
		System.out.println(str);
	}

	@Test
	@Ignore
	public void test002() {
		String str = stringDataConverter("x幹");
		System.out.println("str = " + str);
	}

	@Test
	@Ignore
	public void test003() throws IOException {

		String rowStr = "TWTCoEBANCSSIT            2018101520181015SIT00002                      01O                                                                      YILMAZ,ADEM                                                                                                                                                                                                                                                                         888888888888888.00YOUSSEF AAA ABU AZIZA, SAID                                                                                                                                                                                                                                       AIDER,FARID                                                           A                                                                                                                                                                          ";

		Properties props = new Properties();
		props.load(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(propFileName), StandardCharsets.UTF_8));

		DomesticWireParameter domesticWireParameter = new DomesticWireParameter();
		Set<Entry<Object, Object>> pSet = props.entrySet();
		for (Entry<Object, Object> entry : pSet) {
			String kk = (String) entry.getKey();
			String vv = (String) entry.getValue();

			int from = Integer.parseInt(vv.split(" ")[1]) - 1;
			int to = Integer.parseInt(vv.split(" ")[2]);

			if ((to - from) != Integer.parseInt(vv.split(" ")[0])) {
				throw new RuntimeException("長度不符!");
			}

			System.out.println(String.format("%-20s |  %-15s  | %-200s", kk, vv, rowStr.substring(from, to)));

			try {
				switch (kk) {
					case "beneficiaryAmount":
					case "remitAmount":
						BeanUtils.setProperty(domesticWireParameter, kk, new BigDecimal(rowStr.substring(from, to)));
						break;
					case "remitName":
					case "beneficiaryName":
					case "agentName":
						BeanUtils.setProperty(domesticWireParameter, kk, stringDataConverter(rowStr.substring(from, to)));
						break;
					default:
						BeanUtils.setProperty(domesticWireParameter, kk, rowStr.substring(from, to));
						break;
				}
			} catch (IllegalAccessException | InvocationTargetException | NumberFormatException e) {
				e.printStackTrace();
			}

		}

		System.out.println(domesticWireParameter);
	}

	@Test
	@Ignore
	public void test004() {
		Properties props = new Properties();
		try {
			props.load(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(propFileName), StandardCharsets.UTF_8));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		List<DomesticWireParameter> domesticList = new ArrayList<>();
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(
						FileUtils.getFile(classloader.getResource("20181015105937_Z00039837_TCoE.dat").getFile())), StandardCharsets.UTF_16));) {
			String rowStr = "";
			while ((rowStr = br.readLine()) != null) {
				System.out.println(rowStr);
//				DomesticWireParameter vo = generateDomesticVO(rowStr, props);
//				System.out.println(vo);
//				domesticList.add(vo);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		//----------------------
		System.out.println("@@ domesticList.size() >>> " + domesticList.size());
		for (DomesticWireParameter domesticWireParameterVO : domesticList) {
			System.out.println("domesticWireParameterVO = " + domesticWireParameterVO);
		}
	}

	/**
	 * BufferedReader 讀 UTF_16 使用 char[]
	 */
	@Test
	@Ignore
	public void test005() {

		final int BUFF_SIZE = 938 + 2;

		Properties props = new Properties();
		try {
			props.load(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(propFileName), StandardCharsets.UTF_8));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		List<DomesticWireParameter> domesticList = new ArrayList<>();
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(
						FileUtils.getFile(classloader.getResource("20181015105937_Z00039837_TCoE.dat").getFile())), StandardCharsets.UTF_16));) {

			char[] charbuff = new char[BUFF_SIZE]; //TODO : 長度改變量
			int readed = 0;
			while ((readed = br.read(charbuff)) != -1) {
				System.out.println(" >>> readed >>> " + readed);
//				char[] sliceArr = Arrays.copyOfRange(charbuff, 7 - 1, 26);
//				System.out.println(new String(sliceArr));

				DomesticWireParameter vo = generateDomesticVO(charbuff, props);
//				System.out.println("==============================================================================================");
//				System.out.println(vo);
				domesticList.add(vo);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		//----------------------
		System.out.println();
		System.out.println("@@ domesticList.size() >>> " + domesticList.size());
		for (DomesticWireParameter domesticWireParameterVO : domesticList) {
			System.out.println("domesticWireParameterVO = " + domesticWireParameterVO);
		}
	}

	/**
	 * BufferedReader 讀 BIG5 使用 char[]
	 */
	@Test
	@Ignore
	public void test006() {

		final int BUFF_SIZE = 938 + 2;

		Properties props = new Properties();
		try {
			props.load(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(propFileName), StandardCharsets.UTF_8));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		List<DomesticWireParameter> domesticList = new ArrayList<>();
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(
						FileUtils.getFile(classloader.getResource("20181015105937_Z00039837_TCoE_BIG5.dat").getFile())), Charset.forName("BIG5")));) {

			char[] charbuff = new char[BUFF_SIZE]; //TODO : 長度改變量
			int readed = 0;
			while ((readed = br.read(charbuff)) != -1) {
				System.out.println(" >>> readed >>> " + readed);
//				char[] sliceArr = Arrays.copyOfRange(charbuff, 7 - 1, 26);
//				System.out.println(new String(sliceArr));

				DomesticWireParameter vo = generateDomesticVO(charbuff, props);
//				System.out.println("==============================================================================================");
//				System.out.println(vo);
				domesticList.add(vo);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		//----------------------
		System.out.println();
		System.out.println("@@ domesticList.size() >>> " + domesticList.size());
		for (DomesticWireParameter domesticWireParameterVO : domesticList) {
			System.out.println("domesticWireParameterVO = " + domesticWireParameterVO);
		}
	}

	/**
	 * BufferedInputStream 讀 BIG5 使用 byte[]
	 */
	@Test
//	@Ignore
	public void test007() {

		final int BUFF_SIZE = 938 + 2;

		Properties props = new Properties();
		try {
			props.load(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(propFileName), StandardCharsets.UTF_8));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
//		File ff = FileUtils.toFile(classloader.getResource("20181015105937_Z00039837_TCoE.dat"));
//		File ff = FileUtils.toFile(classloader.getResource("20181015105937_Z00039837_TCoE_BIG5.dat"));
		File ff = FileUtils.toFile(classloader.getResource("20181015105937_Z00039837_TCoE_UTF8.dat"));
		System.out.println(">>> ff.exists() = " + ff.exists());
		System.out.println("########################################################################");

		List<DomesticWireParameter> domesticList = new ArrayList<>();
		
		try (BufferedInputStream bis = new BufferedInputStream(new UnicodeBOMInputStream(new FileInputStream(ff)).skipBOM());) {

			int readed = 0;
			byte[] byteArray = new byte[BUFF_SIZE];
			while ((readed = bis.read(byteArray)) != -1) {
				System.err.println(">>> readed >>> :" + readed);

//				byte[] slice = Arrays.copyOfRange(byteArray, 1-1, 2);
//				System.out.println("slice.length = " + slice.length);
//				System.out.println(new String(slice, StandardCharsets.US_ASCII));
//				System.out.println(new String(slice, StandardCharsets.UTF_16));
//				System.out.println(new String(slice));

//				DomesticWireParameter vo = generateDomesticVO(byteArray, props, "BIG5");
//				DomesticWireParameter vo = generateDomesticVO(byteArray, props, "UTF-16", "UTF-16");
//				DomesticWireParameter vo = generateDomesticVO(byteArray, props, "BIG5", "BIG5");
				DomesticWireParameter vo = generateDomesticVO(byteArray, props, "UTF8", "UTF8");
				domesticList.add(vo);

//				break;
			}

			//----------------------
			System.out.println();
			System.out.println("@@ domesticList.size() >>> " + domesticList.size());
			for (DomesticWireParameter domesticWireParameterVO : domesticList) {
				System.out.println("domesticWireParameterVO = " + domesticWireParameterVO);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	@Test
	@Ignore
	public void test008() {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
//		File ff = FileUtils.toFile(classloader.getResource("20181015105937_Z00039837_TCoE.dat"));
		File ff = FileUtils.toFile(classloader.getResource("20181015105937_Z00039837_TCoE_BIG5.dat"));
		System.out.println(">>> ff.exists() = " + ff.exists());
		System.out.println("########################################################################");

		try (InputStreamReader isr = new InputStreamReader(new FileInputStream(ff), StandardCharsets.US_ASCII)) {

			char[] charBuff = new char[938 + 2];
			int readed;
			while ((readed = isr.read(charBuff)) != -1) {
				// System.out.println("readed = " + readed);
//				System.out.flush();
//				for (char c : charBuff) {
//					System.out.print(c);
//				}

				char[] slice = Arrays.copyOfRange(charBuff, 3 - 1, 6);
				System.out.println("slice.length = " + slice.length);
//				System.out.println(new String(slice, StandardCharsets.UTF_16LE));
				System.out.println(new String(slice));

//				break;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Test
	@Ignore
	public void test009() {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		File ff = FileUtils.toFile(classloader.getResource("20181015105937_Z00039837_TCoE.dat"));
//		File ff = FileUtils.toFile(classloader.getResource("20181015105937_Z00039837_TCoE_BIG5.dat"));
		System.out.println(">>> ff.exists() = " + ff.exists());
		System.out.println("########################################################################");

		try (InputStreamReader isr = new InputStreamReader(new FileInputStream(ff), StandardCharsets.UTF_16)) {

			char[] charBuff = new char[938 + 2];
			int readed;
			while ((readed = isr.read(charBuff)) != -1) {
				// System.out.println("readed = " + readed);
//				System.out.flush();
//				for (char c : charBuff) {
//					System.out.print(c);
//				}

				char[] slice = Arrays.copyOfRange(charBuff, 3, 6);
				System.out.println("slice.length = " + slice.length);
//				System.out.println(new String(slice, StandardCharsets.UTF_16LE));
				System.out.println(new String(slice));

//				break;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// https://stackoverflow.com/questions/26357938/detect-chinese-character-in-java
	// https://www.cnblogs.com/zztt/p/3427452.html
	public static String stringDataConverter(String str) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0 ; i < str.length() ;) {
			int codepoint = str.codePointAt(i);
			String tmpStr = str.substring(i, i + 1);
			i += Character.charCount(codepoint);
			if (Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN) { // 中文
				// System.out.println(tmpStr);
				sb.append(convertToFullWidth(tmpStr));// 轉全形
			} else if (Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.LATIN) { // 英文
				// System.out.println(tmpStr);
				sb.append(convertToHalfWidth(tmpStr));// 轉半形
			} else if (Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.COMMON) { // 空格 逗號...
				// System.out.println(tmpStr);
				sb.append(convertToHalfWidth(tmpStr));// 轉半形
			}
		}
		return sb.toString();
	}

	/**
	 * http://mactruecolor.blogspot.com/2011/03/blog-post.html
	 * 全型轉半型
	 * 
	 * @param source
	 * @return
	 */
	public static String convertToHalfWidth(final String source) {
		if (null == source) {
			return null;
		}

		char[] charArray = source.toCharArray();
		for (int i = 0 ; i < charArray.length ; i++) {
			int ic = (int) charArray[i];

			if (ic >= 65281 && ic <= 65374) {
				charArray[i] = (char) (ic - 65248);
			} else if (ic == 12288) {
				charArray[i] = (char) 32;
			}

		}
		return new String(charArray);
	}

	/**
	 * http://mactruecolor.blogspot.com/2011/03/blog-post.html
	 * 半型轉全型
	 * 
	 * @param source
	 * @return
	 */
	public static String convertToFullWidth(final String source) {
		if (null == source) {
			return null;
		}

		char[] charArray = source.toCharArray();
		for (int i = 0 ; i < charArray.length ; i++) {
			int ic = (int) charArray[i];

			if (ic >= 33 && ic <= 126) {
				charArray[i] = (char) (ic + 65248);
			} else if (ic == 32) {
				charArray[i] = (char) 12288;
			}

		}
		return new String(charArray);
	}

	public static DomesticWireParameter generateDomesticVO(String rowStr, Properties props) {
		DomesticWireParameter domesticWireParameter = new DomesticWireParameter();
		Set<Entry<Object, Object>> pSet = props.entrySet();
		for (Entry<Object, Object> entry : pSet) {
			String kk = (String) entry.getKey();
			String vv = (String) entry.getValue();

			int from = Integer.parseInt(vv.split(" ")[1]) - 1;
			int to = Integer.parseInt(vv.split(" ")[2]);

			if ((to - from) != Integer.parseInt(vv.split(" ")[0])) {
				throw new RuntimeException("長度不符!");
			}

			System.out.println(String.format("%-20s |  %-15s  | %-200s", kk, vv, rowStr.substring(from, to)));

			try {
				switch (kk) {
					case "beneficiaryAmount":
					case "remitAmount":
						BeanUtils.setProperty(domesticWireParameter, kk, new BigDecimal(rowStr.substring(from, to)));
						break;
					case "remitName":
					case "beneficiaryName":
					case "agentName":
						BeanUtils.setProperty(domesticWireParameter, kk, stringDataConverter(rowStr.substring(from, to)));
						break;
					default:
						BeanUtils.setProperty(domesticWireParameter, kk, rowStr.substring(from, to));
						break;
				}
			} catch (IllegalAccessException | InvocationTargetException | NumberFormatException e) {
				e.printStackTrace();
			}

		}
		return domesticWireParameter;
	}

	public static DomesticWireParameter generateDomesticVO(byte[] rowByTes, Properties props, String encoding) {
		DomesticWireParameter domesticWireParameter = new DomesticWireParameter();
		Set<Entry<Object, Object>> pSet = props.entrySet();
		for (Entry<Object, Object> entry : pSet) {
			String kk = (String) entry.getKey();
			String vv = (String) entry.getValue();

			int from = Integer.parseInt(vv.split(" ")[1]) - 1;
			int to = Integer.parseInt(vv.split(" ")[2]);

			if ((to - from) != Integer.parseInt(vv.split(" ")[0])) {
				throw new RuntimeException("長度不符!");
			}

			System.out.println(String.format("%-20s |  %-15s |  %-200s", kk, vv, new String(rowByTes, Charset.forName(encoding)).trim()));

			try {
				switch (kk) {
					case "beneficiaryAmount":
					case "remitAmount":
						BeanUtils.setProperty(domesticWireParameter, kk, new BigDecimal(new String(Arrays.copyOfRange(rowByTes, from, to), Charset.forName(encoding)).trim()));
						break;
					case "remitName":
					case "beneficiaryName":
					case "agentName":
						BeanUtils.setProperty(domesticWireParameter, kk, stringDataConverter(new String(Arrays.copyOfRange(rowByTes, from, to), Charset.forName(encoding)).trim()));
						break;
					default:
						BeanUtils.setProperty(domesticWireParameter, kk, new String(Arrays.copyOfRange(rowByTes, from, to), Charset.forName(encoding)).trim());
						break;
				}
			} catch (IllegalAccessException | InvocationTargetException | NumberFormatException e) {
				e.printStackTrace();
			}

		}
		return domesticWireParameter;
	}

	public static DomesticWireParameter generateDomesticVO(byte[] rowBytes, Properties props, String fromEncoding, String toEncoding) {
		DomesticWireParameter domesticWireParameter = new DomesticWireParameter();
		
		Set<Entry<Object, Object>> pSet = props.entrySet();
		for (Entry<Object, Object> entry : pSet) {
			String kk = (String) entry.getKey();
			String vv = (String) entry.getValue();

			int from = Integer.parseInt(vv.split(" ")[1]) - 1;
			int to = Integer.parseInt(vv.split(" ")[2]);
			int difference = Integer.parseInt(vv.split(" ")[0]);
			
			if ((to - from) != difference) {
				throw new RuntimeException("長度不符!");
			}

			try {
				String thName = Thread.currentThread().getName();
				// System.out.println(String.format("%s - %-20s \t | %-10s \t | %-200s", thName , kk , vv, new String(new String(rowBytes, 0, rowBytes.length, fromEncoding).trim().getBytes(toEncoding), toEncoding)));
				System.out.println(String.format("%s - %-20s \t | %-10s \t | %-200s", thName , kk , vv, new String(new String(rowBytes, from, difference, fromEncoding).trim().getBytes(toEncoding), toEncoding)));
				
				switch (kk) {
					case "beneficiaryAmount":
					case "remitAmount":
						BeanUtils.setProperty(domesticWireParameter, kk, new BigDecimal(new String(new String(rowBytes, from, difference, fromEncoding).trim().getBytes(toEncoding), toEncoding)));
						break;
					case "remitName":
					case "beneficiaryName":
					case "agentName":
						BeanUtils.setProperty(domesticWireParameter, kk, stringDataConverter(new String(new String(rowBytes, from, difference, fromEncoding).trim().getBytes(toEncoding), toEncoding)));
						break;
					default:
						BeanUtils.setProperty(domesticWireParameter, kk, new String(new String(rowBytes, from, difference, fromEncoding).trim().getBytes(toEncoding), toEncoding));
						break;
				}
			} catch (IllegalAccessException | InvocationTargetException | NumberFormatException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		}
		return domesticWireParameter;
	}

	public static DomesticWireParameter generateDomesticVO(char[] rowChars, Properties props) {
		DomesticWireParameter domesticWireParameter = new DomesticWireParameter();
		Set<Entry<Object, Object>> pSet = props.entrySet();
		for (Entry<Object, Object> entry : pSet) {
			String kk = (String) entry.getKey();
			String vv = (String) entry.getValue();

			int from = Integer.parseInt(vv.split(" ")[1]) - 1;
			int to = Integer.parseInt(vv.split(" ")[2]);

			if ((to - from) != Integer.parseInt(vv.split(" ")[0])) {
				throw new RuntimeException("長度不符!");
			}

			System.out.println(String.format("%-20s |  %-15s  | %-200s", kk, vv, String.valueOf(Arrays.copyOfRange(rowChars, 0, rowChars.length))));

			try {
				switch (kk) {
					case "beneficiaryAmount":
					case "remitAmount":
						BeanUtils.setProperty(domesticWireParameter, kk, new BigDecimal(Arrays.copyOfRange(rowChars, from, to)));
						break;
					case "remitName":
					case "beneficiaryName":
					case "agentName":
						BeanUtils.setProperty(domesticWireParameter, kk, stringDataConverter(String.valueOf(Arrays.copyOfRange(rowChars, from, to))));
						break;
					default:
						BeanUtils.setProperty(domesticWireParameter, kk, String.valueOf(Arrays.copyOfRange(rowChars, from, to)));
						break;
				}
			} catch (IllegalAccessException | InvocationTargetException | NumberFormatException e) {
				e.printStackTrace();
			}

		}
		return domesticWireParameter;
	}

}
