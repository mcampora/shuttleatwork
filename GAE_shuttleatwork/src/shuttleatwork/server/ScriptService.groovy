package shuttleatwork.server

import groovy.lang.Binding;
import groovy.lang.Script;
import java.lang.reflect.Constructor;
import shuttletowork.system.*;
import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;

class ScriptService {
  def root;

  def ScriptService(def path) {
    root = path;
  }

  String execute(String name, def req) {
    println ">ScriptService: " + name

	Binding binding = new Binding();
	binding.setVariable("req", req);
	Class clazz = Class.forName("shuttleatwork.actions." + name);
	Constructor<Script> cons = clazz.getConstructor(Binding.class);
	Script foo = cons.newInstance(binding);
	String res = (String)foo.run();
	return res;
  }
}