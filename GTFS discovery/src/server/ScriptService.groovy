package server

import facade.*;
import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;

class ScriptService {
  def root;

  def ScriptService(def path) {
    root = path;
  }

  def execute(String name, def req) {
    println ">ScriptService: " + name
    GroovyScriptEngine gse = new GroovyScriptEngine(root);
    Binding binding = new Binding();
    binding.setVariable("req", req);
    def res = gse.run("facade/${name}.groovy", binding);
    //println "res: $res";
    return res.getBytes();
  }

  // test method
  public static void main(String[] args) {
	  def ss = new ScriptService("${Facade.ROOT}/src/")
	  println ss.execute("helloWorld", null)
	  println ss.execute("helloWorld", [a:'x'])
  }
}
