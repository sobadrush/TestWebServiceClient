package com.ctbc.test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
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
				DomesticWireParameter vo = generateDomesticVO(rowStr, props);
				// System.out.println(vo);
				domesticList.add(vo);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		//----------------------
		System.err.println("domesticList.size() >>> " + domesticList.size());
		for (DomesticWireParameter domesticWireParameterVO : domesticList) {
			System.out.println("domesticWireParameterVO = " + domesticWireParameterVO);
		}
	}

	@Test
//	@Ignore
	public void test005() {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		File ff = FileUtils.toFile(classloader.getResource("20181015105937_Z00039837_TCoE.dat"));
		System.out.println(">>> ff.exists() = " + ff.exists());
		System.out.println("########################################################################");

		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(ff));) {

			int readCount = 0;
			byte[] byteArray = new byte[938];
			while ((readCount = bis.read(byteArray)) != -1) {
//				System.err.println(">>> readCount >>> :" + rd);
				System.out.println(new String(byteArray, "utf16"));
				System.out.println("-----------------------------------------------");

				byte[] slice = Arrays.copyOfRange(byteArray, 0, 4);
				System.out.println("slice.length = " + slice.length);
				System.out.println(new String(slice, "utf16"));
				System.out.println(new String(slice));
				
				byte[] slice2 = Arrays.copyOfRange(byteArray, 0, 4);
				System.out.println("slice.length = " + slice2.length);
				System.out.println(new String(slice2).getBytes("utf16"));
				System.out.println(new String(new String(slice2).getBytes("utf16"), "utf8"));
				break;
			}

//			for (int i = 0 ; i < byteArray.length ; i++) {
//				char cc = (char) byteArray[i];
//				System.out.print(cc);
//			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	// https://stackoverflow.com/questions/26357938/detect-chinese-character-in-java
	public static String stringDataConverter(String str) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0 ; i < str.length() ;) {
			int codepoint = str.codePointAt(i);
			String tmpStr = str.substring(i, i + 1);
			i += Character.charCount(codepoint);
			if (Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN) { // 中文
				// System.out.println(tmpStr);
				sb.append(convertToFullWidth(tmpStr));
			} else if (Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.LATIN) { // 英文
				// System.out.println(tmpStr);
				sb.append(convertToHalfWidth(tmpStr));
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

}
