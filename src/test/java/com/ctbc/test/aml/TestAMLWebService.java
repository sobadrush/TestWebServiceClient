package com.ctbc.test.aml;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.rpc.ServiceException;

import org.apache.commons.io.FileUtils;
import org.datacontract.schemas._2004._07.PatriotOfficer_DomesticWireService.DomesticWireParameter;
import org.datacontract.schemas._2004._07.PatriotOfficer_DomesticWireService.DomesticWireResult;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;

import com.ctbc.test.TestReadProp;
import com.ctbc.util.UnicodeBOMInputStream;

import net.patriotofficer.DomesticWireService.DomesticWireServiceLocator;
import net.patriotofficer.DomesticWireService.IDomesticWireService;
import net.patriotofficer.DomesticWireService.IDomesticWireServiceProxy;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAMLWebService {


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
	@Ignore
	public void test001() {
		DomesticWireServiceLocator domesticWireServiceLocator = new DomesticWireServiceLocator();// 國內匯款
		try {
			IDomesticWireService domesticSvc = domesticWireServiceLocator.getBasicHttpBinding_IDomesticWireService();
			DomesticWireParameter params = getTestAmlParameter();
			DomesticWireResult callBackData = domesticSvc.submitDomesticWire(params);
			System.out.println("callBackData  = " + callBackData);
		} catch (ServiceException | RemoteException e) {
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void test002() {
		String endPoint = "http://172.24.30.55:4445/DomesticWire.svc?wsdl";
		IDomesticWireServiceProxy iDomesticWireServiceProxy = new IDomesticWireServiceProxy(endPoint);
		DomesticWireParameter params = getTestAmlParameter();

		DomesticWireResult callBackData;
		try {
			callBackData = iDomesticWireServiceProxy.getIDomesticWireService().submitDomesticWire(params);
			System.out.println("callBackData  = " + callBackData);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		// 1. 開檔
		// 2. Output File
//		Thread th = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				// 2.讀檔
//				// 3.發web
//				// 4.寫到 output.txt
//			}
//		});
//		th.start();
	}

	@Test
//	@Ignore
	public void test003() {
		String endPoint = "http://172.24.30.55:4445/DomesticWire.svc?wsdl";
		IDomesticWireServiceProxy iDomesticWireServiceProxy = new IDomesticWireServiceProxy(endPoint);
		//----------------------------------------------------------------------------
		final String propFileName = "mytest.properties";
		
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
				DomesticWireParameter vo = TestReadProp.generateDomesticVO(byteArray, props, "UTF8", "UTF8");
				domesticList.add(vo);
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
		//----------------------------------------------------------------------------
		DomesticWireParameter params = domesticList.get(0);
//		params.setReferenceNumber("049520180723001");
		
//		DomesticWireParameter params = getTestAmlParameter();
		DomesticWireResult callBackData;
		try {
			callBackData = iDomesticWireServiceProxy.getIDomesticWireService().submitDomesticWire(params);
			System.out.println("callBackData  = " + callBackData);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}
	
	private static DomesticWireParameter getTestAmlParameter() {
		DomesticWireParameter params = new DomesticWireParameter();
		params.setAgentID("00039837");
		params.setAgentName("Peter");
		params.setBeneficiaryAccountNo("A04951234567890");
		params.setBeneficiaryAddress("TW");
		params.setBeneficiaryAmount(new BigDecimal("0"));
		params.setBeneficiaryBankID("0495");
		params.setBeneficiaryBankName("TW");
		params.setBeneficiaryCurrency("TWD");
		params.setBeneficiaryID("A123456789");
		params.setBeneficiaryName("Jhon");
		params.setBranchNo("0495");
		params.setCountry("TW");
		params.setDataDate("20180723");
		params.setDocumentNo("");
		params.setReferenceNumber("049520180723001");
		params.setRemarks("");
		params.setRemitAccountNo("");
		params.setRemitAddress("");
		params.setRemitAmount(new BigDecimal("0")); // exception
		params.setRemitBankID("");
		params.setRemitBankName("");
		params.setRemitCurrency("");
		params.setRemitID("");
		params.setRemitName("");
		params.setScanListType("");
		params.setSourceSystem("BANCS");
		params.setTxnDate("20180722");
		params.setTxnDirection(0);
		params.setTxnNo("049520180723001");
		params.setTxnType("I");
		return params;
	}

}
