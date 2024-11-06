package ch.zhaw.studyflow.webserver.security.principal;

import java.util.Objects;

/**
 * A claim is a piece of information that is stored in a principal.
 * Claims are used to store information about a user. It is not recommended to store sensitive information in a claim,
 * since it is not guaranteed to be secure.
 * @param <T> The type of the value.
 */
public abstract class Claim<T> {
    private final String name;
    private final Class<T> valueType;

    protected Claim(String name, Class<T> valueType) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(valueType);

        this.name       = name;
        this.valueType  = valueType;
    }


    /**
     * Returns the name of the claim.
     * @return the name of the claim
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the type of the value of the claim.
     * @return the type of the value of the claim
     */
    public Class<T> valueType() {
        return valueType;
    }
}
