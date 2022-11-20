import React, {useState, useCallback} from "react";

import {resolvePath, useNavigate} from 'react-router-dom';
import "./Login.css"

export var ses;

function Login() {
    const [username, setUsername] = useState("asdasd");
    const [password, setPassword] = useState("123aA!");

    const navigate = useNavigate();
    // const handleLoginClick = useCallback(() => navigate('/home', {replace: true}), [navigate]);
    const loginReq = `http://localhost:8080/login?username=${username}&password=${password}`
    
    ses = (
        async function handleLoginClick() {
            const session = (
                fetch(loginReq, {
                    method: "post",
                }).then((response) => {
                    if (response.status === 200) {
                        console.log("login success");
                        navigate('/home');
                        return response.json();
                    } else {
                        console.log(`invalid: ${response.status}`);
                    }
                }).then((data) => {
                    return data.id;
                })
            )
            const sesid = await session;
            console.log(sesid);
            return sesid;
        })


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

            <p>Password</p>
            <input
                className="answerBox"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />
            <button className="loginButton" onClick={ses}>
                <p>Login</p>
            </button>
            <p>New here? Create an account</p>
        </div>
    )
}

export default Login