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

package net.virtualvoid.jcosmos.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.virtualvoid.crypto.HashTools;
import net.virtualvoid.crypto.SHA1Hash;
import net.virtualvoid.crypto.SHA1Hashes;
import net.virtualvoid.jcosmos.annotation.Export;
import net.virtualvoid.jcosmos.annotation.Import;
import net.virtualvoid.jcosmos.engine.LazyVal;

@Export
public class SHA1HashFactory implements SHA1Hashes{
	@Import
	HashTools tools;

	private class SHA1Impl implements SHA1Hash{
		private final byte[] hash;

		public SHA1Impl(byte[] hash) {
			this.hash = hash;
		}
		public byte[] asBytes() {
			return hash;
		}

		private final LazyVal<String> hashString = new LazyVal<String>(){
			@Override
			protected String retrieve() {
				return tools.fromBytes(asBytes());
			}
		};
		public String asString() {
			return hashString.get();
		}
	}

	private MessageDigest getDigest(){
		try {
			return MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {
			throw new Error("Couldn't find SHA1 MessageDigest implementation");
		}
	}

	public SHA1Hash hash(byte[] bs) {
		return new SHA1Impl(getDigest().digest(bs));
	}

	final int READ_SIZE = 1024;

	public SHA1Hash hash(InputStream is) {
		ByteBuffer buffer = ByteBuffer.allocate(READ_SIZE);
		ReadableByteChannel readChannel = Channels.newChannel(is);

		MessageDigest digest = getDigest();

		try {
			while(readChannel.read(buffer)!=-1){
				digest.update(buffer);
				buffer.clear();
			}
			return new SHA1Impl(digest.digest());
		} catch (IOException e) {
			throw new Error("Error while reading input stream",e);
		}
	}
}
