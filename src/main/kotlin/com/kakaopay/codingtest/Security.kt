package com.kakaopay.codingtest

import org.apache.tomcat.util.codec.binary.Base64
import org.springframework.util.Assert
import org.springframework.util.StringUtils
import java.security.AlgorithmParameters
import java.security.GeneralSecurityException
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.persistence.AttributeConverter
import javax.persistence.Converter

class AesCipherException(message: String?) : RuntimeException(message)

class AesCipher(private val encryptionKey: String) {

    init {
        if (!validateKey(encryptionKey)) {
            throw AesCipherException("Key length must be 16")
        }
    }

    companion object {
        /**
         * Encryption algorithm
         */
        private const val ENCRYPTION_ALGORITHM = "AES/CBC/PKCS5Padding"
        private const val DEFAULT_ENCRYPTION_KEY = "kakao look at me"
        private val IV = "----------------".toByteArray()
        val default = AesCipher(DEFAULT_ENCRYPTION_KEY)
    }

    /**
     * Encrypt a string with a given key.
     *
     * @param clearText string to be encrypted
     * @param key       encryption key
     * @return encrypted string
     */
    private fun pack(clearText: String, key: String?): String {
        checkInit()

        val iv = IV

        val cipherText = encrypt(clearText, key, iv)
        val wrapped = ByteArray(iv.size + cipherText.size)
        System.arraycopy(iv, 0, wrapped, 0, iv.size)
        System.arraycopy(cipherText, 0, wrapped, 16, cipherText.size)

        return Base64.encodeBase64URLSafeString(wrapped)
    }

    fun packInternal(clearText: String): String {
        checkInit()
        return pack(clearText, encryptionKey)
    }

    /**
     * Decrypt a string with given key.
     *
     * @param cipherText encrypted string
     * @param key        the key used in decryption
     * @return a decrypted string
     */
    private fun unpack(cipherText: String, key: String?): String {
        checkInit()

        val dataToDecrypt = Base64.decodeBase64(cipherText.toByteArray())
        val iv = ByteArray(16)
        val data = ByteArray(dataToDecrypt.size - 16)

        System.arraycopy(dataToDecrypt, 0, iv, 0, 16)
        System.arraycopy(dataToDecrypt, 16, data, 0, dataToDecrypt.size - 16)

        val plainText = decrypt(data, key, iv)
        return String(plainText)
    }

    fun unpackInternal(cipherText: String): String {
        checkInit()

        return unpack(cipherText, encryptionKey)
    }

    private fun encrypt(clearText: String, key: String?, iv: ByteArray): ByteArray {
        try {
            val cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM)
            val params = AlgorithmParameters.getInstance("AES")
            params.init(IvParameterSpec(iv))
            cipher.init(Cipher.ENCRYPT_MODE, getKey(key!!), params)
            return cipher.doFinal(clearText.toByteArray())
        } catch (e: GeneralSecurityException) {
            throw AesCipherException("Failed to encrypt.")
        }

    }

    private fun decrypt(cipherBytes: ByteArray, key: String?, iv: ByteArray): ByteArray {
        try {
            val cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM)
            val params = AlgorithmParameters.getInstance("AES")
            params.init(IvParameterSpec(iv))
            cipher.init(Cipher.DECRYPT_MODE, getKey(key!!), params)
            return cipher.doFinal(cipherBytes)
        } catch (e: GeneralSecurityException) {
            throw AesCipherException("Failed to decrypt.")
        }

    }

    private fun getKey(key: String): SecretKeySpec {
        return SecretKeySpec(key.toByteArray(), "AES")

    }

    private fun checkInit() {
        Assert.isTrue(validateKey(encryptionKey), "Key length must be 16")
    }

    private fun validateKey(key: String?): Boolean {
        return !StringUtils.isEmpty(key) && key!!.length == 16
    }
}

@Converter
class StringCipherConverter : AttributeConverter<String, String> {

    override fun convertToDatabaseColumn(attribute: String): String =
        if (StringUtils.isEmpty(attribute)) {
            ""
        } else AesCipher.default.packInternal(attribute)

    override fun convertToEntityAttribute(dbData: String): String =
        if (StringUtils.isEmpty(dbData))
        {
            ""
        } else AesCipher.default.unpackInternal(dbData)
}

