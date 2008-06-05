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

package net.virtualvoid.jcosmos.functional.v0;


public interface Seqs {
	<T> Seq<T> emptySequence();
	<T> Seq<T> array(T... els);
	<T> F2<Seq<T>,Seq<T>,Seq<T>> join();
	<T> Seq<T> seq(Foldable<T> foldable);
	<T> RASeq<T> raseq(RandomAccessible<T> accessible);
}
