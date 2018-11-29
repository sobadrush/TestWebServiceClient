package com.ctbc.test.aml;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Properties;

import org.datacontract.schemas._2004._07.PatriotOfficer_DomesticWireService.DomesticWireParameter;

import com.ctbc.test.TestReadProp;

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
				DomesticWireParameter vo = TestReadProp.generateDomesticVO(byteArray, props, "UTF8", "UTF8");
				System.out.println(Thread.currentThread().getName() + " >>> \n " + vo);
			}

			//----------------------
			System.out.println();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
