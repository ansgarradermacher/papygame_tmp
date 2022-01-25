/*****************************************************************************
 * Copyright (c) 2020 CEA LIST and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   CEA LIST - Initial API and implementation
 *
 *****************************************************************************/

package org.eclipse.papyrus.gamification.data;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class Encryptor {
	private byte[] key;
	private byte[] iv;



	private static final String ALGORITHM = "AES";

	public Encryptor() throws DecoderException {
		// this.key = DatatypeConverter.parseHexBinary("6250655368566D5971337477397A24432646294A404E635166546A576E5A7234");
		// this.iv = DatatypeConverter.parseHexBinary("564af9633b9f5ac0b1218843f6d4b059")

		this.key = Hex.decodeHex("6250655368566D5971337477397A24432646294A404E635166546A576E5A7234");
		this.iv = Hex.decodeHex("564af9633b9f5ac0b1218843f6d4b059");

		System.out.println("key (" + key.length + "): " + key);
		System.out.println("iv (" + iv.length + "): " + iv);


	}



	public String encrypt(String input) {
		try {
			byte[] plainText = input.getBytes();
			SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
			IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
			byte[] array = cipher.doFinal(plainText);
			// return DatatypeConverter.printBase64Binary(array);
			return Base64.encodeBase64String(array);


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}

	}

	public byte[] getKey() {
		return key;
	}

	public void setKey(byte[] key) {
		this.key = key;
	}

	public byte[] getIv() {
		return iv;
	}

	public void setIv(byte[] iv) {
		this.iv = iv;
	}
}