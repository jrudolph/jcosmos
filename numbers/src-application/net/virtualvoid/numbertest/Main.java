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

package net.virtualvoid.numbertest;

import net.virtualvoid.jcosmos.Program;
import net.virtualvoid.jcosmos.annotation.Export;
import net.virtualvoid.jcosmos.annotation.Import;
import net.virtualvoid.numbers.DoubleNumberFactory;
import net.virtualvoid.numbers.Number;
import net.virtualvoid.numbers.RationalNumber;
import net.virtualvoid.numbers.RationalNumberFactory;

@Export
public class Main implements Program{
	@Import
	private DoubleNumberFactory factory;
	@Import
	private RationalNumberFactory rs;

	private Number d(double d){
		return factory.newInstance(d);
	}
	public void main(String[] args) {
		Number n1 = d(5.5);
		Number n2 = d(4.5);
		assert Math.abs(n1.sub(n2).doubleValue()-1.0) < 0.0001;

		RationalNumber r1 = rs.newInstance(5, 2);
		RationalNumber r2 = rs.newInstance(4, 3);
		RationalNumber res = r1.add(r2);
		assert res.numerator() == 23;
		assert res.denominator() == 6;

		RationalNumber res2 = r1.mult(r2);
		assert res2.numerator() == 10;
		assert res2.denominator() == 3;

		RationalNumber kuerz = rs.newInstance(24, 3);
		assert kuerz.numerator() == 8;
		assert kuerz.denominator() == 1;
	}
}
