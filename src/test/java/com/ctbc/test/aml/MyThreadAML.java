package com.ctbc.test.aml;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.xml.rpc.ServiceException;

import org.datacontract.schemas._2004._07.PatriotOfficer_DomesticWireService.DomesticWireParameter;

import com.ctbc.test.TestReadProp;

import net.patriotofficer.DomesticWireService.DomesticWireServiceLocator;
import net.patriotofficer.DomesticWireService.IDomesticWireService;

public class MyThreadAML implements Runnable {

	private BufferedInputStream bis = null;
	private BufferedWriter bw = null;
	private Properties props = null;
	private DomesticWireServiceLocator domesticWireServiceLocator = null; // AML WebService
	private IDomesticWireService domesticSvc = null; // AML WebService
	
	{
		this.domesticWireServiceLocator = new DomesticWireServiceLocator();// 國內匯款
		try {
			this.domesticSvc = domesticWireServiceLocator.getBasicHttpBinding_IDomesticWireService();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public MyThreadAML() {
		super();
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
				// System.err.println(">>> readed >>> :" + readed);
				DomesticWireParameter paramsVO = TestReadProp.generateDomesticVO(byteArray, props, StandardCharsets.UTF_8.name(), StandardCharsets.UTF_8.name());
				System.out.println(String.format("=================================== %n%s - %-10s", Thread.currentThread().getName() , paramsVO));
				
				bw.write(new String(byteArray).trim());
				bw.newLine();
				
//				DomesticWireResult callBackData = domesticSvc.submitDomesticWire(paramsVO);
//				System.out.println(String.format("callBackData >>> %s", callBackData));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
