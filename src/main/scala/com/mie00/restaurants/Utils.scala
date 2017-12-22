package com.mie00.restaurants

object Utils {
  def factorial(i: Int): Option[Int] = i match {
    case 0 => Option(1)
    case x if x < 0 => None
    case i => Option(i * factorial(i - 1).get)
  }
  def isPalindrome(str: String): Boolean = str match {
    case x if x.length() <= 1 => true
    case x if x.endsWith(x.charAt(0).toString()) => isPalindrome(str.drop(1).dropRight(1))
    case _ => false
  }
  def compose[A, B, C](f: B => C, g: A => B): A => C = (x => f(g(x)))
  val encodePattern = "^(.)(\\1*)(.*)$".r
  def runLengthEncode(str: String): String = str match {
    case encodePattern(first, tail, rest) => compressLetters(first.charAt(0), tail.length() + 1) + runLengthEncode(rest)
    case _ => ""
  }
  private def compressLetters(c: Char, count: Int): String = c.toString() + count.toString()
  val decodePattern = "^(.)([0-9]+)(.*)$".r
  def runLengthDecode(str: String): String = str match {
    case decodePattern(first, count, rest) => decompressLetters(first.charAt(0), count.toInt) + runLengthDecode(rest)
    case _ => ""
  }
  private def decompressLetters(c: Char, count: Int): String = count match {
    case 0 => ""
    case _ => c + decompressLetters(c, count - 1)
  }
}
