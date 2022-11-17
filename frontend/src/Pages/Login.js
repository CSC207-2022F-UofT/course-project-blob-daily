import React, {useState, useCallback} from "react";
import {useNavigate} from 'react-router-dom';
import "./Login.css"
import ShowError from "../Components/showError";

function Login() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const navigate = useNavigate();

    const handleLoginClick = useCallback(() => navigate('/home', {replace: true}), [navigate]);


    return(
        <div className="login">
            <h1 className="loginTitle">Quest Pets</h1>

            <h1 className="loginDescription">Welcome a platform where you can be a better you</h1>

            <p>Username</p>
            <input
                className="answerBox"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
            />
            <ShowError error={"Username doesn't exist"}/>

            <p>Password</p>
            <input
                className="answerBox"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                type="password"
            />
            <ShowError error={"Password doesn't exist"}/>

            <div className="loginButton" onClick={handleLoginClick}>
                <p>Login</p>
            </div>
            <p>New here? Create an account</p>
        </div>
    )
}

export default Login