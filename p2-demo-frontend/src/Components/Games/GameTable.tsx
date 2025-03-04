import { useAuth } from "../../GlobalData/AuthContext"

export const GameTable:React.FC = () => {

    //!NEW! Context API 

    //We want this component to have access to the loggedInUser data...
        //Since we're hypothetically getting all games be user Id

    //BUT... there's no need to change loggedInUser data from here.
    //So we can leave out the mutator in our useAuth! VERY well encapsulated code
    const {loggedInUser} = useAuth()

    return(
        <>
            <h1>{loggedInUser?.username}'s games</h1>
            <h3>This is where the games would be... If I had some!!</h3>
        </>
    )
}