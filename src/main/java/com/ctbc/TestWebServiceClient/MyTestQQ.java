package com.ctbc.TestWebServiceClient;

import java.rmi.RemoteException;

import cn.com.webservice.qq.QqOnlineWebServiceSoapProxy;

public class MyTestQQ {

	public static void main(String[] args) throws RemoteException {
		String endPoint = "http://www.webxml.com.cn/webservices/qqOnlineWebService.asmx?wsdl";
		QqOnlineWebServiceSoapProxy qqPorxy = new QqOnlineWebServiceSoapProxy(endPoint);
		
		String result = qqPorxy.qqCheckOnline("379839335");
		System.out.println(" result = " + result);
	}

}
