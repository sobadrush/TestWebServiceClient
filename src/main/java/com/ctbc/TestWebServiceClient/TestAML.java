package com.ctbc.TestWebServiceClient;

import java.math.BigDecimal;
import java.rmi.RemoteException;

import org.datacontract.schemas._2004._07.PatriotOfficer_DomesticWireService.DomesticWireParameter;
import org.datacontract.schemas._2004._07.PatriotOfficer_DomesticWireService.DomesticWireResult;

import net.patriotofficer.DomesticWireService.IDomesticWireServiceProxy;

public class TestAML {

	public static void main(String[] args) throws RemoteException {
		String endPoint = "http://172.24.30.55:4445/DomesticWire.svc?wsdl";
		IDomesticWireServiceProxy iDomesticWireServiceProxy = new IDomesticWireServiceProxy(endPoint);

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
		params.setRemitAmount(new BigDecimal("0"));
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
		
		DomesticWireResult callBackData = iDomesticWireServiceProxy.getIDomesticWireService().submitDomesticWire(params);
		System.out.println("callBackData  = " + callBackData);
		
		// 1. 開檔
		// 2. Output File
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				// 2.讀檔
				// 3.發web
				// 4.寫到 output.txt
			}
		});
		th.start();
	}

}
