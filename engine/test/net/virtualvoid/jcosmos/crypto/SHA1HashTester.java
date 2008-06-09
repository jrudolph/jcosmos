package net.virtualvoid.jcosmos.crypto;
/*
 The contents of this file are subject to the Mozilla Public License
 Version 1.1 (the "License"); you may not use this file except in
 compliance with the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/

 Software distributed under the License is distributed on an "AS IS"
 basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 License for the specific language governing rights and limitations
 under the License.

 The Initial Developer of the Original Code is Johannes Rudolph.
 Portions created by the Initial Developer are Copyright (C) 2008
 the Initial Developer. All Rights Reserved.

 Contributor(s):
    Johannes Rudolph <johannes_rudolph@gmx.de>
 */

import static org.testng.AssertJUnit.assertEquals;
import net.virtualvoid.crypto.HashTools;
import net.virtualvoid.crypto.SHA1Hash;
import net.virtualvoid.crypto.SHA1Hashes;
import net.virtualvoid.jcosmos.JCosmosTest;
import net.virtualvoid.jcosmos.annotation.Import;

import org.testng.annotations.Test;

public class SHA1HashTester extends JCosmosTest{
	@Import
	SHA1Hashes factory;

	@Import
	HashTools HashTools;

	private void assertHash(String expected,String toHash){
		SHA1Hash hash = factory.hash(toHash.getBytes());
		assertEquals(expected,hash.asString());
		assertEquals(expected,HashTools.fromBytes(HashTools.toBytes(hash.asString())));
	}

	@Test
	public void testSHA1(){
		assertHash("2856df06a28c38ab6015a039722241a198cc5680","blub\n");
	}

	@Test
	public void testHashTools(){
		assertEquals("32",HashTools.fromBytes(new byte[]{0x32}));
		assertEquals("32f5",HashTools.fromBytes(new byte[]{0x32,(byte)0xf5}));
	}
}
