package ch.zhaw.studyflow.webserver.security.principal.claims;

import ch.zhaw.studyflow.webserver.security.principal.Claim;

public class IntegerClaim extends Claim<Integer> {
    public IntegerClaim(String name) {
        super(name, Integer.class, Integer::getInteger, Object::toString);
    }
}
