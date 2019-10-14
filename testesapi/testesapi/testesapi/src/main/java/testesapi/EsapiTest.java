/**
 * 
 */
package testesapi;

import java.io.BufferedReader;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Encoder;
import org.owasp.esapi.Encryptor;
import org.owasp.esapi.HTTPUtilities;
import org.owasp.esapi.errors.EncodingException;
import org.owasp.esapi.errors.EncryptionException;

/**
 * @author a.cardenas.riquelme
 *
 */
public class EsapiTest {

	/**
	 * @param args
	 * @throws EncodingException 
	 * @throws EncryptionException 
	 */
	public static void main(String[] args) throws EncodingException, EncryptionException {
		// TODO Auto-generated method stub
		
		//Encoder encoder = ESAPI.encoder();
		
		Encoder red= ESAPI.encoder();
		
		 
		
		//String input1 = encoder.encodeForHTML("RED");
		
		System.out.print("red" + red.encodeForHTML("<script>red"));
		
		
		

}


	}

