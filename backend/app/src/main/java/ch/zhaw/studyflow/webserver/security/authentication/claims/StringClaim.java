package ch.zhaw.studyflow.webserver.security.authentication.claims;

import ch.zhaw.studyflow.webserver.security.authentication.Claim;

import java.util.function.Function;

public class StringClaim extends Claim<String> {
    public  StringClaim(String name) {
        super(name, String.class, Function.identity(), Function.identity());
    }
}
