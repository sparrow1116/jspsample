package com.sec.webs.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import com.sec.webs.util.PropertyUtil;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class PasswordHelper {
	
	private final static int ITERATION_NUMBER = PropertyUtil.getInt("common.iteration_number");
	
	 /**
	    * From a password, a number of iterations and a salt,
	    * returns the corresponding digest
	    * @param iterationNb int The number of iterations of the algorithm
	    * @param password String The password to encrypt
	    * @param salt byte[] The salt
	    * @return byte[] The digested password
	    * @throws NoSuchAlgorithmException If the algorithm doesn't exist
	    * @throws UnsupportedEncodingException 
	    */
	   public static byte[] getHash(String password, byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
	       MessageDigest digest = MessageDigest.getInstance("SHA-1");
	       digest.reset();
	       digest.update(salt);
	       byte[] input = digest.digest(password.getBytes("UTF-8"));
	       for (int i = 0; i < ITERATION_NUMBER; i++) {
	           digest.reset();
	           input = digest.digest(input);
	       }
	       return input;
	   }
	 
	public static byte[] base64ToByte(String data) throws IOException {
		BASE64Decoder decoder = new BASE64Decoder();
		return decoder.decodeBuffer(data);
	}

	public static String byteToBase64(byte[] data) {
		BASE64Encoder endecoder = new BASE64Encoder();
		return endecoder.encode(data);
	}

	public static String[] getDigestAndSalt(String pw) throws NoSuchAlgorithmException, UnsupportedEncodingException {
	    // Uses a secure Random not a simple Random
	    SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
	    // Salt generation 64 bits long
	    byte[] bSalt = new byte[8];
	    random.nextBytes(bSalt);
	    // Digest computation
	    byte[] bDigest = getHash(pw, bSalt);
	    String sDigest = byteToBase64(bDigest);
	    String sSalt = byteToBase64(bSalt);
	    return new String[] { sDigest, sSalt };
	}
	
	
	 

}
