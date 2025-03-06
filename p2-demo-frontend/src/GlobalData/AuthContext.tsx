//This is our implementation of Context API
//Context API lets us send data around the frontend in a secure and encapsulated way
    //Better than the store.ts that lives in this folder! We won't use that anymore

import { createContext, ReactNode, useContext, useEffect, useState } from "react";

//First, we'll define a LoggedInUser interface to structure our data
interface LoggedInUser{
    userId:string;
    username:string;
    role:string;
    token:string;
}

//Define an AuthContextType interface to define initial state and a mutator for user data
interface AuthContextType{
    loggedInUser: LoggedInUser | null;
    setLoggedInUser: (user:LoggedInUser | null) => void
}

//Use the AuthContextType to create our overall AuthContext
//This is going to help us provider the user data to other components
const AuthContext = createContext<AuthContextType | null>(null)

//Now, we can finally create our component - AuthProvider
//We'll be using the AuthContextType declared above
//BIG PICTURE: we'll wrap components with this AuthProvider to PROVIDE data to them
    //see Main.tsx!!!
export const AuthProvider:React.FC<{children:ReactNode}> = ({children}) => {

    //Define a useState for loggedInUser data (we'll also localstorage here)
    const[loggedInUser, setLoggedInUser] = useState<LoggedInUser|null>(
        JSON.parse(localStorage.getItem("loggedInUser") || 'null')
    ) 
    //The default value of our loggedInUser state is whatever's in localStorage

    //This useEffect will either set new loggedInuser data, or remove it (if it's falsy)
        //when loggedInUser state changes and on initial render
    useEffect(() => {
        if(loggedInUser){
            localStorage.setItem('loggedInUser', JSON.stringify(loggedInUser))
        } else {
            localStorage.removeItem('loggedInUser')
        }
    }, [loggedInUser])
    //NOTE: we should have login/role checks in any component that requires successful login

    //providing the loggedInUser data and setLoggedInUser mutator to anything wrapped in the AuthProvider (which will the entire app in our case - see Main.tsx)
    return(
        <AuthContext.Provider value={{loggedInUser, setLoggedInUser}}>
            {children}
        </AuthContext.Provider>
    )
    //This is what makes the data global! Any component we wrap in this Provider will be PROVIDED with this data and mutator

}

//Finally, we'll define a hook that lets us access the user info in the AuthContext
//This is how we can decide what each component gets
    //Maybe one component needs to get AND set user data
    //Maybe others only need to get it, and have no opportunity to set it
export const useAuth = () : AuthContextType => {

    //extract the current context value, to check if it exists
    //(it WILL exist if the component is wrapped in the provider)
    const context = useContext(AuthContext) // remember, AuthContext was defined above

    //if the component trying to access user data isn't wrapped in AuthProvider...
    //throw an error
    if(!context){
        throw new Error('useAuth must be used from within an AuthProvider')
    }

    return context //finally give the component the data they need

}