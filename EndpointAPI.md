This is a page that will describe how the Endpoint API works.

# Introduction #

The Endpoint API is an API that the Client will be able to use via a parameter passed in through that Endpoint java program.


# Details #

An example of using this API would be

java -jar Endpoint.jar "{Command : createUser, Username : Bob }"

The first argument to the program is a JSON string. It does not matter if what order the key value pairs are given in.

Each function within the class will be of the following form.
The "Command" will be called via reflection from the main function in the program.

public void createUser(CommandArguments arguments)
{
}