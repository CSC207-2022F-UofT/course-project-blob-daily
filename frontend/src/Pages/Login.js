import React, {useState, useCallback} from "react";

import {Link, resolvePath, useNavigate} from 'react-router-dom';
import "./Login.css"
import ShowError from "../Components/showError";



function Login(props) {
    const [isHover, setIsHover] = useState(false)

    const handleMouseEnter = () => {
        setIsHover(true);
    };

    const handleMouseLeave = () => {
        setIsHover(false);
    };

    const textStyle = {
        color: isHover ? 'blue' : 'black',
    };

    return(
        <div className="login">
            <h1 className="loginTitle">BlobDaily</h1>

            <h1 className="loginDescription">Welcome to the platform where you can be a better you</h1>

            
            <h2>Login</h2>
            <input
                className="answerBox"
                value={props.username}
                onChange={(e) => {props.handleUsername(e)}}
                placeholder="Enter username..."
            />

            <input
                className="answerBox"
                value={props.password}
                onChange={(e) => {props.handlePassword(e)}}
                type="password"
                placeholder="Enter password..."
            />
            {props.usernameFailed && <ShowError error={"Username or Password doesn't exist"}/>}

            <button className="loginButton" onClick={props.handleLoginClick}>
                <p>Login</p>
            </button>
            <div className="spacing">
                <Link to="/register" style={textStyle} onMouseEnter={handleMouseEnter} onMouseLeave={handleMouseLeave}>
                    Create an account instead
                </Link>
            </div>
        </div>
    )
}

export default Login