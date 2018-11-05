Suppose we wanted to encrypt the plain-text, “Send guns tomorrow,” using the keyword “BRICKS”. We begin by writing out the message, and writing the keyword underneath, repeating the keyword as often as necessary to match the length of the message:

Plain: S E N D G U N S T O M O R R O W

Key: B R I C K S B R I C K S B R I C

Cipher: T V V F Q M O J B Q W G S I W Y

We now start to add the plain-text and the key letters together, using the convention A = 0, B = 1, C = 2, etc., and reducing the answer modulo 26.1 So we calculate S + B = 18 + 1 = 19 = T, E + R = 4 + 17 = 21 = V , and so on. Later in the message, we compute U + S = 20 + 18 = 38 ≡ 12 = M, where ≡ denotes reduction modulo 26. All this calculation gives the ciphertext shown in the third line. When the cipher-text is received by someone who knows the key, they can decrypt the original message by reversing the process and subtracting the key letters again.
