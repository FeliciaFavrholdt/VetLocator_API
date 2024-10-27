package dat.security.controllers;// dat.security.controllers.SecurityController.java
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Set;

@Path("/security")
public class SecurityController {

    private static final String SECRET = "secret_key";
    private ISecurityDAO userDAO;

    public SecurityController(ISecurityDAO userDAO) {
        this.userDAO = userDAO;
    }

    @POST
    @Path("/login")
    public Response login(@FormParam("username") String username, @FormParam("password") String password) {
        try {
            User user = userDAO.getVerifiedUser(username, password);
            String token = createToken(new UserDTO(user));
            return Response.ok(token).build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/register")
    public Response register(@FormParam("username") String username, @FormParam("password") String password) {
        try {
            User newUser = userDAO.createUser(username, password);
            String token = createToken(new UserDTO(newUser));
            return Response.ok(token).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    public boolean authorize(UserDTO userDTO, Set<String> allowedRoles) {
        return userDTO.getRoles().stream().anyMatch(allowedRoles::contains);
    }

    public String createToken(UserDTO user) throws Exception {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        return JWT.create()
                .withClaim("username", user.getUsername())
                .withArrayClaim("roles", user.getRoles().toArray(new String[0]))
                .sign(algorithm);
    }

    public UserDTO verifyToken(String token) throws Exception {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        var verifier = JWT.require(algorithm).build();
        var decodedJWT = verifier.verify(token);

        String username = decodedJWT.getClaim("username").asString();
        Set<String> roles = Set.of(decodedJWT.getClaim("roles").asArray(String.class));
        return new UserDTO(username, roles);
    }
}
