package com.ctbc.test.aml;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Properties;

import javax.xml.rpc.ServiceException;

import org.datacontract.schemas._2004._07.PatriotOfficer_DomesticWireService.DomesticWireParameter;
import org.datacontract.schemas._2004._07.PatriotOfficer_DomesticWireService.DomesticWireResult;

import com.ctbc.test.TestReadProp;

import net.patriotofficer.DomesticWireService.DomesticWireServiceLocator;
import net.patriotofficer.DomesticWireService.IDomesticWireService;

public class MyThreadAML implements Runnable {

	private static int nn = 1;

	private BufferedInputStream bis = null;
	private BufferedWriter bw = null;
	private Properties props = null;

	public MyThreadAML() {

	}

	public MyThreadAML(BufferedInputStream bis, BufferedWriter bw) {
		this.bis = bis;
		this.bw = bw;
	}
	
	public MyThreadAML(BufferedInputStream bis, BufferedWriter bw, Properties props) {
		this.bis = bis;
		this.bw = bw;
		this.props = props;
	}

	@Override
	public void run() {
		final int BUFF_SIZE = 938 + 2;
		try {

			int readed = 0;
			byte[] byteArray = new byte[BUFF_SIZE];
			while ((readed = bis.read(byteArray)) != -1) {
				System.err.println(">>> readed >>> :" + readed);
				DomesticWireParameter paramsVO = TestReadProp.generateDomesticVO(byteArray, props, "UTF8", "UTF8");
//				System.out.println(Thread.currentThread().getName() + " >>> \n " + vo);
				
				DomesticWireServiceLocator domesticWireServiceLocator = new DomesticWireServiceLocator();// 國內匯款
				try {
					IDomesticWireService domesticSvc = domesticWireServiceLocator.getBasicHttpBinding_IDomesticWireService();
					DomesticWireResult callBackData = domesticSvc.submitDomesticWire(paramsVO);
					System.out.println("callBackData  = " + callBackData);
				} catch (ServiceException | RemoteException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
