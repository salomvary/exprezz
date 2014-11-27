package com.exprezz

import javax.script.ScriptEngineManager

import scala.io.Source

object Application extends App {
  // `null` is a magic value to fix nashorn availability when running from sbt
  // https://github.com/sbt/sbt/issues/1214#issuecomment-55566056
  val engineManager = new ScriptEngineManager(null)
  val engine = engineManager.getEngineByName("nashorn")

  eval("vendor/settimeout.js")
  eval("vendor/es6-promise-2.0.0.js")
  eval("application.js")

  private def eval(path: String) {
    val source = Source.fromFile("src/main/javascript/" + path)
    engine.eval(source.reader())
  }
}
