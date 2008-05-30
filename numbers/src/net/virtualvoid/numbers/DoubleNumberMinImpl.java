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

package net.virtualvoid.numbers;

import net.virtualvoid.numbers.NumberMin;

public class DoubleNumberMinImpl implements NumberMin{
	final double value;
	DoubleNumberMinImpl(double d){
		value=d;
	}
	protected NumberMin d(double d){
		return new DoubleNumberMinImpl(d);
	}
	public NumberMin add(NumberMin n2) {
		return d(value+n2.doubleValue());
	}
	public double doubleValue() {
		return value;
	}
	public NumberMin mult(NumberMin n2) {
		return d(value*n2.doubleValue());
	}
	public NumberMin multInv() {
		return d(1./value);
	}
	public NumberMin neg() {
		return d(-value);
	}
}
