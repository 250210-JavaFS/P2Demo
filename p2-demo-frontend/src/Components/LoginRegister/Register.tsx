import axios from "axios"
import { Button, Container, Form } from "react-bootstrap"

export const Register:React.FC = () => {

    //TODO: gather user input like with our hypotenuse calculator

    //Hardcoding a new user registration with axios 
    //axios is a way to send HTTP requests from React
    const register = async () => {

        //POST request with hardcoded user info
        const response = await axios.post("http://3.133.155.102:8080/auth/register", {
            username:"reactUser",
            password:"password"
        })

        .then(() => {
            alert("User created!")
            //TODO: actually use the returned data

        })

        console.log(response)

    }

    return(

        <Container>
          <div>
              <h1>New here? Create an Account for free!</h1>

              <div>
                  <Form.Control
                      type="text"
                      placeholder="username"
                      name="username"
                  />
              </div>
              <div>
                  <Form.Control
                      type="password"
                      placeholder="password"
                      name="password"
                  />
              </div>

              <div>
                <Button onClick={register}>Create Account!</Button>
              </div>
          </div>
      </Container>
  )

}