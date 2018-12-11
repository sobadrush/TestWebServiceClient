package com.ctbc.test.aml;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Properties;

import javax.xml.rpc.ServiceException;

import org.datacontract.schemas._2004._07.PatriotOfficer_DomesticWireService.DomesticWireParameter;
import org.datacontract.schemas._2004._07.PatriotOfficer_DomesticWireService.DomesticWireResult;

import com.ctbc.test.TestReadProp;

import net.patriotofficer.DomesticWireService.DomesticWireServiceLocator;
import net.patriotofficer.DomesticWireService.IDomesticWireService;

public class MyThreadAML implements Runnable {

	private BufferedInputStream bis = null;
	private BufferedWriter bw = null;
	private Properties props = null;
	private String encoding = null;
	private Integer totalCount = 0;
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

	public MyThreadAML(BufferedInputStream bis, BufferedWriter bw, Properties props, String encoding) {
		this.bis = bis;
		this.bw = bw;
		this.props = props;
		this.encoding = encoding;
	}

	@Override
	public void run() {
		try {

			while (true) { // while 迴圈讀取總筆數 ─ 讀取第一列，直到任一Thread將 this.totalCount 改成不為 0 時，break
				synchronized (this.totalCount) {
					if (this.totalCount != 0) {
						break;
					} else {
						final byte[] byteArrayLine1 = new byte[10 + 2];
						int readedLine1 = bis.read(byteArrayLine1);
						this.totalCount = Integer.parseInt(new String(byteArrayLine1, encoding).trim());
						System.out.println(String.format(" %s , 讀取byte數：%d ,總筆數：%d", Thread.currentThread().getName(), readedLine1, this.totalCount));
					}
				}
			}

			final byte[] byteBuff = new byte[938 + 2];
			int readed = 0;
			while ((readed = bis.read(byteBuff)) != -1) {

				System.out.println(">>> readed >>> :" + readed);

				DomesticWireParameter paramsVO = TestReadProp.generateDomesticVO(byteBuff, props, encoding, encoding);
				System.out.println(String.format("=================================== %n%s - %-10s", Thread.currentThread().getName(), paramsVO));

				// 發請求
				DomesticWireResult callBackData = domesticSvc.submitDomesticWire(paramsVO);
				System.out.println(String.format("%n >>> %s - callBackData >>> %s", Thread.currentThread().getName(), callBackData));

//				bw.write(new String(byteArray).trim());
//				bw.newLine();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
