package com.exprezz

import java.nio.charset.Charset
import java.util.function.Function

import com.twitter.finagle.Http

object Client {
  def get(url: String, callback: Function[String, Unit]) = {
    Http.fetchUrl(url)
      .onSuccess { response =>
        callback.apply(response.getContent.toString(Charset.forName("UTF-8")))
      }
      .onFailure { ex =>
        // TODO emit error
        println(s"Request failed to $url with $ex")
      }
  }
}
