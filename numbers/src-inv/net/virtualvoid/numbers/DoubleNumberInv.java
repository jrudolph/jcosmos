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

import net.virtualvoid.NumberMin;

public class DoubleNumberInv {
	boolean addInv(NumberMin recv,NumberMin arg1,NumberMin res){
		return res.doubleValue() == recv.doubleValue() + arg1.doubleValue();
	}
	boolean multInv(NumberMin recv,NumberMin arg1,NumberMin res){
		return res.doubleValue() == recv.doubleValue() * arg1.doubleValue();
	}
	boolean multInvInv(NumberMin recv,NumberMin res){
		return res.doubleValue() == 1. / recv.doubleValue();
	}
	boolean negInv(NumberMin recv,NumberMin res){
		return res.doubleValue() == -recv.doubleValue();
	}
}
