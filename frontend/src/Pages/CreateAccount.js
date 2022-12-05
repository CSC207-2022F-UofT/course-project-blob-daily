import ShowError from "../Components/showError";
import React, {useState, useCallback} from "react";
import {Link, resolvePath, useNavigate} from 'react-router-dom';
import "./Login.css"

function CreateAccount(props) {
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

    return (
        <div>
            <div className="login">
                <h1 className="loginTitle">BlobDaily</h1>

                <h1 className="loginDescription">Start your journey now</h1>
                <p>Username</p>
                <input
                    className="answerBox"
                    value={props.username}
                    onChange={(e) => {props.handleUsername(e)}}
                />

                <p>Password</p>
                <input
                    className="answerBox"
                    value={props.password}
                    onChange={(e) => {props.handlePassword(e)}}
                    type="password"
                />
                {props.usernameFailed && <ShowError error={"Username or Password doesn't exist"}/>}

                <button className="loginButton" onClick={props.handleRegisterClick}>
                    <p>Register</p>
                </button>
                <div className="spacing">
                <Link to="/" style={textStyle} onMouseEnter={handleMouseEnter} onMouseLeave={handleMouseLeave}>
                    Already have an account? Login
                </Link>
            </div>
            </div>
        </div>
    )

}

export default CreateAccount