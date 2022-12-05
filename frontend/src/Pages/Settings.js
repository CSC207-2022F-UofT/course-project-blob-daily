import './Settings.css'
import Navbar from "../Components/navbar";
import React, {useCallback, useState} from "react";
import {useNavigate} from "react-router-dom";
import ShowError from "../Components/showError";
import AccountApi from '../api/AccountApi';


function Settings(props){
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const navigate = useNavigate();
    const logoutReq = `http://localhost:8080/logout?sessionID=${props.sessionId}`;
    const deleteReq = `http://localhost:8080/delete?sessionID=${props.sessionId}`;
    const updateUsernameReq = `http://localhost:8080/updateUsername?sessionID=${props.sessionId}&newUsername=${username}`;
    const updatePasswordReq = `http://localhost:8080/updatePassword?sessionID=${props.sessionId}&newPassword=${password}`;

    function handleLogoutClick(){
        fetch(logoutReq, {
            method: "post"
        }).then((response) => {
            if(response.status === 200) {
                console.log("logout success");
                navigate('/');
            }
        })
    }

    function handleDeleteClick(){
        fetch(deleteReq, {
            method: "delete"
        }).then((response) => {
            if (response.status === 200) {
                console.log("delete success");
                navigate('/');
            }
        })
    }

    function handleUsernameClick(){
        fetch(updateUsernameReq,{
            method: "patch"
        }).then((respone) =>{
            if (respone.status === 200) {
                console.log("update username success");
                navigate('/');
            }
        })
    }

    function handlePasswordClick(){
        fetch(updatePasswordReq,{
            method: "patch"
        }).then((respone) =>{
            if (respone.status === 200) {
                console.log("update password success");
                navigate('/');
            }
        })
    }

    return(
        <div>
            <Navbar curPage={3}/>
            <AccountApi sessionId={props.sessionId}/>
                <div className="settingsForm">
                    <input
                        className="answerBox"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                    <button onClick={handleUsernameClick}>Send</button>
                </div>
                {/* <div className="errorTextSettings">
                    <ShowError error={"Invalid Username"}/>
                </div> */}

                <div className="settingsForm">
                    <input
                        className="answerBox"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    <button onClick={handlePasswordClick}>Send</button>
                </div>
            {/* <div className="errorTextSettings space">
                <ShowError error={"Invalid Password"}/>
            </div> */}
                <div>
                    <button className="account" onClick={handleDeleteClick}>Delete account</button>
                </div>

                <div>
                    <button className="account" onClick={handleLogoutClick}>Logout</button>
                </div>
        </div>
    )
}

export default Settings