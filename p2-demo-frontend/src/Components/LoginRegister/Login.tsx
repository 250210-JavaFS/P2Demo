import axios from "axios";
import { useEffect, useRef, useState } from "react";
import { Button, Container, Form } from "react-bootstrap"
import { useNavigate } from "react-router-dom"
import { store } from "../../GlobalData/store";
import { useAuth } from "../../GlobalData/AuthContext";

export const Login:React.FC = () => {

    //!NEW! Context API!!!!
    //This is the hook we created in AuthContext.tsx
    //We want this component to be able to get and set loggedInUser data
    const {loggedInUser, setLoggedInUser} = useAuth()

    //we can use the useNavigate hook to navigate between components programatically
        //(no more manual URL changing)
    const navigate = useNavigate()

    //Using the useRef and useEffect hooks to focus our username input box on component load
    const usernameRef = useRef<HTMLInputElement>(null);

    useEffect(() => {
        //if the current value of the ref is truthy...
        if (usernameRef.current) {
            usernameRef.current.focus(); //focus it so the user can type right away
        }
    }, []); //remember [] means this happens on component load


    //Defining a state object to store the user's login credentials
    const[loginCreds, setLoginCreds] = useState({
        username:"",
        password:""
    }) //could have defined an interface for this, but we didn't


    //Function to store use inputs
    const storeValues = (event:React.ChangeEvent<HTMLInputElement>) => {

        //I'm going to store the name and value of the inputs for ease of use below
        const name = event.target.name //name is an attribute we set on the input boxes
        const value = event.target.value //value is the actual value in the input at the time

        //"Take whatever input was changed, and set the matching state field to the value of that input"
        //[name] can be EITHER username or password. This ugly code lends flexibility. 
        //This syntax is less necessary if we just have 2 fields, but wayyyy more useful if there are like, 50
        setLoginCreds((loginCreds) => ({...loginCreds, [name]:value}))

    }


    //Function to make the actual login request
    //navigates to /users if a manager logged in, and /games if a user logged in
    const login = async () => {

        //TODO: make sure the username/password are present before proceeding

        try{

            //EC2 AXIOS REQUEST
//          const response = await axios.post("http://3.133.155.102:8080/auth/login",           loginCreds, {withCredentials:true})

            const response = await axios.post("http://localhost:8081/auth/login",           loginCreds, {withCredentials:true})
            //withCredentials lets us interact with sessions on the backend
            //every request that depends on the user being logged in, being an admin, etc, needs this

            //!NEW! Context API!
            
            //Setting the logged in User data thanks to useAuth 
            setLoggedInUser(response.data)

            console.log(loggedInUser)

            alert("Welcome " + loggedInUser?.username)

            /* OLD - store.ts, before Context API
            //if the catch doesn't run, login was successful! save the data to our global store, then switch components
            store.loggedInUser = response.data //this is our logged in user data from the backend

            //greet the user with this newly stored data
            alert(store.loggedInUser.username + " has logged in! Welcome.")
            */

            //users will get sent to users component if they're an "admin", or the games component if they're a "user"
            if(loggedInUser?.role === "manager"){
                navigate("/users")
            } else {
                navigate("/games")
            }

        } catch {
            alert("Our biggest error is settling on bad practices... :(")
        }

    }



    return(
        /*Bootstrap gives us this Container element that does some default padding and centering*/
        <Container> 

            <h1 className="mb-5">Welcome</h1>
                <h3>Please Log In:</h3>
                
                <div>
                    <Form.Control
                        type="text"
                        placeholder="username"
                        name="username"
                        ref={usernameRef} //attach our usernameRef here!
                        //This is how our useRef knows what to focus.
                        onChange={storeValues}
                    />
                </div>

                <div>
                    <Form.Control
                        type="password"
                        placeholder="password"
                        name="password"
                        onChange={storeValues}
                    />
                </div>
                

            <Button variant="outline-success m-1" onClick={login}>Login</Button>
            <Button variant="outline-dark" onClick={()=>navigate("/register")}>Register</Button>
        </Container>
    )


}