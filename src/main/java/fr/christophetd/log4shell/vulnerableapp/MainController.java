package fr.christophetd.log4shell.vulnerableapp;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
public class MainController {

    private static final Logger logger = LogManager.getLogger("HelloWorld");

    @GetMapping("/")
    public String index(@RequestHeader("X-Api-Version") String apiVersion) {
        logger.info("Received a request for API version " + apiVersion);
        return "Hello, visitor. You need to lgin first!";
    }

    @PostMapping("/login")
    public String postBody(@RequestParam String uname, @RequestParam("password") String pwd) {
	if(uname.equals("admin") && pwd.equals("password")) {
            //return("Welcome Back Admin" + pwd);
            return("<!doctype html> <html lang=\"en\"> <head> <meta charset=\"utf-8\"> <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"> <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"> <link href=\"https://unpkg.com/tailwindcss@2.2.4/dist/tailwind.min.css\" rel=\"stylesheet\"> </head> <body> <div class=\"h-screen flex\"> <div class=\"flex w-1/2 justify-center items-center bg-white\"> <form class=\"bg-white\" method=\"POST\" action=\"/login\"> <h1 class=\"text-gray-800 font-bold text-2xl mb-1\">Welcome back admin</h1> <p class=\"text-sm font-normal text-gray-600 mb-7\">You have <b>2</b> personal messages waiting for you</p> <button type=\"submit\" class=\"block w-full bg-indigo-600 mt-4 py-2 rounded-2xl text-white font-semibold mb-2\">Read your messages</button><span class=\"text-sm ml-2 hover:text-blue-500 cursor-pointer\"><a href=\"/index.html\">Log me out again</a></span></form></div> </div>");
        }
	else{

            // vulnerable code
            logger.error(uname);
	    if (uname.length() > 50) {
              uname = uname.substring(0,50);
	    }
            //return("<code> the password you entered was invalid, <u> we will log your information </u> </code>");
            return("<!doctype html> <html lang=\"en\"> <head> <meta charset=\"utf-8\"> <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"> <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"> <link href=\"https://unpkg.com/tailwindcss@2.2.4/dist/tailwind.min.css\" rel=\"stylesheet\"> </head> <body> <div class=\"h-screen flex\"> <div class=\"flex w-1/2 justify-center items-center bg-white\"> <form class=\"bg-white\" method=\"POST\" action=\"/login\"> <h1 class=\"text-red-800 font-bold text-2xl mb-1\">Sorry " + uname + "!</h1> <p class=\"text-sm font-normal text-gray-600 mb-7\">We did not recognise your username or password</p> <button type=\"submit\" class=\"block w-full bg-indigo-600 mt-4 py-2 rounded-2xl text-white font-semibold mb-2\">Forgot your password?</button> <span class=\"text-sm ml-2 hover:text-blue-500 cursor-pointer\">Please not that all unauthorized attempts will be logged!</span> </form> </div> </div>");
        }
    }
}
