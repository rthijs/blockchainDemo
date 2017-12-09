package be.continuum.etherdemo;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

public class Web3jServiceImpl implements Web3jService {
	
	@Autowired
	private Web3j web3j;

	@Override
	public String getClientVersion() throws IOException {
		return web3j.web3ClientVersion().send().getWeb3ClientVersion();
	}

	@Override
	public BigInteger getAccountBalance(String address) throws InterruptedException, ExecutionException {
		return web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).sendAsync().get().getBalance();
	}

	@Override
	public TransactionReceipt sendEther(String fromAddress, String toAddress, BigInteger amount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransactionReceipt sendData(String fromAddress, String toAddress, BigInteger amount, String data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransactionReceipt sendEtherAndData(String fromAddress, String toAddress, BigInteger amount, String data) {
		// TODO Auto-generated method stub
		return null;
	}

}
