import React, {useState, useCallback} from "react";

import {resolvePath, useNavigate} from 'react-router-dom';
import "./Login.css"
import ShowError from "../Components/showError";



function Login(props) {
    // const handleLoginClick = useCallback(() => navigate('/home', {replace: true}), [navigate]);

    return(
        <div className="login">
            <h1 className="loginTitle">Quest Pets</h1>

            <h1 className="loginDescription">Welcome a platform where you can be a better you</h1>

            <p>Username</p>
            <input
                className="answerBox"
                value={props.username}
                onChange={(e) => {props.handleUsername(e)}}
            />


            {props.usernameFailed && <ShowError error={"Username doesn't exist"}/>}

            <p>Password</p>
            <input
                className="answerBox"
                value={props.password}Ï
                onChange={(e) => {props.handlePassword(e)}}
                type="password"
            />
            <ShowError error={"Password or username doesn't exist"}/>

            <button className="loginButton" onClick={props.handleLoginClick}>
                <p>Login</p>
            </button>
            <p>New here? Create an account</p>
        </div>
    )
}

export default Login