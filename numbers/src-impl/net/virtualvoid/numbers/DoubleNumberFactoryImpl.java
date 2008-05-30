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

import net.virtualvoid.jcosmos.engine.FactoryHelper;
import net.virtualvoid.jcosmos.engine.LazyVal;
import net.virtualvoid.numbers.DoubleNumberFactory;
import net.virtualvoid.numbers.Number;
import net.virtualvoid.numbers.NumberImplementor;

public class DoubleNumberFactoryImpl implements DoubleNumberFactory{
	private final static LazyVal<NumberImplementor> implementor = new LazyVal<NumberImplementor>(){
		@Override
		protected NumberImplementor retrieve() {
			return FactoryHelper.getFactory(NumberImplementor.class);
		}
	};
	public Number newInstance(double d) {
		return implementor.get().newInstance(new DoubleNumberMinImpl(d));
	}
}
