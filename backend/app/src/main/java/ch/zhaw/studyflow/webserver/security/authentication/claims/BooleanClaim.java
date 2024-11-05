package ch.zhaw.studyflow.webserver.security.authentication.claims;

public class BooleanClaim extends Claim<Boolean> {
    public BooleanClaim(String name) {
        super(name, Boolean.class, Boolean::parseBoolean, Object::toString);
    }
}
