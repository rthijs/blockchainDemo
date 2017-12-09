package be.continuum.etherdemo;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

public interface Web3jService {
	
	public String getClientVersion() throws IOException;
	
	public BigInteger getAccountBalance(String address) throws InterruptedException, ExecutionException;
	
	public TransactionReceipt sendEther(String fromAddress, String toAddress, BigInteger amount);
	
	public TransactionReceipt sendData(String fromAddress, String toAddress, BigInteger amount, String data);
	
	public TransactionReceipt sendEtherAndData(String fromAddress, String toAddress, BigInteger amount, String data);

}
