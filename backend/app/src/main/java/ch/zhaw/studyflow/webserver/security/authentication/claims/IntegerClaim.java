package ch.zhaw.studyflow.webserver.security.authentication.claims;

public class IntegerClaim extends Claim<Integer> {
    public IntegerClaim(String name) {
        super(name, Integer.class, Integer::getInteger, Object::toString);
    }
}
