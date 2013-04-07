package shuttleatwork.system;

//import static org.junit.Assert.*;
import groovy.util.GroovyTestCase;

class JSonTest extends GroovyTestCase {
	static class AA {
		def s;
		def bb;
	}

	static class BB {
		def s;
		
		boolean b = true;
		int i = 1;
		String str = "msg";
	}

	public void testString() {
		assertEquals JSon2.transform("test"), "\"test\""
	}

	public void testInteger() {
		assertEquals JSon2.transform(1), "1"
	}

	public void testBoolean() {
		assertEquals JSon2.transform(true), "true"
	}

	public void testArray() {
		def b = [ "msg" ]
		def a = [ 1, true, b ]
		assertEquals JSon2.transform(a), "[ 1, true, [ \"msg\" ] ]"
	}

	public void testMap() {
		def c = [:]
		def d = [:]
		c["d"] = d
		d["int"] = 1
		d["str"] = "msg"
		d["bool"] = true
		assertEquals JSon2.transform(c), 
			"{ \"d\":{ \"int\":1, \"str\":\"msg\", \"bool\":true } }"
	}

	public void testObj() {
		def aa = new AA()
		def bb = new BB()
		aa.s = "aa"
		aa.bb = bb
		bb.s = "bb"
		assertEquals JSon2.transform(aa), 
			"{ \"bb\":{ \"b\":true, \"str\":\"msg\", \"i\":1, \"s\":\"bb\" }, \"s\":\"aa\" }"
	}
}
