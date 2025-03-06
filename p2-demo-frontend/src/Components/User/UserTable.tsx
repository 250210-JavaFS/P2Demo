import axios from "axios"
import { useEffect, useState } from "react"
import { Button, Container, Table } from "react-bootstrap"
import { User } from "../../Interfaces/User"
import { useAuth } from "../../GlobalData/AuthContext"

export const UserTable:React.FC = () => {

    //state object to store the User Array from the backend
    const [users, setUsers] = useState<User[]>([])

    //This component only needs GET access to user info, not SET access
    const {loggedInUser} = useAuth()


    //useEffect - we'll call a GET request for all users when the component loads
    useEffect(() => {

        //TODO: make sure the user is a manager, redirect them to login if not

        getAllUsers()

    }, []) //we want this to run once on load, so we use []


    //Function to get all users from the backend (HTTP request!)
    const getAllUsers = async () => {

        try{
            const response = await axios.get("http://localhost:8081/users", {
                headers: {
                    'Authorization':`Bearer ${loggedInUser?.token}`
                }
            })
            //no withCredentials - that's for session-based architectures
            //now, we're sending the JWT in the auth header instead to verify the user

            //TODO: error throwing code

            console.log(response.data) //print out the data just to see it

            //store the user data in our "users" state object 
            setUsers(response.data) 

        } catch {
            alert("Something went wrong trying to fetch users")
        }
    }

    //function that does a fake update delete (wanna show how to extract data from a map)
    const updateUser = (user:User) => {
        alert("User " + user.userId + " has been fake updated or deleted")

        //TODO: Could definitely make another call to getAllUsers for automatic updates
        //TODO2: Cache the list of users and update THAT so we don't make a repeat DB call
    }


    return(
        <Container className="d-flex flex-column align-items-center mt-3">
            
            <h3>Users: </h3>

            <Table className="table-dark table-hover table-striped w-50">
                <thead>
                    <tr>
                        <th>User Id</th>
                        <th>Username</th>
                        <th>Role</th>
                        <th>Options</th>
                    </tr>
                </thead>
                <tbody className="table-secondary">
                    {users.map((user:User) => (
                        <tr key={user.userId}>
                            <td>{user.userId}</td>
                            <td>{user.username}</td>
                            <td>{user.role}</td>
                            <td>
                                <Button variant="outline-success" onClick={() => updateUser(user)}>Promote</Button>
                                <Button variant="outline-danger" onClick={() => updateUser(user)}>Fire</Button>
                            </td>
                        </tr>
                    ))} 
                {/* WHY parenthesis to open the arrow func? because it implicitly returns */}
                </tbody>
            </Table>

        </Container>
    )

}