package project.dailyge.security.jasyp

import org.jasypt.encryption.StringEncryptor
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig

class JasyptConfig(
    private val password: String,
) {

    private companion object {
        private const val POOL_SIZE = 1
        private const val ALGORITHM = "PBEWithMD5AndDES"
        private const val PROVIDER_NAME = "SunJCE"
        private const val SALT_GENERATOR_CLASS_NAME = "org.jasypt.salt.RandomSaltGenerator"
        private const val IV_GENERATOR_CLASS_NAME = "org.jasypt.iv.NoIvGenerator"
        private const val BASE_64 = "base64"
    }

    fun createStringEncryptor(): StringEncryptor {
        val encryptor = PooledPBEStringEncryptor()
        val config = SimpleStringPBEConfig().apply {
            this.password = this@JasyptConfig.password
            algorithm = ALGORITHM
            setKeyObtentionIterations("1000")
            poolSize = POOL_SIZE
            providerName = PROVIDER_NAME
            setSaltGeneratorClassName(SALT_GENERATOR_CLASS_NAME)
            setIvGeneratorClassName(IV_GENERATOR_CLASS_NAME)
            stringOutputType = BASE_64
        }
        encryptor.setConfig(config)
        return encryptor
    }
}
