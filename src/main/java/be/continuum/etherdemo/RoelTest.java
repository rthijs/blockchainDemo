package be.continuum.etherdemo;

import java.io.IOException;
import java.math.BigInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.protocol.exceptions.TransactionException;

@Component
public class RoelTest {
	
	@Autowired
	Web3jSampleService web3jSampleService;

	public void printSomeLines() throws InterruptedException, TransactionException, Exception {
		System.out.println("nu in RoelTest.printSomeLines");
		
		try {
			System.out.println(web3jSampleService.getClientVersion());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BigInteger wei = web3jSampleService.getEtherAmount("0x007c5a80fb694a02cc201a13bbf55bab83349293");
		
		System.out.println(wei.toString());
		
		//System.out.println(web3jSampleService.getRawTransactionResponse());
		
		web3jSampleService.testGetAccounts();
		
		web3jSampleService.testUnlockPersonalAccount();
		
		System.out.println(web3jSampleService.getEtherAmount("0x3a75b6cda8c06b6806e5117d18d53a8dd55a95e4"));
		
		web3jSampleService.sendTransaction();
		
		
	}
}
