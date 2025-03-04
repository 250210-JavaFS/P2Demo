//This is our implementation of Context API
//Context API lets us send data around the frontend in a secure and encapsulated way
    //Better than the store.ts that lives in this folder! We won't use that anymore

//First, we'll define a LoggedInUser interface to structure our data
interface loggedInUser{
    userId:string;
    username:string;
    role:string;
}

//Define an AuthContextType interface to define initial state and a mutator for user data
