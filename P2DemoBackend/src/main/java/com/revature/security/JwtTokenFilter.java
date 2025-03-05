package com.revature.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.revature.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

//This Util Class will make sure the requests hitting our server has a JWT in the Authorization header

@Component //extending OncePerRequestFilter guarantees a single execution per request.
public class JwtTokenFilter extends OncePerRequestFilter {

    //This class will use a few methods found in JwtUtil
    private final JwtTokenUtil jwtUtil;

    @Autowired
    public JwtTokenFilter(JwtTokenUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /*This method is called for each request that hits our server
    It checks if the request has a valid JWT in the Authorization header
    It uses a lot of helper methods that we've defined below*/
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //If the Authorization header of the request doesn’t contain a Bearer token,
        //Run the filter without doing anything else. (The request will fail later if JWT is needed)
        if (!hasAuthorizationBearer(request)) {
            filterChain.doFilter(request, response);
            System.out.println("hello you're missing a bearer token! " +
                    "this is only a problem if the request you sent needs one (login/register don't need one)");
            return;
        }

        //If the Authorization header contains a Bearer token...
        //extract the token to try verifying it.
        String token = getAccessToken(request);

        //if the token is not valid (expired, malformed, etc.)
        //Run the filter without doing anything else. (The request will fail later if JWT is needed)
        if (!jwtUtil.validateAccessToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        //If the token IS verified,
        //Update the authentication context with the user details (ID, username, role).
        //Then continue the filter chain. (The request will pass through)
        setAuthenticationContext(token, request);
        filterChain.doFilter(request, response);
    }

    //This method checks the Authorization header in our HTTP request to check for the JWT
    //(The first check in our filter)
    private boolean hasAuthorizationBearer(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (ObjectUtils.isEmpty(header) || !header.startsWith("Bearer ")) {
            return false;
        }
        return true; //if the "Bearer" header is present, return true
    }

    //This method extracts the JWT from the Authorization header
    //(Before the second check in our filter)
    private String getAccessToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        //Auth header will look like this: "Bearer: {your.jwt.here}"
        String token = header.split(" ")[1].trim();
        return token;
    }

    /* this method extracts the user details from the JWT token (including their authorities),
    creates an Authentication object with these details,
    and sets this Authentication object in the security context.
    This lets Spring determine who the user is and what privileges they have */
    private void setAuthenticationContext(String token, HttpServletRequest request) {
        User user = (User) getUserDetails(token);

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole()));

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user,
                null,
                authorities);

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        //After all the configurations above, we tell Spring who the user is and what their role is
        //This information is stored in the authentication object we created above
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    //This is used to extract the userId, username, and role from the JWT in the method above
    private UserDetails getUserDetails(String token) {
        User userDetails = new User();

        //use the extractor methods we wrote in JwtTokenUtil to get the userId and username
        userDetails.setUserId(jwtUtil.extractUserId(token));
        userDetails.setUsername(jwtUtil.extractUsername(token));
        userDetails.setRole(jwtUtil.extractRole(token));

        System.out.println("user: " + userDetails);
        System.out.println("role: " + userDetails.getRole());

        return userDetails;
    }

}