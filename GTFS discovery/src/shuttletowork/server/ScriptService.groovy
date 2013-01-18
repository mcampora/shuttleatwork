package shuttletowork.server

import shuttletowork.system.*;
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
    def res = gse.run("${name}.groovy", binding);
    //println "res: $res";
    return res.getBytes();
  }
}
