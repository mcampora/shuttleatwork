package shuttletowork.system;

import static org.junit.Assert.*;
import groovy.util.GroovyTestCase;

class JSonTest extends GroovyTestCase {
	static class AA {
		def s;
		def bb;
	}
	
	static class BB {
		def s;
		def aa;
	}
	
	public void testString() {
		assertEquals JSon.transform("test"), "'test'"
	}
	
	public void testArray() {	
		  def b = []
		  def a = [ "1", b ]
		  b.add(a)
		  assertEquals JSon.transform(a), "[ '1', [ { 'jsonref':1 },  ],  ]" // missing a jsonid for arrays
	}
	
	public void testMap() {
		  def c = [:]
		  def d = [:]
		  c["d"] = d
		  d["c"] = c
		  assertEquals JSon.transform(c), "{ 'jsonid':1, 'd':{ 'jsonid':2, 'c':{ 'jsonref':1 },  },  }"
	}
		
	public void testObj() {
		  def aa = new AA()
		  def bb = new BB()
		  aa.s = "aa"
		  aa.bb = bb
		  bb.s = "bb"
		  assertEquals JSon.transform(aa), "{ 'jsonid':1, 'bb':{ 'jsonid':2, 's':'bb',  }, 's':'aa',  }"
		  
		  bb.aa = aa
		  assertEquals JSon.transform(aa), "{ 'jsonid':1, 'bb':{ 'jsonid':2, 'aa':{ 'jsonref':1 }, 's':'bb',  }, 's':'aa',  }"
	}	
}
