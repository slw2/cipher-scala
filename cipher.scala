/** Import is for readLine so that we can write input directly to the program */

import scala.io.StdIn

object Cipher {
  /** Bit-wise exclusive-or of two characters */
  def xor(a: Char, b: Char): Char = (a.toInt ^ b.toInt).toChar

  /** Print ciphertext in octal */
  def showCipher(cipher: Array[Char]) = {
    for (c <- cipher) {
      print(c / 64); print(c % 64 / 8); print(c % 8); print(" ")
    }
    println()
  }

  /** Read file into array (discarding the EOF character) */
  def readFile(fname: String): Array[Char] =
    scala.io.Source.fromFile(fname).toArray.init

  /* Functions below here need to be implemented */

  /** Encrypt plain using key; can also be used for decryption */
  def encrypt(key: Array[Char], plain: Array[Char]): Array[Char] = {
    // Create an array for the result, called cipher
    var cipher = new Array[Char](plain.size)
    var i = 0
    // Go through each element of the plain array and encrypt each element
    // Invariant: cipher[0..i) = encrypt (key, plain[0..i))
    while (i < plain.size) {
      cipher(i) = xor(plain(i), key(i % key.length))
      // cipher[0..i+1) = encrypt (key, plain[0..i+1))
      i = i + 1
      // I
    }
    //showCipher(cipher) 
    cipher
  }

  /** Try to decrypt ciphertext, using crib as a crib */
  def tryCrib(crib: Array[Char], ciphertext: Array[Char]) = {
    val K = crib.size
    // set variable start for the starting position of the crib in the ciphertext
    var start = 0
    var foundKey = false
    // 0 <= start <= size of ciphertext - K
    while (start <= ciphertext.size - K && !(foundKey)) {
      var a = 0
      var keyChars = new Array[Char](K)
      // I: keyChars[0..a) = xor(ciphertext[start..start+a), crib[0..a)
      while (a < K) {
        keyChars(a) = xor(ciphertext(start + a), crib(a))
        a = a + 1
        // I
      }
      // test for repetition
      var j = testRep(keyChars, K)
      // post: j is the length of the key
      // if there is repetition, print the key and the plain text
      if (j > 0) {
        var i = 0
        val key = new Array[Char](j)
        while (i < j) {
          // start%j is the position in the key that keyChars starts
          key(i) = keyChars(((j - start % j) + i) % j)
          //println("i= " + i + "j= " + j + "start= " + start + "start mod j = " + start%j + "j-start%j = " + (j-start%j) + "j-start%j + i" + (j-start%j +i))
          i += 1
        }
        foundKey = true
        println("Key: " + key.mkString)
        println("Plain text: " + encrypt(key, ciphertext).mkString)
      }
      // if no repetition, start again with new starting point
      else start = start + 1
    }
  }

  def testRep(keyChars: Array[Char], K: Int) = {
    var j = 2
    var i = 0
    while (j <= K - 2 && j + i < K) {
      if (keyChars(i) == keyChars(j + i)) i += 1
      else {
        j = j + 1
        i = 0
      }
    }
    if (j > K - 2) 0
    else j
  }


  /** The first optional statistical test, to guess the length of the key */
  def crackKeyLen(ciphertext: Array[Char]) = ???

  /** The second optional statistical test, to guess characters of the key. */
  def crackKey(klen: Int, ciphertext: Array[Char]) = ???

  /** The main method just selects which piece of functionality to run */
  def main(args: Array[String]) = {
    // string to print if error occurs
    val errString =
      "Usage: scala Cipher (-encrypt|-decrypt) key [file]\n" +
        "     | scala Cipher -crib crib [file]\n" +
        "     | scala Cipher -crackKeyLen [file]\n" +
        "     | scala Cipher -crackKey len [file]"

    // Get the plaintext, either from the file whose name appears in position
    // pos, or from standard input
    def getPlain(pos: Int) =
      if (args.length == pos + 1) readFile(args(pos)) else StdIn.readLine.toArray

    // Check there are at least n arguments
    def checkNumArgs(n: Int) = if (args.length < n) {
      println(errString); sys.exit
    }

    // Parse the arguments, and call the appropriate function
    checkNumArgs(1)
    val command = args(0)
    if (command == "-encrypt" || command == "-decrypt") {
      checkNumArgs(2);
      val key = args(1).toArray;
      val plain = getPlain(2)
      println(new String(encrypt(key, plain)))
    }
    else if (command == "-crib") {
      checkNumArgs(2);
      val key = args(1).toArray;
      val plain = getPlain(2)
      tryCrib(key, plain)
    }
    else if (command == "-crackKeyLen") {
      checkNumArgs(1);
      val plain = getPlain(1)
      crackKeyLen(plain)
    }
    else if (command == "-crackKey") {
      checkNumArgs(2);
      val klen = args(1).toInt;
      val plain = getPlain(2)
      crackKey(klen, plain)
    }
    else println(errString)
  }
}
