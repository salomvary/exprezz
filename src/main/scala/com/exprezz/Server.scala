package com.exprezz

import java.net.InetSocketAddress

import com.twitter.finagle.builder.ServerBuilder
import com.twitter.finagle.http.{Http, Request, Response, RichHttp}
import com.twitter.finagle.{Service, builder}
import com.twitter.io.Charsets.Utf8
import com.twitter.util.{Future, Promise}
import org.jboss.netty.buffer.ChannelBuffers.copiedBuffer
import org.jboss.netty.handler.codec.http.DefaultHttpResponse
import org.jboss.netty.handler.codec.http.HttpResponseStatus._
import org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1

import scala.collection.mutable

class Server(listener: RequestListener) {
  private val requestListeners = mutable.MutableList[RequestListener]()

  private val service = new Service[Request, Response] {
    def apply(request: Request) =
      handleRequest(request).map { serverResponse =>
        val response = new DefaultHttpResponse(HTTP_1_1, OK)
        response.setContent(copiedBuffer(serverResponse.getContent(), Utf8))
        Response(response)
      }
  }

  private var server: builder.Server = null

  on("request", listener)

  private def makeServer(port: Int) = {
    ServerBuilder()
      .codec(RichHttp[Request](Http()))
      .bindTo(new InetSocketAddress(port))
      .name("httpserver")
      .build(service)
  }

  private def handleRequest(request: Request): Future[ServerResponse] = {
    val response = new Promise[ServerResponse]
    val req = new IncomingMessage(request.getMethod.getName, request.getUri)
    val res = new ServerResponse(response)
    requestListeners.foreach(_.apply(req, res))
    response
  }

  def listen(port: Int) = {
    server = makeServer(port)
    this
  }

  def on(event: String, listener: RequestListener) = {
    event match {
      case "request" => requestListeners += listener
    }
    this
  }
}

object Server {
  def apply(listener: RequestListener) = new Server(listener)

  def createServer(listener: RequestListener) = apply(listener)
}
