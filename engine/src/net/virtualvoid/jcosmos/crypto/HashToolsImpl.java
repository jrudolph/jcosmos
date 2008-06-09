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

import java.util.regex.Pattern;

import net.virtualvoid.crypto.HashTools;
import net.virtualvoid.jcosmos.annotation.Export;
import net.virtualvoid.jcosmos.annotation.Import;
import net.virtualvoid.jcosmos.functional.util.v0.Arrays;
import net.virtualvoid.jcosmos.functional.util.v0.Numbers;
import net.virtualvoid.jcosmos.functional.util.v0.Strings;

@Export
public class HashToolsImpl implements HashTools{
	@Import
	Arrays Arrays;
	@Import
	Strings Strings;
	@Import
	Numbers Numbers;

	public String fromBytes(byte[] bs) {
		return Arrays.fromPrimitive(bs)
			.map(Strings.format("%02x"))
			.fold(Strings.join(""), new StringBuilder())
			.toString();
	}
	private final Pattern p = Pattern.compile("^[0-9a-f]{40}$");

	private int value(char c){
		if (Character.isDigit(c))
			return c-'0';
		else
			return 10+c-'a';
	}
	public byte[] toBytes(String str) {
		char[]chars = str.toCharArray();
		assert p.matcher(str).matches();
		byte[] res = new byte[20];
		for (int i=0;i<20;i++)
			res[i] = (byte) (value(chars[i*2])*16 + value(chars[i*2+1]));
		return res;
	}
}
