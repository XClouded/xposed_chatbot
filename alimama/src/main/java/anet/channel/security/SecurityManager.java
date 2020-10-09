package anet.channel.security;

public class SecurityManager {
    private static volatile ISecurityFactory securityFactory;

    public static void setSecurityFactory(ISecurityFactory iSecurityFactory) {
        securityFactory = iSecurityFactory;
    }

    public static ISecurityFactory getSecurityFactory() {
        if (securityFactory == null) {
            securityFactory = new ISecurityFactory() {
                public ISecurity createSecurity(String str) {
                    return new SecurityGuardImpl(str);
                }

                public ISecurity createNonSecurity(String str) {
                    return new NoSecurityGuardImpl(str);
                }
            };
        }
        return securityFactory;
    }
}
