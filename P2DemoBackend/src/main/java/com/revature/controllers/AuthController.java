package com.revature.controllers;

import com.revature.models.DTOs.LoginDTO;
import com.revature.models.DTOs.OutgoingUserDTO;
import com.revature.models.User;
import com.revature.security.JwtTokenUtil;
import com.revature.services.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

//AuthController? Think AUTHentication/AUTHorization
//I like to put account registration and login functionality here
@RestController //Combines @Controller and @ResponseBody (makes a class a bean, and lets us send JSON responses)
@RequestMapping("/auth") //Requests ending in /auth will go to this Controller
@CrossOrigin(value = {"http://localhost:5173",
        "http://myp2bucketbjp.s3-website-us-east-1.amazonaws.com"},
        allowCredentials = "true") //This annotation will allow requests from our front end
public class AuthController {

    //autowire the AuthService to use its methods
    private final AuthService authService;

    //We also need to autowire the JWTTokenUtil and the AuthenticationManager
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(AuthService authService, JwtTokenUtil jwtTokenUtil, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
    }


    //Insert a new user (POST request)
    @PostMapping("/register") //Requests ending in /auth/register will invoke this method
    public ResponseEntity<OutgoingUserDTO> registerUser(@RequestBody User user){

        //Send the User data to the service (which will send it to the DAO)
        OutgoingUserDTO returnedUser = authService.registerUser(user);

        //Send the inserted User back to the client in a response
        return ResponseEntity.ok(returnedUser);
        //.ok() sends a 200 OK status code and allows us to send a response body

    }

    //Login (POST request)
    @PostMapping("/login")
    public ResponseEntity<OutgoingUserDTO> login(@RequestBody LoginDTO loginDTO){

        //NOTE: No more explicit session handling, and no more talking to the service layer!

        //attempt to log in
        try{
            //The AuthenticationManager from Spring Security is now in charge of username/password checks
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
            );

            //If the auth is successful, build a user object based on the logged in user data
            User loggedInUser = (User) auth.getPrincipal();

            //Finally, generate a JWT! The user will use this in subsequent requests
            String jwt = jwtTokenUtil.generateAccessToken(loggedInUser);

            System.out.println(jwt);

            //send JWT back with the user info
            return ResponseEntity.ok().body(
                    new OutgoingUserDTO(
                            loggedInUser.getUserId(),
                            loggedInUser.getUsername(),
                            loggedInUser.getRole(),
                            jwt
                    )
                    //TODO: other constructor that takes User + jwt
            );

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }

        /*
        //NOTE: we have an HttpSession coming in through parameters, implicitly included in every HTTP request
        //Login is where we set it up

        //try to login (send the loginDTO to the service)
        OutgoingUserDTO loggedInUser = authService.login(loginDTO);
        //If anything goes wrong, the service throws an exception and our global Exception handler takes over

        //If we get here, the login was successful - we can build up the User's session!
        session.setAttribute("userId", loggedInUser.getUserId());
        session.setAttribute("username", loggedInUser.getUsername());
        session.setAttribute("role", loggedInUser.getRole());

        //it's really easy to access these values with getAttribute()!
        System.out.println("User " + session.getAttribute("username") + " has logged in!");

        /* WHY store all this info in a Session?

          -It lets us store user info that can be used for checks throughout the app
            -check that the user is logged in (session != null)
            -check that a user's role is appropriate (role.equals("admin"))
            -personalize the app (use the user's name in HTTP responses to use them in the UI etc)
            -simplify and secure our URLs!
                -ex: use the stored userId in "findXByUserId" methods instead of sending it in the PATH
                -This cleans up our URLs and secures them a bit more too.

        //Return the User info to the client
        return ResponseEntity.ok(loggedInUser);

        */
    }

}
