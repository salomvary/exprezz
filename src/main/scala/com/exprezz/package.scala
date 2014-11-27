package com

import java.util.function.BiFunction

package object exprezz {
  /**
   * "When interfaces consist of a single method, a function object can be directly
   * given with no need to perform an explicit new operator call."
   * http://www.oracle.com/technetwork/articles/java/jf14-nashorn-2126515.html
   *
   * Unfortunately, Scala's Functions are not interfaces, they are abstract classes
   * in Java terms. Hence the usage of Java BiFunction here.
   */
  type RequestListener = BiFunction[IncomingMessage, ServerResponse, Unit]
}
