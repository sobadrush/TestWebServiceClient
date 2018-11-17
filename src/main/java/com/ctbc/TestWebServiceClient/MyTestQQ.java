package com.ctbc.TestWebServiceClient;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import cn.com.webservice.qq.QqOnlineWebServiceLocator;
import cn.com.webservice.qq.QqOnlineWebServiceSoap;

public class MyTestQQ {

	public static void main(String[] args) throws RemoteException {
//		String endPoint = "http://www.webxml.com.cn/webservices/qqOnlineWebService.asmx?wsdl";
//		QqOnlineWebServiceSoapProxy qqPorxy = new QqOnlineWebServiceSoapProxy(endPoint);
//		
//		String result = qqPorxy.qqCheckOnline("379839335");
//		System.out.println(" result = " + result);
		
		//---------------------------------------------
		QqOnlineWebServiceLocator wsLocator = new QqOnlineWebServiceLocator();
		System.out.println(" wsLocator.getServiceName() >>> " + wsLocator.getServiceName());
		try {
			QqOnlineWebServiceSoap wsSoap = wsLocator.getqqOnlineWebServiceSoap();
			String result = wsSoap.qqCheckOnline("379839335");
			System.out.println(" result = " + result);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		
	}

}
