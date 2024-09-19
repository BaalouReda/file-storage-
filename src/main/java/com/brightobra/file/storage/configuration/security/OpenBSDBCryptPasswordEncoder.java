package com.brightobra.file.storage.configuration.security;

import java.security.SecureRandom;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.crypto.generators.OpenBSDBCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

//@CopyRight from : https://gist.github.com/rubensa/f998c068d5a0bb0fcfa4f60401482fdb
public class OpenBSDBCryptPasswordEncoder implements PasswordEncoder {
    private Pattern BCRYPT_PATTERN = Pattern
            .compile("\\A\\$2(a|y|b)?\\$\\d\\d\\$[./0-9A-Za-z]{53}");
    private final Log logger = LogFactory.getLog(getClass());

    private static final int GENSALT_DEFAULT_LOG2_ROUNDS = 10;
    private static final int BCRYPT_SALT_LEN = 16;

    static final int MIN_LOG_ROUNDS = 4;
    static final int MAX_LOG_ROUNDS = 31;

    private static final String DEFAULT_VERSION = "2y";

    private final String version;
    private final int strength;

    private final SecureRandom random;

    public OpenBSDBCryptPasswordEncoder() {
        this(GENSALT_DEFAULT_LOG2_ROUNDS);
    }

    /**
     * @param version  the version, may be 2b, 2y or 2a. (2a is not backwards compatible.)
     *
     */
    public OpenBSDBCryptPasswordEncoder(String version) {
        this(version, GENSALT_DEFAULT_LOG2_ROUNDS);
    }

    /**
     * @param version  the version, may be 2b, 2y or 2a. (2a is not backwards compatible.)
     * @param strength the log rounds to use, between 4 and 31
     *
     */
    public OpenBSDBCryptPasswordEncoder(String version, int strength) {
        this(version, strength, new SecureRandom());
    }

    /**
     * @param strength the log rounds to use, between 4 and 31
     *
     */
    public OpenBSDBCryptPasswordEncoder(int strength) {
        this(DEFAULT_VERSION, strength, new SecureRandom());
    }

    /**
     * @param strength the log rounds to use, between 4 and 31
     * @param random the secure random instance to use
     *
     */
    public OpenBSDBCryptPasswordEncoder(int strength, SecureRandom random) {
        this(DEFAULT_VERSION, strength, random);
    }

    /**
     * @param version  the version, may be 2b, 2y or 2a. (2a is not backwards compatible.)
     * @param strength the log rounds to use, between 4 and 31
     * @param random the secure random instance to use
     *
     */
    public OpenBSDBCryptPasswordEncoder(String version, int strength, SecureRandom random) {
        if (strength < MIN_LOG_ROUNDS || strength > MAX_LOG_ROUNDS) {  // Minimum rounds: 16, maximum 2^31
            throw new IllegalArgumentException("Bad strength");
        }
        this.version = version;
        this.strength = strength;
        this.random = random;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        byte rnd[] = new byte[BCRYPT_SALT_LEN];

        random.nextBytes(rnd);
        return OpenBSDBCrypt.generate(version, rawPassword.toString().toCharArray(), rnd, strength);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (encodedPassword == null || encodedPassword.length() == 0) {
            logger.warn("Empty encoded password");
            return false;
        }

        if (!BCRYPT_PATTERN.matcher(encodedPassword).matches()) {
            logger.warn("Encoded password does not look like BCrypt");
            return false;
        }

        return OpenBSDBCrypt.checkPassword(encodedPassword, rawPassword.toString().toCharArray());
    }
}