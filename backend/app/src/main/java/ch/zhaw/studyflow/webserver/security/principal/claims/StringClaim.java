package ch.zhaw.studyflow.webserver.security.principal.claims;

import ch.zhaw.studyflow.webserver.security.principal.Claim;

import java.util.function.Function;

public class StringClaim extends Claim<String> {
    public  StringClaim(String name) {
        super(name, String.class, Function.identity(), Function.identity());
    }
}
