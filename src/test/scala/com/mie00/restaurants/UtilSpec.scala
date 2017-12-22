package com.mie00.restaurants

import org.scalatest._

class UtilSpec extends FlatSpec with Matchers {
  "factorial" should "return 1 in case of 0" in {
    Utils.factorial(0) shouldEqual Some(1)
  }
  "factorial" should "work for positive numbers" in {
    Utils.factorial(1) shouldEqual Some(1)
    Utils.factorial(2) shouldEqual Some(2)
    Utils.factorial(3) shouldEqual Some(6)
    Utils.factorial(5) shouldEqual Some(120)
  }
  "factorial" should "return None for negative numbers" in {
    Utils.factorial(-1) shouldEqual None
    Utils.factorial(-2) shouldEqual None
  }
  "isPalindrome" should "return true for palindromes" in {
    "madam mom radar refer wow".split(" ").map(word => Utils.isPalindrome(word) shouldEqual true)
    Utils.isPalindrome("") shouldEqual true
    Utils.isPalindrome("b") shouldEqual true
  }
  "isPalindrome" should "return false for non palindromes" in {
    Utils.isPalindrome("ba") shouldEqual false
  }
  "compose" should "work for integer parameters" in {
    val inc: Int => Int = _ + 1
    val square: Int => Int = i => i * i
    val cmp = Utils.compose(square, inc)
    cmp(6) shouldEqual 49
  }
  "compose" should "work for different types" in {
    val isPos: Int => Boolean = _ > 0
    val boolToString: Boolean => String = if (_) "true" else "false"
    val reprPos = Utils.compose(boolToString, isPos)
    reprPos(6) shouldEqual "true"
    reprPos(0) shouldEqual "false"
  }
  "encodePatter" should "work" in {
    Utils.runLengthEncode("aaaaaaaaaabbbaxxxxyyyzyx") shouldEqual "a10b3a1x4y3z1y1x1"
  }
  "decodePatter" should "work" in {
    Utils.runLengthDecode("a10b3a1x4y3z1y1x1") shouldEqual "aaaaaaaaaabbbaxxxxyyyzyx"
  }
}
