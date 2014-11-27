package com.exprezz

import com.twitter.util.Promise

class ServerResponse(done: Promise[ServerResponse]) {
  private val buffer = new StringBuilder

  def getContent() = buffer.toString()

  def write(chunk: String) = {
    buffer ++= chunk
    this
  }

  def end() = {
    done.setValue(this)
    this
  }
}