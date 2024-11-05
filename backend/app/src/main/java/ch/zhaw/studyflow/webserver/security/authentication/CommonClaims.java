package ch.zhaw.studyflow.webserver.security.authentication;

import ch.zhaw.studyflow.webserver.security.authentication.claims.BooleanClaim;
import ch.zhaw.studyflow.webserver.security.authentication.claims.Claim;
import ch.zhaw.studyflow.webserver.security.authentication.claims.IntegerClaim;
import ch.zhaw.studyflow.webserver.security.authentication.claims.StringClaim;

public final class CommonClaims {
    private CommonClaims() {
        /* no instances */
    }

    public static final Claim<Boolean> AUTHENTICATED = new BooleanClaim("authenticated");
    public static final Claim<Integer> USERID = new IntegerClaim("id");
}
