package be.continuum.etherdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

/**
 * Sample service to demonstrate web3j bean being correctly injected.
 * 
 * https://github.com/web3j/examples/blob/master/spring-boot/src/main/java/org/web3j/examples/Web3jSampleService.java
 */
@Service
public class Web3jSampleService {

	@Autowired
	private Web3j web3j;

	public String getClientVersion() throws IOException {
		Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
		return web3ClientVersion.getWeb3ClientVersion();
	}

	public BigInteger getEtherAmount(String address) {

		// send asynchronous requests to get balance
		EthGetBalance ethGetBalance;
		try {
			ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).sendAsync().get();

			BigInteger wei = ethGetBalance.getBalance();

			return wei;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public String getRawTransactionResponse() {
		EthTransaction transaction;
		try {
			transaction = web3j
					.ethGetTransactionByHash("0x5b676dd71b385812fafd1a04243bc8ccbc557260b0089c0e92acac352e25fed5")
					.sendAsync().get();

			return transaction.getRawResponse();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	public void testGetAccounts() {
		try {
			EthAccounts accounts = web3j.ethAccounts().sendAsync().get();

			for (String accountString : accounts.getAccounts()) {
				System.out.println("Account x = " + accountString);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testUnlockPersonalAccount() {

		Admin admin = Admin.build(new HttpService());

		PersonalUnlockAccount personalUnlockAccount;
		try {
			personalUnlockAccount = admin
					.personalUnlockAccount("0x3a75b6cda8c06b6806e5117d18d53a8dd55a95e4", "hittentit").send();

			if (personalUnlockAccount.accountUnlocked()) {
				System.out.println("personal account unlocked");
			} else {
				System.out.println("ERROR: personal account locked");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendTransaction() throws InterruptedException, TransactionException, Exception {
        
        URL dinges = getClass().getResource("/007c5a80fb694a02cc201a13bbf55bab83349293.wallet");
        
        System.out.println(dinges.getFile());
        System.out.println(dinges.getPath());
        
        Credentials credentials = WalletUtils.loadCredentials("sivlaw", dinges.getPath());
        
		System.out.println("amount:" +  getEtherAmount("0x007c5a80fb694a02cc201a13bbf55bab83349293").toString() + "in 0x007c5a80fb694a02cc201a13bbf55bab83349293");
		System.out.println("amount:" +  getEtherAmount("0x3a75b6cda8c06b6806e5117d18d53a8dd55a95e4").toString() + "in 0x3a75b6cda8c06b6806e5117d18d53a8dd55a95e4");
		
		ECKeyPair ecKeyPair = credentials.getEcKeyPair();
		
		System.out.println("private key: " + ecKeyPair.getPrivateKey().toString());
		System.out.println("public key: " + ecKeyPair.getPublicKey().toString());
		
		TransactionReceipt transactionReceipt = Transfer.sendFunds(
		        web3j, credentials, "0x3a75b6cda8c06b6806e5117d18d53a8dd55a95e4",
		        BigDecimal.valueOf(0.01), Convert.Unit.ETHER).send();
		
		System.out.println("transactionHash : " + transactionReceipt.getTransactionHash());
		
		System.out.println("amount:" +  getEtherAmount("0x007c5a80fb694a02cc201a13bbf55bab83349293").toString() + "in 0x007c5a80fb694a02cc201a13bbf55bab83349293");
		System.out.println("amount:" +  getEtherAmount("0x3a75b6cda8c06b6806e5117d18d53a8dd55a95e4").toString() + "in 0x3a75b6cda8c06b6806e5117d18d53a8dd55a95e4");		

	}
	
	public void sendTransactionWithData() throws IOException, CipherException {
		URL dinges = getClass().getResource("/007c5a80fb694a02cc201a13bbf55bab83349293.wallet");
		Credentials credentials = WalletUtils.loadCredentials("sivlaw", dinges.getPath());
		
		System.out.println(credentials.getAddress());
		
		// get the next available nonce
		EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
				credentials.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
		
		BigInteger nonce = ethGetTransactionCount.getTransactionCount();

		// create our transaction
		//RawTransaction rawTransaction  = RawTransaction.createEtherTransaction( nonce, <gas price>, <gas limit>, <toAddress>, <value>);

		// sign & send our transaction
		byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
		String hexValue = Hex.toHexString(signedMessage);
		EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
		
	}
	    public static RawTransaction createTransaction(
	            BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to,
	BigInteger value, String data) {
	}
}