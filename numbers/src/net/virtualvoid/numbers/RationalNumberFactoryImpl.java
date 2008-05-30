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

import net.virtualvoid.jcosmos.annotation.Export;
import net.virtualvoid.jcosmos.annotation.Import;
import net.virtualvoid.numbers.DoubleNumberFactory;
import net.virtualvoid.numbers.Number;
import net.virtualvoid.numbers.NumberImplementor;
import net.virtualvoid.numbers.RationalNumberFactory;

@Export
public class RationalNumberFactoryImpl implements RationalNumberFactory{
	@Import
	private NumberImplementor implementor;
	@Import
	private DoubleNumberFactory doubles;

	@Override
	public RationalNumberImpl newInstance(long num, long denom) {
		long t = RationalNumberImpl.ggt(num,denom);
		return new RationalNumberImpl(num/t,denom/t){
			@Override
			protected Number d(double d) {
				return doubles.newInstance(d);
			}
			@Override
			protected RationalNumberImpl f(long num, long denom) {
				return newInstance(num,denom);
			}
			@Override
			protected Number meFat() {
				return implementor.newInstance(this);
			}
		};
	}
}
